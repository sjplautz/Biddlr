package classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.example.biddlr.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import interfaces.JobDataListener;
import interfaces.UserDataListener;

public class DatabaseManager {

    public static DatabaseManager shared = new DatabaseManager();

    public User currentUser;

    public FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference activeJobRef;
    private DatabaseReference expiredJobRef;
    private DatabaseReference userRef;
    private StorageReference imgRef;

    public void setUp() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // create reference to active job table
        activeJobRef = database.getReference("active_job");

        // create reference to expired job table
        expiredJobRef = database.getReference("expired_job");

        // create reference to user table
        userRef = database.getReference("user");

        storage = FirebaseStorage.getInstance();
        imgRef = storage.getReference("images");

        userRef.child(getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("CURRENT USER", "user: " + user);
                currentUser = user;
            }

            @Override
            public void onCancelled(DatabaseError error) { }
        });
    }

    /* JOB DATABASE */

    /**
     * Adds new job to database
     * @param job
     * @param image
     */
    public void addNewJob(Job job, byte[] image) {
        String id = activeJobRef.push().getKey();
        if(image != null) {
            StorageReference tmpRef = imgRef.child(id);
            tmpRef.putBytes(image);
        }
        job.setJobID(id);
        activeJobRef.child(id).setValue(job);
    }

    public void addJobBid(Job job, Double bid) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(job.getJobID() + "/bids" , job.getBids());
        updates.put(job.getJobID() + "/currentBid" , job.getCurrentBid());

        activeJobRef.updateChildren(updates);
    }

    /**
     * Active jobs in the database
     * @param limit max number of jobs
     * @param listener fragment to receive jobs
     */
    public void setActiveJobsListener(int limit, final JobDataListener listener) {
        activeJobRef.orderByChild("status").equalTo("IN_BIDDING").limitToFirst(limit).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("ACTIVE JOBS", "job: " + job);
                listener.newDataReceived(job);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("JOB CHANGED", "job: " + job);
                listener.newDataReceived(job);
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    /**
     * Returns job associated with an ID
     * @param jobID
     * @param listener fragment to receive job
     */
    public void setJobFromIDListener(String jobID, final JobDataListener listener) {
        activeJobRef.orderByChild("jobID").equalTo(jobID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("JOB ID QUERY", "job: " + job);
                listener.newDataReceived(job);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("JOB CHANGED", "job: " + job);
                listener.newDataReceived(job);
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    /**
     * All jobs posted by a user
     * @param userID
     * @param limit max number of jobs
     * @param listener fragment to receive jobs
     */
    public void setJobsForPosterListener(String userID, int limit, final JobDataListener listener) {
        activeJobRef.orderByChild("posterID").equalTo(userID).limitToFirst(limit).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("USER JOB QUERY", "job: " + job);
                listener.newDataReceived(job);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("JOB CHANGED", "job: " + job);
                listener.newDataReceived(job);
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    /**
     * Jobs within a coordinate and radius span
     * @param coordinate central coordinate
     * @param radius distance from coordinate query will span
     * @param limit max number of jobs
     * @param listener fragment to receive jobs
     */
    public void setJobsForLocationListener(LatLngWrapped coordinate, Double radius, int limit, final JobDataListener listener) {
        Double maxLat = coordinate.lat + radius;
        final Double maxLng = coordinate.lng + radius;
        Double minLat = coordinate.lat - radius;
        final Double minLng = coordinate.lng - radius;

        activeJobRef.orderByChild("coordinates/lat").startAt(minLat).endAt(maxLat).limitToFirst(limit).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                Double lng = job.getCoordinates().lng;
                if ( minLng <= lng && lng <= maxLng && job.getStatus().toString().equals("in bidding")) {
                    Log.d("JOB LOC QUERY", "job: " + job);
                    listener.newDataReceived(job);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("JOB CHANGED", "job: " + job);
                listener.newDataReceived(job);
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    /* USER DATABASE */

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    /**
     * Adds new user to database
     * @param user
     */
    public void addNewUser(User user) {
        String id = getCurrentUser().getUid();
        user.setUserID(id);
        userRef.child(id).setValue(user);
    }

    public void addBidForUser(User user) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(user.getUserID() + "/biddedJobs" , user.getBiddedJobs());

        userRef.updateChildren(updates);
    }

    /**
     * Updates user data associated with userID
     * @param user
     */
    public void updateUser(User user) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(user.getUserID() , user);

        userRef.updateChildren(updates);
    }

    /**
     * Returns user associated with an ID
     * @param userID
     * @param listener fragment to receive the user
     */
    public void setUserFromIDListener(String userID, final UserDataListener listener) {
        userRef.orderByChild("userID").equalTo(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("USER ID QUERY", "user: " + user);
                listener.newDataReceived(user);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("USER CHANGED", "user: " + user);
                listener.newDataReceived(user);
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public StorageReference getImgRef(String jobId){
        return imgRef.child(jobId);
    }

    public void setImage(String id, final ImageView iv){
        iv.setImageResource(R.drawable.ic_camera_default_gray);
        StorageReference ref = imgRef.child(id);
        ref.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            iv.setImageBitmap(bmp);
            iv.setBackgroundColor(iv.getContext().getResources().getColor(R.color.lightGray, null));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv.setBackgroundColor(iv.getContext().getResources().getColor(R.color.colorPrimary, null));
            }
        });
    }
}

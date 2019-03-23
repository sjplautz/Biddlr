package classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ImageView;

import com.example.biddlr.ExploreFragment;
import com.example.biddlr.JobListAdapter;
import com.example.biddlr.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;

import interfaces.DataListener;

public class DatabaseManager {

    public static DatabaseManager shared = new DatabaseManager();
    private ArrayList<Job> jobs = new ArrayList<Job>();
    private ArrayList<User> users = new ArrayList<User>();

    public FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference jobRef;
    private DatabaseReference userRef;
    private StorageReference imgRef;

    private JobListAdapter jobsAdapter = null;

    public void setUp() {
        mAuth = FirebaseAuth.getInstance();

        mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        jobRef = database.getReference("job");

        storage = FirebaseStorage.getInstance();
        imgRef = storage.getReference("images");

        jobsAdapter = new JobListAdapter(jobs);
    }

    public JobListAdapter getExploreAdapter() {
        return jobsAdapter;
    }

    // Old way
    public void setJobListener() {
        ChildEventListener jobChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("FIREBASE", "job: " + job);

                jobs.add(0, job);

                if (jobsAdapter != null) {
                    jobsAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
        jobRef.addChildEventListener(jobChildEventListener);
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    // New way
    public void getAllJobs(int limit, final DataListener listener) {
        jobRef.limitToFirst(limit).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("ALL JOBS", "job: " + job);
                listener.newDataReceived(job);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    // Get all jobs posted by a user
    public void getJobsForPoster(String userID, int limit, final DataListener listener) {
        jobRef.orderByChild("posterID").equalTo(userID).limitToFirst(limit).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("USER JOB QUERY", "job: " + job);
                listener.newDataReceived(job);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    // Haven't tested this
    public void getJobsByLocation(LatLngWrapped coordinate, Double radius, int limit, final DataListener listener) {
        Double maxLat = coordinate.lat + radius;
        final Double maxLng = coordinate.lng + radius;
        Double minLat = coordinate.lat - radius;
        final Double minLng = coordinate.lng - radius;

        jobRef.orderByChild("coordinates").orderByChild("lat").startAt(maxLat).endAt(minLat).limitToFirst(limit).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                Double lng = job.getCoordinates().lng;
                if ( minLng <= lng && lng <= maxLng) {
                    Log.d("JOB LOC QUERY", "job: " + job);
                    listener.newDataReceived(job);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    //adds job to front of shared jobs lists
    public void addJob(Job job, byte[] image) {
        String id = jobRef.push().getKey();
        if(image != null) {
            StorageReference tmpRef = imgRef.child(id);
            tmpRef.putBytes(image);
        }
        job.setJobID(id);
        jobRef.child(id).setValue(job);
    }

    public void setImage(String id, final ImageView iv){
        iv.setImageResource(R.drawable.ic_biddlrlogo);
        StorageReference ref = imgRef.child(id);
        ref.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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
    
    // Users
    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

//    public ArrayList<User> getUsers() {
//        return users;
//    }

    public void addUser(User user) {
//        users.add(user);
        userRef.child("user").child(user.getUserID()).setValue(user);
    }
}

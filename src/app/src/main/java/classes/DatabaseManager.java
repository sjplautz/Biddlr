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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

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

        database = FirebaseDatabase.getInstance();
        jobRef = database.getReference("job");

        storage = FirebaseStorage.getInstance();
        imgRef = storage.getReference("images");

//        jobs = new ArrayList<Job>();
//        users = new ArrayList<User>();

        jobsAdapter = new JobListAdapter(jobs);
    }

    public JobListAdapter getExploreAdapter() {
        return jobsAdapter;
    }

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

    public ArrayList<Job> getJobsByLocation() {
        ArrayList<Job> jobList = new ArrayList<>();
        jobRef.orderByChild("location").limitToFirst(10).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("LOC QUERY", "location: " + job.getLocation());
                jobs.add(0, job);
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
        return jobList;
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

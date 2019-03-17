package classes;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DatabaseManager {

    public static DatabaseManager shared = new DatabaseManager();
    private ArrayList<Job> jobs;
    private ArrayList<User> users;
    public FirebaseAuth mAuth;
    public FirebaseDatabase database;
    public DatabaseReference jobRef;
    public DatabaseReference userRef;
    private FirebaseStorage storage;
    private StorageReference imgRef;

    public void setUp() {
        shared.mAuth = FirebaseAuth.getInstance();

//        shared.jobs.add( new Job("Window Washing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
//        shared.jobs.add( new Job("Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
//        shared.jobs.add( new Job("Sort Change", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
//        shared.jobs.add( new Job("Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
//        shared.jobs.add( new Job("Seal 1,000 envelopes", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
//        shared.jobs.add( new Job("Window Washing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
//        shared.jobs.add( new Job("Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.I have 20 jars of unsorted change that needs to be rolled. ",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
//        shared.jobs.add( new Job("Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
//        shared.jobs.add( new Job("Seal 1,000 envelopes", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
//        shared.jobs.add( new Job("Window Washing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
//        shared.jobs.add( new Job("Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
//        shared.jobs.add( new Job("Sort Change", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
//        shared.jobs.add( new Job("Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
//        shared.jobs.add( new Job("Seal 1,000 envelopes", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));

        shared.users = new ArrayList<User>();
//        shared.users.add( new User(0, "password", "John", "Smith", "", "", "", 4.0, 4.0, null, null, null));
//        shared.users.add( new User(1,"password", "Jane", "Doe"));
    }

    public void setJobListener() {
        shared.database = FirebaseDatabase.getInstance();
        shared.jobRef = database.getReference("job");
        shared.jobs = new ArrayList<Job>();

        shared.storage = FirebaseStorage.getInstance();
        shared.imgRef = storage.getReference("images");

        ChildEventListener jobChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("FIREBASE", "job: " + job);
                shared.jobs.add(0, job);
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
        DatabaseManager.shared.jobRef.addChildEventListener(jobChildEventListener);
    }

    public ArrayList<Job> getJobs() {
        return shared.jobs;
    }

    //adds job to front of shared jobs lists
    public void addJob(Job job, byte[] image) {
        String id = shared.jobRef.push().getKey();
        StorageReference tmpRef = shared.imgRef.child(id);
        tmpRef.putBytes(image);
        job.setJobID(id);
        shared.jobRef.child(id).setValue(job);
    }
    
    // Users
    public ArrayList<User> getUsers() {
        return shared.users;
    }

    public void addUser(User user) {
        shared.users.add(user);
        shared.userRef.child("user").child(user.getUserID()).setValue(user);
    }
}

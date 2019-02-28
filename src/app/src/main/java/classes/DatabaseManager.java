package classes;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DatabaseManager {

    public static DatabaseManager shared = new DatabaseManager();
    private ArrayList<Job> jobs;
    private ArrayList<User> users;
    public FirebaseAuth mAuth;

    public void setUp() {
        DatabaseManager.shared.mAuth = FirebaseAuth.getInstance();

        shared.jobs = new ArrayList<Job>();
        shared.jobs.add( new Job(0,"Window Washing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(1,"Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(2,"Sort Change", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(3,"Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(4,"Seal 1,000 envelopes", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(5,"Window Washing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(6,"Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.I have 20 jars of unsorted change that needs to be rolled. ",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(8,"Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(9,"Seal 1,000 envelopes", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(10,"Window Washing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(11,"Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(12,"Sort Change", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(13,"Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(14,"Seal 1,000 envelopes", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));

        shared.users = new ArrayList<User>();
        shared.users.add( new User(0, "password", "John", "Smith", "", "", "", 4.0, 4.0, null, null, null));
        shared.users.add( new User(1,"password", "Jane", "Doe"));
    }

//    public void createAccount(String email, String password) {
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
////                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
////                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }

//    public void signIn(String email, String password, final Runnable signInSucceeded, final Runnable signInFailed) {
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            signInSucceeded.run();
//                        } else {
//                            signInFailed.run();
//                        }
//                    }
//                });
//    }

    // Jobs
    public ArrayList<Job> getJobs() {
        return shared.jobs;
    }

    //adds job to front of shared jobs lists
    public void addJob(Job job) {
        shared.jobs.add(0, job);
    }

    // Users
    public ArrayList<User> getUsers() {
        return shared.users;
    }

    public void addUser(User user) {
        shared.users.add(user);
    }
}

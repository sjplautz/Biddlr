package classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.example.biddlr.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enums.JobStatus;
import interfaces.JobDataListener;
import interfaces.UserDataListener;

public class DatabaseManager {

    public static DatabaseManager shared = new DatabaseManager();

    public User currentUser;

    public FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    private DatabaseReference activeJobRef;
    private DatabaseReference expiredJobRef;
    private DatabaseReference completedJobRef;
    private DatabaseReference userRef;
    private StorageReference jobImgRef;
    private StorageReference userImgRef;

    public CollectionReference dialogsRef;

    public void setUp() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // firestore
        dialogsRef = firestore.collection("dialogs");

        // create reference to active job table
        activeJobRef = database.getReference("active_job");

        // create reference to expired job table
        expiredJobRef = database.getReference("expired_job");

        // create reference to completed job table
        completedJobRef = database.getReference("completed_job");

        // create reference to user table
        userRef = database.getReference("user");

        storage = FirebaseStorage.getInstance();
        jobImgRef = storage.getReference("images/jobs");
        userImgRef = storage.getReference("images/users");
    }

    /**
     * Set the current logged in user object
     */
    public void setCurrentUser() {
        userRef.child(getFirebaseUser().getUid()).addValueEventListener(new ValueEventListener() {
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
            StorageReference tmpRef = jobImgRef.child(id);
            tmpRef.putBytes(image);
        }
        job.setJobID(id);
        activeJobRef.child(id).setValue(job);
    }

    /**
     * Updates the bids and currentBid field of a job
     * @param job
     * @param bid
     */
    public void addJobBid(Job job, Double bid) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(job.getJobID() + "/bids" , job.getBids());
        updates.put(job.getJobID() + "/currentBid" , job.getCurrentBid());

        activeJobRef.updateChildren(updates);
    }

    /**
     * Updates job data associated with jobID
     * @param job Job to update
     */
    public void updateJob(Job job){
        Map<String, Object> updates = new HashMap<>();
        updates.put(job.getJobID(), job);

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
                listener.dataChanged(job);
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("JOB REMOVED", "job: " + job);
                listener.dataRemoved(job);
            }
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
                listener.dataChanged(job);
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("JOB REMOVED", "job: " + job);
                listener.dataRemoved(job);
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void setCompletedJobsForBidderListener(String userID, final JobDataListener listener) {
        Log.d("COMPLETED_OUTER", "Query was called at least");
        completedJobRef.orderByChild("selectedBidderID").equalTo(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("UNIQUE", "job: " + job);
                listener.newDataReceived(job);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
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
                listener.dataChanged(job);
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("JOB REMOVED", "job: " + job);
                listener.dataRemoved(job);
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        userRef.orderByChild("userID").equalTo(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("BIDDED JOB QUERY", "user: " + user);
                if(user.getBiddedJobs() != null) {
                    for (String jobId : user.getBiddedJobs().keySet()) {
                        setJobFromIDListener(jobId, listener);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Jobs within a coordinate and radius span
     * @param coordinate central coordinate
     * @param radius distance from coordinate query will span
     * @param limit max number of jobs
     * @param listener fragment to receive jobs
     */
    public ChildEventListener setJobsForLocationListener(LatLngWrapped coordinate, Double radius, int limit, final JobDataListener listener) {
        Double maxLat = coordinate.lat + radius;
        final Double maxLng = coordinate.lng + radius;
        Double minLat = coordinate.lat - radius;
        final Double minLng = coordinate.lng - radius;

        ChildEventListener eventHandle = activeJobRef.orderByChild("coordinates/lat").startAt(minLat).endAt(maxLat).limitToFirst(limit).addChildEventListener(new ChildEventListener() {
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
                listener.dataChanged(job);
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Job job = dataSnapshot.getValue(Job.class);
                Log.d("JOB REMOVED location jobs listener", "job: " + job.getTitle());
                listener.dataRemoved(job);
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        return eventHandle;
    }

    /**
     * Gets jobs that match the given search parameters
     * @param coordinate User's current coordinates
     * @param filter The text filter to apply
     * @param radius The distance filter to apply
     * @param listener Fragment to receive jobs
     * @return The ChildEventListener that is created
     */
    public ChildEventListener setJobsFromSearchListener(LatLngWrapped coordinate, final String filter, final double radius, final JobDataListener listener){
        final Double maxLat = coordinate.lat + (radius / 69.2);
        final Double maxLng = coordinate.lng + (radius / 55.2);
        final Double minLat = coordinate.lat - (radius / 69.2);
        final Double minLng = coordinate.lng - (radius / 55.2);
        return activeJobRef.orderByChild("status").equalTo("IN_BIDDING").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Job job = dataSnapshot.getValue(Job.class);
                if(job.getTitle().contains(filter)){
                    if(radius == -1 || (job.getCoordinates().getLat() > minLat && job.getCoordinates().getLat() < maxLat)) {
                        if(radius == -1 || (job.getCoordinates().getLng() > minLng && job.getCoordinates().getLng() < maxLng)){
                            Log.d("SEARCH_JOB_ADDED", "job: " + job.getTitle());
                            listener.newDataReceived(job);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void markJobCompleted(Job job){
        if(job.getStatus() == JobStatus.COMPLETED){
            completedJobRef.child(job.getJobID()).setValue(job);

            activeJobRef.child(job.getJobID()).removeValue();
        }
    }

    /* USER DATABASE */

    public FirebaseUser getFirebaseUser() {
        return mAuth.getCurrentUser();
    }

    /**
     * Adds new user to database
     * @param user
     */
    public void addNewUser(User user) {
        String id = getFirebaseUser().getUid();
        user.setUserID(id);
        userRef.child(id).setValue(user);

        DatabaseManager.shared.setCurrentUser();
    }

    /**
     * Updates the biddedJobs field for a user on the database
     * @param user
     */
    public void addBidForUser(User user) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(user.getUserID() + "/biddedJobs" , user.getBiddedJobs());

        userRef.updateChildren(updates);
    }

    public void setUserPoints(User user){

    }

    /**
     * Updates user data associated with userID
     * @param user
     */
    public void updateUser(User user, byte[] image) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(user.getUserID() , user);
        if(image != null){
            StorageReference tmpRef = userImgRef.child(user.getUserID());
            tmpRef.putBytes(image);
        }

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

    /**
     * Returns all users associated with an ID from ids
     * @param ids list of userID strings
     * @param listener fragment to receive the users
     */
    public void setUsersFromListListener(final List<String> ids, final UserDataListener listener){
        userRef.orderByChild("userID").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                if(ids.contains(user.getUserID())){
                    Log.d("USER ADDED", "user: " + user);
                    listener.newDataReceived(user);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public StorageReference getImgRef(String jobId){
        return jobImgRef.child(jobId);
    }

    public StorageReference getUserRef(String userId) { return userImgRef.child(userId); }

    /**
     * Searches for an image tied to a Job. If found, sets the ImageView to display that image
     * @param id The job ID
     * @param iv The ImageView to display with
     */
    public void setJobImage(String id, final ImageView iv){
        iv.setImageResource(R.drawable.ic_camera_default_gray);
        StorageReference ref = jobImgRef.child(id);
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
            iv.setBackgroundColor(iv.getContext().getResources().getColor(R.color.gray, null));
            }
        });
    }

    /**
     * Searches for an images tied to a user. If found, set the ImageView to display that image
     * @param id The user ID
     * @param iv The ImageView to display with
     */
    public void setUserImage(String id, final ImageView iv) {
        iv.setImageResource(R.drawable.baseline_person_24);
        StorageReference tmpRef = userImgRef.child(id);
        tmpRef.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iv.setImageBitmap(bmp);
            }
        });
    }

    public void addNewDialogForUsers(final ArrayList<User> users) {
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add(users.get(0).getId());
        userIds.add(users.get(1).getId());
        Collections.sort(userIds);
        dialogsRef.whereEqualTo("userIds", userIds).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {
                        Log.d("DIALOG", "Document not found");
                        addDialog(users);
                    } else {
                        Log.d("DIALOG", "Document found!");
                    }
                } else {
                    Log.d("DIALOG", "Failed with: ", task.getException());
                }
            }
        });
    }

    public void addDialog(ArrayList<User> users) {
        Dialog d = new Dialog("id", "name", "photo", users, new ChatMessage("id", null, ""), 0);
        HashMap<String, Object> data = d.getRepresentation();
        dialogsRef.add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("DIALOG", "DocumentSnapshot written with ID: " + documentReference.getId());
                        setDialogId(documentReference.getId());
                        ChatMessage m = new ChatMessage("id", currentUser,"Chat message");
                        HashMap<String, Object> mData = m.getRepresentation();
                        dialogsRef.document(documentReference.getId()).collection("messages").add(mData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("MESSAGE", "DocumentSnapshot written with ID: " + documentReference.getId());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DIALOG", "Error adding document", e);
                    }
                });
    }

    public void setDialogId(String dialogId) {
        dialogsRef.document(dialogId).update(
                        "id", dialogId
                );
    }

    public ArrayList<Dialog> getDialogs() {
        ArrayList<Dialog> dialogs = new ArrayList<Dialog>();
        return dialogs;
    }

    public ArrayList<ChatMessage> getMessages() {
//        ChatMessage m = new ChatMessage("abc", currentUser, "This is a messages");
//        ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
//        messages.add(m);

        return null;
    }

    public ChatMessage addNewMessage(final String dialogID, final String input) {
        ChatMessage m = new ChatMessage("id", currentUser, input);
        HashMap<String, Object> data = m.getRepresentation();



        dialogsRef.document(dialogID).collection("messages").add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("MESSAGE", "DocumentSnapshot written with ID: " + documentReference.getId());
                        updateLastMessage(dialogID, documentReference.getId(), input );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MESSAGE", "Error adding document", e);
                    }
                });

        return m;
    }

    public void updateLastMessage(String dialogID, String messageID, String text) {
        dialogsRef.document(dialogID).collection("messages").document(messageID).update(
                "id", messageID
        );

        dialogsRef.document(dialogID).update(
                "lastMessage", text
                );
    }

    public void removeEventListener(ChildEventListener listenerHandle){
        activeJobRef.removeEventListener(listenerHandle);
    }

    public void updatePoints(User user, Integer points){
        Map<String, Object> updates = new HashMap<>();
        updates.put(user.getUserID() + "/biddlrPoints" , user.getBiddlrPoints());

        userRef.updateChildren(updates);
    }

}

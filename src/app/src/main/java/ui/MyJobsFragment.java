package ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.biddlr.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.ExpandableListAdapter;
import classes.DatabaseManager;
import classes.Job;
import enums.JobStatus;
import interfaces.JobDataListener;

/**
 * Fragment that controls the My Job page
 */
public class MyJobsFragment extends Fragment implements JobDataListener {
    //TODO Change to HashMap
    private ArrayList<Job> inProgressJobs = new ArrayList<>();
    private ArrayList<Bitmap> inProgressPics = new ArrayList<>();
    private ArrayList<Job> postedJobs = new ArrayList<>();
    private ArrayList<Bitmap> postedPics = new ArrayList<>();
    private ArrayList<Job> biddedJobs = new ArrayList<>();
    private ArrayList<Bitmap> biddedPics = new ArrayList<>();
    private ExpandableListAdapter adapter;

    /**
     * Creates a new instance of the My Job page
     * @return A new MyJobsFragment object
     */
    public static MyJobsFragment newInstance() {
        return new MyJobsFragment();
    }

    /**
     * Method called when the MyJobsFragment is created
     * @param savedInstanceState A saved version of a previous MyJobsFragment if it exists
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseManager.shared.setJobsForPosterListener(DatabaseManager.shared.getFirebaseUser().getUid(), 50, this);
    }

    /**
     * Method called when the MyJobsFragment is displayed
     * @param inflater The object used to create a View for the fragment based on the xml file
     * @param container
     * @param savedInstanceState A saved version of a previous MyJobsFragment if it exists
     * @return A View describing a MyJobsFragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_jobs, container, false);

        List<String> headers = new ArrayList<>();
        headers.add("In Progress Jobs");
        headers.add("My Posted Jobs");
        headers.add("My Bidded Jobs");

        HashMap<String, List<Job>> children = new HashMap<>();
        children.put("In Progress Jobs", inProgressJobs);
        children.put("My Posted Jobs", postedJobs);
        children.put("My Bidded Jobs", biddedJobs);

        HashMap<String, List<Bitmap>> pics = new HashMap<>();
        pics.put("In Progress Jobs", inProgressPics);
        pics.put("My Posted Jobs", postedPics);
        pics.put("My Bidded Jobs", biddedPics);

        ExpandableListView listMyJobs = v.findViewById(R.id.listMyJobs);
        adapter = new ExpandableListAdapter(getContext(), headers, children, pics);
        listMyJobs.setAdapter(adapter);
        listMyJobs.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(groupPosition == 1){
                    Fragment bidsFrag = BidderSelectFragment.newInstance(postedJobs.get(childPosition).getJobID());
                    FragmentTransaction trans = getFragmentManager().beginTransaction();
                    trans.add(android.R.id.content, bidsFrag);
                    trans.addToBackStack("JOBS");
                    trans.commit();
                }
                return false;
            }
        });

        return v;
    }

    /**
     * Implements the newDataReceived method of the DataListener interface
     * Adds Job to the postedJobs list when a job is added to the database
     * @param job The Job that has been added
     */
    @Override
    public void newDataReceived(final Job job){
        if(job.getStatus() == JobStatus.IN_PROGRESS){
            inProgressJobs.add(0, job);
            inProgressPics.add(0, null);
            DatabaseManager.shared.getImgRef(job.getJobID()).getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    int index = inProgressJobs.indexOf(job);
                    inProgressPics.set(index, bmp);
                    adapter.notifyDataSetChanged();
                }
            });
        }
        else if(job.getPosterID().equals(DatabaseManager.shared.currentUser.getUserID())){
            postedJobs.add(0, job);
            postedPics.add(0, null);
            DatabaseManager.shared.getImgRef(job.getJobID()).getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    int index = postedJobs.indexOf(job);
                    postedPics.set(index, bmp);
                    adapter.notifyDataSetChanged();
                }
            });
        }
        else{
            biddedJobs.add(0, job);
            biddedPics.add(0, null);
            DatabaseManager.shared.getImgRef(job.getJobID()).getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    int index = biddedJobs.indexOf(job);
                    biddedPics.set(index, bmp);
                    adapter.notifyDataSetChanged();
                }
            });
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void dataRemoved(Job job) { }

    @Override
    public void dataChanged(Job job) { }
}

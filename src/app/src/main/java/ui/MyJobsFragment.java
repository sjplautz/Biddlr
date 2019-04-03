package ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.biddlr.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.ExpandableListAdapter;
import classes.DatabaseManager;
import classes.Job;
import interfaces.JobDataListener;

/**
 * Fragment that controls the My Job page
 */
public class MyJobsFragment extends Fragment implements JobDataListener {
    private ArrayList<Job> postedJobs = new ArrayList<>();
    private ArrayList<Job> biddedJobs = new ArrayList<>();
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
        headers.add("My Posted Jobs");
        headers.add("My Bidded Jobs");

        HashMap<String, List<Job>> children = new HashMap<>();
        children.put("My Posted Jobs", postedJobs);
        children.put("My Bidded Jobs", biddedJobs);

        ExpandableListView listMyJobs = v.findViewById(R.id.listMyJobs);
        adapter = new ExpandableListAdapter(getContext(), headers, children);
        listMyJobs.setAdapter(adapter);
        listMyJobs.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(groupPosition == 0){
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
    public void newDataReceived(Job job){
        if(job.getPosterID().equals(DatabaseManager.shared.currentUser.getUserID())){
            postedJobs.add(0, job);
        }
        else{
            biddedJobs.add(0, job);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void dataRemoved(Job job) { }

    @Override
    public void dataChanged(Job job) { }
}

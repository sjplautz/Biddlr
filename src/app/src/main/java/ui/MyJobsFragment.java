package ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private ArrayList<Job> jobs = new ArrayList<>();
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
        children.put("My Posted Jobs", jobs);

        ExpandableListView listMyJobs = v.findViewById(R.id.listMyJobs);
        adapter = new ExpandableListAdapter(getContext(), headers, children);
        listMyJobs.setAdapter(adapter);

        return v;
    }

    /**
     * Implements the newDataReceived method of the DataListener interface
     * Adds Job to the jobs list when a job is added to the database
     * @param job The Job that has been added
     */
    @Override
    public void newDataReceived(Job job){
        jobs.add(0, job);
        adapter.notifyDataSetChanged();
    }
}

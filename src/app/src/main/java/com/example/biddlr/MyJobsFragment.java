package com.example.biddlr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        headers.add("Posted jobs");
        headers.add("Jobs in bidding");

        HashMap<String, List<Job>> children = new HashMap<>();
        children.put("Posted jobs", jobs);
//        children.put("Jobs in bidding", DatabaseManager.shared.getJobs());

        ExpandableListView listMyJobs = v.findViewById(R.id.listMyJobs);
        adapter = new ExpandableListAdapter(getContext(), headers, children);
        listMyJobs.setAdapter(adapter);
        listMyJobs.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPos, int childPos, long id) {
                Job job = null;
                switch(groupPos){
                    case 0:
                        job = jobs.get(childPos);
                        break;
                    case 1:
//                        job = DatabaseManager.shared.getJobs().get(childPos);
                        break;
                }
                Fragment jobFrag = JobViewFragment.newInstance(job);
                FragmentManager manager = getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.replace(R.id.frameNull, jobFrag);
                trans.addToBackStack(null);
                trans.commit();
                return false;
            }
        });

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

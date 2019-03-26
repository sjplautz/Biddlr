package com.example.biddlr;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import interfaces.DataListener;

public class MyJobsFragment extends Fragment implements DataListener {
    private ArrayList<Job> jobs = new ArrayList<>();
    private JobListAdapter adapter;

    public static MyJobsFragment newInstance() {
        MyJobsFragment fragment = new MyJobsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new JobListAdapter(jobs);
        DatabaseManager.shared.getJobsForPoster(DatabaseManager.shared.getCurrentUser().getUid(), 50, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_jobs, container, false);

        List<String> headers = new ArrayList<>();
        headers.add("Posted jobs");
        headers.add("Jobs in bidding");

        HashMap<String, List<Job>> children = new HashMap<>();
        children.put("Posted jobs", jobs);
        children.put("Jobs in bidding", DatabaseManager.shared.getJobs());

        ExpandableListView listMyJobs = v.findViewById(R.id.listMyJobs);
        ExpandableListAdapter adapter = new ExpandableListAdapter(getContext(), headers, children);
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
                        job = DatabaseManager.shared.getJobs().get(childPos);
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

    @Override
    public void newDataReceived(Job job){
        jobs.add(0, job);
        adapter.notifyDataSetChanged();
    }
}

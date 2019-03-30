package com.example.biddlr;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import classes.DatabaseManager;
import classes.Job;
import interfaces.JobDataListener;

public class ExploreFragment extends Fragment implements JobDataListener {
    private RecyclerView recycler;
    private JobListAdapter adapter;
    private ArrayList<Job> jobs = new ArrayList<>();

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Call the query you want with 'this' as the listener
        DatabaseManager.shared.setActiveJobsListener(50, this);
        adapter = new JobListAdapter(jobs);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_explore, container, false);

        recycler = v.findViewById(R.id.exploreRecycler);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext().getApplicationContext());
        recycler.setLayoutManager(manager);
        recycler.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration div = new DividerItemDecoration(recycler.getContext(), ((LinearLayoutManager) manager).getOrientation());
        recycler.addItemDecoration(div);

        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(new JobListTouchListener(getContext().getApplicationContext(), recycler, new JobListTouchListener.ClickListener() {
            @Override
            public void onClick(View v, int pos) {
                Job job = jobs.get(pos);
                Fragment jobFrag = JobViewFragment.newInstance(job);
                FragmentManager manager = getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.replace(R.id.frameNull, jobFrag);
                trans.addToBackStack(null);
                trans.commit();
            }

            @Override
            public void onLongClick(View v, int pos) {

            }
        }));

        return v;
    }

    @Override
    public void newDataReceived(Job job) {
        jobs.add(0, job);
        adapter.notifyDataSetChanged();
    }
}

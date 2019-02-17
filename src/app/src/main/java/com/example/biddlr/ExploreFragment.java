package com.example.biddlr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import classes.Job;

public class ExploreFragment extends Fragment {
    private List<Job> jobList;
    private RecyclerView recycler;
    private JobListAdapter adapter;

    public static ExploreFragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        jobList = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_explore, container, false);

        adapter = new JobListAdapter(jobList);
        recycler = v.findViewById(R.id.exploreRecycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(manager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(new JobListTouchListener(getContext(), recycler, new JobListTouchListener.ClickListener() {
            @Override
            public void onClick(View v, int pos) {
                Job job = jobList.get(pos);
                Fragment jobFrag = JobViewFragment.newInstance(job);
                FragmentManager manager = getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.replace(R.id.frameNull, jobFrag);
                trans.commit();
            }

            @Override
            public void onLongClick(View v, int pos) {

            }
        }));

        prepareSampleData();
        return v;
    }

    private void prepareSampleData() {
        Job j1 = new Job(0, "This is the first job", "Hello. I posted a job here. Take a look if you please.", 0, "job1", "123 Main St.", LocalDateTime.now().plusHours(1), 20.0);
        jobList.add(j1);

        Job j2 = new Job(0, "This is the second job", null, 0, "job2", null, LocalDateTime.now().plusHours(1), 999.0);
        jobList.add(j2);

        Job j3 = new Job(0, "This is the first job", null, 0, "job1", "Somewhere", LocalDateTime.now().plusHours(1), 20.0);
        jobList.add(j3);

        Job j4 = new Job(0, "This is the second job", null, 0, "job2", "Here", LocalDateTime.now().plusHours(1), 999.0);
        jobList.add(j4);

        Job j5 = new Job(0, "This is the first job", null, 0, "job1", null, LocalDateTime.now().plusHours(1), 20.0);
        jobList.add(j5);

        Job j6 = new Job(0, "This is the second job", null, 0, "job2", "There", LocalDateTime.now().plusHours(1), 999.0);
        jobList.add(j6);

        Job j7 = new Job(0, "This is the first job", null, 0, "job1", null, LocalDateTime.now().plusHours(1), 20.0);
        jobList.add(j7);

        Job j8 = new Job(0, "This is the second job", null, 0, "job2", "Anywhere", LocalDateTime.now().plusHours(1), 999.0);
        jobList.add(j8);

        Job j9 = new Job(0, "This is the first job", null, 0, "job1", null, LocalDateTime.now().plusHours(1), 20.0);
        jobList.add(j9);

        Job j10 = new Job(0, "This is the second job", null, 0, "job2", null, LocalDateTime.now().plusHours(1), 999.0);
        jobList.add(j10);

        adapter.notifyDataSetChanged();
    }
}

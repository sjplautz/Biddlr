package com.example.biddlr;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import classes.DatabaseManager;
import classes.Job;
import enums.JobStatus;

public class ExploreFragment extends Fragment {
    private List<Job> jobList = new ArrayList<>();
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
        jobList = DatabaseManager.shared.getJobs();
        View v = inflater.inflate(R.layout.fragment_explore, container, false);

        adapter = new JobListAdapter(jobList);
        recycler = v.findViewById(R.id.exploreRecycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext().getApplicationContext());
        recycler.setLayoutManager(manager);
        recycler.setItemAnimator(new DefaultItemAnimator());

        //adding a divider between recyclerview list items
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.recycler_divider));
        recycler.addItemDecoration(divider);

        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(new JobListTouchListener(getContext().getApplicationContext(), recycler, new JobListTouchListener.ClickListener() {
            @Override
            public void onClick(View v, int pos) {
                Job job = jobList.get(pos);
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

        //prepareSampleData();
        return v;
    }

    private void prepareSampleData() {
        adapter.notifyDataSetChanged();
    }
}

package com.example.biddlr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import classes.DatabaseManager;
import classes.Job;

public class HomeFragment extends Fragment {
    private List<Job> jobList;
    private RecyclerView recycler;
    private JobListAdapter adapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        adapter = DatabaseManager.shared.getExploreAdapter();

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton btnCreateNewJob = v.findViewById(R.id.btnCreateNewJob);
        btnCreateNewJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).openDialog();
            }
        });

        //Sets the layout for the recycler view to inflate with
        recycler = v.findViewById(R.id.homeRecycler);

        //Set the layout parameters
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(manager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

        //adding a divider between recyclerview list items
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.recycler_divider));
        recycler.addItemDecoration(divider);

        recycler.addOnItemTouchListener(new JobListTouchListener(getContext(), recycler, new JobListTouchListener.ClickListener() {
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

        //add the map fragment to home fragment and inflate it
        Fragment mapFragment = new MapFragment();
        FragmentTransaction trans = getChildFragmentManager().beginTransaction();
        trans.add(R.id.mapView, mapFragment).commit();

        return v;
    }

    /*
    //this will be used to update the map with the appropriate batch of pins when the map
    //fragment is reopened, resulting in a dynamic map
    @Override
    public void onResume(){
        super.onResume();
        handler.post(batchAddMarkersRunnable);
    }
    */
}

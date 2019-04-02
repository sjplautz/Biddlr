package com.example.biddlr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

import classes.DatabaseManager;
import classes.Job;
import classes.User;
import interfaces.JobDataListener;
import interfaces.UserDataListener;


public class BidderSelectFragment extends Fragment implements JobDataListener, UserDataListener {
    private static final String JOB_ID = "job_id";
    private BidderListAdapter adapter;
    private ArrayList<User> users = new ArrayList<>();
    private int selectedIndex = -1;

    public static BidderSelectFragment newInstance(String jobId) {
        Bundle bundle = new Bundle();
        bundle.putString(JOB_ID, jobId);
        BidderSelectFragment bsf = new BidderSelectFragment();
        bsf.setArguments(bundle);
        return bsf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseManager.shared.setJobFromIDListener(getArguments().getString(JOB_ID), this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_bidder_select, container, false);

        final Button btnViewProfile = v.findViewById(R.id.btnView);
        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedIndex != -1){
                    Fragment userFrag = UserProfileFragment.newInstance(users.get(selectedIndex));
                    FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                    trans.add(android.R.id.content, userFrag);
                    trans.commit();
                }
            }
        });

        final Button btnSelect = v.findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final RecyclerView recycler = v.findViewById(R.id.listBidders);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext().getApplicationContext());
        recycler.setLayoutManager(manager);
        recycler.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Pair<String, Double>> bids = new ArrayList<>();
        bids.add(new Pair<>("Test 1", 20.0));
        bids.add(new Pair<>("Test 2", 21.0));
        bids.add(new Pair<>("Test 3", 22.0));
        bids.add(new Pair<>("Test 4", 23.0));
        bids.add(new Pair<>("Test 5", 24.0));
        bids.add(new Pair<>("Test 6", 25.0));

        adapter = new BidderListAdapter(getArguments().getString(JOB_ID), users, null);
        recycler.setAdapter(adapter);

        recycler.addOnItemTouchListener(new BidderListTouchAdapter(getContext().getApplicationContext(), recycler, new BidderListTouchAdapter.ClickListener(){
            @Override
            public void onClick(View v, int pos){
                selectedIndex = pos;
                adapter.setSelectedItem(pos);
                adapter.notifyDataSetChanged();

                btnSelect.setEnabled(true);
                btnViewProfile.setEnabled(true);
            }

            @Override
            public void onLongClick(View v, int pos){

            }
        }));

        return v;
    }

    @Override
    public void newDataReceived(Job job){
        HashMap<String, Double> bids = job.getBids();
        ArrayList<String> uids = new ArrayList<>(bids.keySet());
        DatabaseManager.shared.setUsersFromListListener(uids, this);
    }

    @Override
    public void newDataReceived(User user){
        users.add(user);
        adapter.notifyDataSetChanged();
    }
}

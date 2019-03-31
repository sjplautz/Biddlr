package com.example.biddlr;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class BidderSelectFragment extends Fragment {
    public static BidderSelectFragment newInstance() {
        return new BidderSelectFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bidder_select, container, false);

        RecyclerView recycler = v.findViewById(R.id.listBidders);

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

        BidderListAdapter adapter = new BidderListAdapter(bids, null);
        recycler.setAdapter(adapter);

        return v;
    }
}

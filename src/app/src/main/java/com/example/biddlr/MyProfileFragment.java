package com.example.biddlr;

import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import classes.Job;

public class MyProfileFragment extends Fragment {

    private List<Job> jobList;
    private RecyclerView recycler;
    private Button btnEdit;
    private JobListAdapter adapter;
    private TextView txtEmpty;

    public static MyProfileFragment newInstance() { return new MyProfileFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //need to add method to database manager to get the user's profile
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        jobList = new ArrayList<>();
        adapter = new JobListAdapter(jobList);

        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        //create handle for rating bar and set star count (stars possible) to 5
        RatingBar rating = (RatingBar) v.findViewById(R.id.myRatingBar);
        rating.setNumStars(5);

        //setting the profile image resource to default baseline image
        ImageView imgProfile = (ImageView) v.findViewById(R.id.myProfileImage);
        imgProfile.setImageResource(R.drawable.baseline_person_24);

        //setting the star count in the my rating bar to default 4.5/5 for my profile
        double default_rating = 4.5;
        rating.setRating((float)default_rating);

        //grabbing a handle to recycler view
        recycler = v.findViewById(R.id.myCompletedJobsRecycler);

        //adding a divider between recyclerview list items
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.recycler_divider));
        recycler.addItemDecoration(divider);

        //Set the layout parameters
        final RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
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
                trans.addToBackStack(null);
                trans.commit();
            }

            @Override
            public void onLongClick(View v, int pos) {

            }
        }));

        //getting a handle to the edit button
        btnEdit = v.findViewById(R.id.btnEditProfile);
        //adding a click listener to the button to navigate to profile editing fragment
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment editFrag = EditProfileFragment.newInstance();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.replace(R.id.frameNull, editFrag);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        //grabbing handle to empty text view to sub in if recycler is empty
        txtEmpty = v.findViewById(R.id.myTxtEmpty);

        //switches the visibility to either the recycler or empty message based on whether any jobs completed
        if (jobList.isEmpty()) {
            recycler.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        }
        else {
            recycler.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.GONE);
        }

        return v;
    }

}

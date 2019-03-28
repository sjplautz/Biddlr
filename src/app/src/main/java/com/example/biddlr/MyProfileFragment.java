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
    private TextView bio;

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
        adapter = new JobListAdapter(jobList, null);

        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        //create handle for rating bar and set start to 4/5 default rating
        RatingBar rating = (RatingBar) v.findViewById(R.id.userRatingBar);
        rating.setNumStars(5);

        //setting the profile image resource to default baseline image
        ImageView imgProfile = (ImageView) v.findViewById(R.id.DefaultProfileImage);
        imgProfile.setImageResource(R.drawable.baseline_person_24);

        //setting the star count in the user rating bar
        RatingBar userRating = (RatingBar) v.findViewById(R.id.userRatingBar);
        double default_rating = 4.5;
        userRating.setRating((float)default_rating);

        //grabbing a handle to recycler view
        recycler = v.findViewById(R.id.completedJobsRecycler);

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

        //populate the prefab data for demo
        //prepareSampleData();

        return v;
    }

    private void prepareSampleData() {
//        Job j1 = new Job(0, "This is the first job", null, 0, "job1", "123 Main St.", LocalDateTime.now().plusHours(1), 20.0);
//        jobList.add(j1);
//
//        Job j2 = new Job(0, "This is the second job", null, 0, "job2", null, LocalDateTime.now().plusHours(1), 999.0);
//        jobList.add(j2);
//
//        Job j3 = new Job(0, "This is the first job", null, 0, "job1", "Somewhere", LocalDateTime.now().plusHours(1), 20.0);
//        jobList.add(j3);
//
//        Job j4 = new Job(0, "This is the second job", null, 0, "job2", "Here", LocalDateTime.now().plusHours(1), 999.0);
//        jobList.add(j4);
//
//        Job j5 = new Job(0, "This is the first job", null, 0, "job1", null, LocalDateTime.now().plusHours(1), 20.0);
//        jobList.add(j5);
//
//        Job j6 = new Job(0, "This is the second job", null, 0, "job2", "There", LocalDateTime.now().plusHours(1), 999.0);
//        jobList.add(j6);
//
//        Job j7 = new Job(0, "This is the first job", null, 0, "job1", null, LocalDateTime.now().plusHours(1), 20.0);
//        jobList.add(j7);
//
//        Job j8 = new Job(0, "This is the second job", null, 0, "job2", "Anywhere", LocalDateTime.now().plusHours(1), 999.0);
//        jobList.add(j8);
//
//        Job j9 = new Job(0, "This is the first job", null, 0, "job1", null, LocalDateTime.now().plusHours(1), 20.0);
//        jobList.add(j9);
//
//        Job j10 = new Job(0, "This is the second job", null, 0, "job2", null, LocalDateTime.now().plusHours(1), 999.0);
//        jobList.add(j10);

        adapter.notifyDataSetChanged();
    }
}

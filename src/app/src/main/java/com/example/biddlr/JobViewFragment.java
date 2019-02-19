package com.example.biddlr;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import classes.DatabaseManager;
import classes.Job;
import classes.User;


public class JobViewFragment extends Fragment {
    private static final String JOB_FRAGMENT_KEY = "job_fragment_key";
    private Job job;

    public static JobViewFragment newInstance(Job job) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(JOB_FRAGMENT_KEY, job);
        JobViewFragment fragment = new JobViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        job = getArguments().getParcelable(JOB_FRAGMENT_KEY);
        View v = inflater.inflate(R.layout.fragment_job_view, container, false);

        TextView txtJobTitle = (TextView) v.findViewById(R.id.txtJobTitle);
        txtJobTitle.setText(job.getTitle());

        TextView txtJobDescription = (TextView) v.findViewById(R.id.txtJobDescription);
        txtJobDescription.setText(job.getDescription());

        TextView txtJobLocation = (TextView) v.findViewById(R.id.txtJobLocation);
        txtJobLocation.setText(job.getLocation());

        TextView txtTimeToExpiration = (TextView) v.findViewById(R.id.txtTimeToExpiration);
        txtTimeToExpiration.setText(job.getFormattedDateFromNow());

        TextView txtJobAskingPrice = (TextView) v.findViewById(R.id.txtJobAskingPrice);
        String start = "$" + job.getStartingPrice();
        txtJobAskingPrice.setText(start);

        TextView txtJobCurrentBid = (TextView) v.findViewById(R.id.txtJobCurrentBid);
        String current = "$" + job.getCurrentBid();
        txtJobCurrentBid.setText(current);

        TextView txtPosterName = (TextView) v.findViewById(R.id.txtPosterName);
        User poster = DatabaseManager.shared.getUsers().get(0);
        txtPosterName.setText(poster.getFirstName() + " " + poster.getLastName());

        RatingBar rtgPosterRating = (RatingBar) v.findViewById(R.id.rtgPosterRating);
        rtgPosterRating.setRating(poster.getPosterRating().floatValue());

        ImageView imgProfile = (ImageView) v.findViewById(R.id.imgProfile);
        imgProfile.setImageResource(R.drawable.baseline_person_24);

        ImageView imgJob = (ImageView) v.findViewById(R.id.imgJob);
        imgJob.setImageResource(R.drawable.job1);

        return v;
    }
}

package com.example.biddlr;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.InputStream;
import java.nio.ByteBuffer;

import classes.DatabaseManager;
import classes.Job;
import classes.User;
import interfaces.JobDataListener;
import interfaces.UserDataListener;


public class JobViewFragment extends Fragment implements UserDataListener, JobDataListener {
    private static final String JOB_FRAGMENT_KEY = "job_fragment_key";
    private Job job;
    private TextView txtPosterName;
    private TextView txtJobCurrentBid;

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

        DatabaseManager.shared.setUserFromIDListener(job.getPosterID(), this);
        DatabaseManager.shared.setJobFromIDListener(job.getJobID(), this);

        TextView txtJobTitle = (TextView) v.findViewById(R.id.txtJobTitle);
        txtJobTitle.setText(job.getTitle());

        TextView txtJobDescription = (TextView) v.findViewById(R.id.txtJobDescription);
        txtJobDescription.setText(job.getDescription());

        TextView txtJobLocation = (TextView) v.findViewById(R.id.txtJobLocation);
        txtJobLocation.setText(job.getLocation());

        TextView txtTimeToExpiration = (TextView) v.findViewById(R.id.txtTimeToExpiration);
        txtTimeToExpiration.setText(job.formatDateFromNow());

        TextView txtJobAskingPrice = (TextView) v.findViewById(R.id.txtJobAskingPrice);
        String start = "$" + job.getStartingPrice();
        txtJobAskingPrice.setText(start);

        txtJobCurrentBid = (TextView) v.findViewById(R.id.txtJobCurrentBid);
        String current = "$" + job.getCurrentBid();
        txtJobCurrentBid.setText(current);

        txtPosterName = (TextView) v.findViewById(R.id.txtPosterName);

        RatingBar rtgPosterRating = (RatingBar) v.findViewById(R.id.rtgPosterRating);
        rtgPosterRating.setRating(5);//poster.getPosterRating().floatValue());

        ImageView imgProfile = (ImageView) v.findViewById(R.id.imgProfile);
        imgProfile.setImageResource(R.drawable.baseline_person_24);

        ImageView imgJob = v.findViewById(R.id.imgJob);
        DatabaseManager.shared.setImage(job.getJobID(), imgJob);

        Button btnPlaceBid = v.findViewById(R.id.btnPlaceBid);
        btnPlaceBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobBidDialogFragment bidDialog = JobBidDialogFragment.newInstance(job);
                bidDialog.show(getChildFragmentManager(), null);
            }
        });

        //getting a handle to the view profile button
        Button btnView = v.findViewById(R.id.btnViewProfile);
        //adding a click listener to the button to navigate to profile editing fragment
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment userFrag = UserProfileFragment.newInstance();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.replace(R.id.frameNull, userFrag);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        return v;
    }

    @Override
    public void newDataReceived(User user) {
        // Set profile info
        txtPosterName.setText(user.getFirstName() + " " + user.getLastName());
    }

    @Override
    public void newDataReceived(Job job) {
        // Update current bid
        String current = "$" + job.getCurrentBid();
        txtJobCurrentBid.setText(current);
    }

}

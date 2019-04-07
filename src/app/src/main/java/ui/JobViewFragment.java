package ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biddlr.R;

import classes.DatabaseManager;
import classes.Job;
import classes.User;
import interfaces.JobDataListener;
import interfaces.UserDataListener;

/**
 * Fragment to view details of a specific job
 * Navigated to when user taps a job list item
 */
public class JobViewFragment extends Fragment implements UserDataListener, JobDataListener {
    private static final String JOB_FRAGMENT_KEY = "job_fragment_key";
    private Job job;
    private User poster;

    private TextView txtPosterName;
    private TextView txtJobCurrentBid;
    private RatingBar rtgPosterRating;

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

        // Firebase listeners
        DatabaseManager.shared.setUserFromIDListener(job.getPosterID(), this);
        DatabaseManager.shared.setJobFromIDListener(job.getJobID(), this);

        TextView txtJobTitle = (TextView) v.findViewById(R.id.txtJobTitle);
        txtJobTitle.setText(job.getTitle());

        TextView txtJobDescription = (TextView) v.findViewById(R.id.txtJobDescription);
        txtJobDescription.setText(job.getDescription());

        TextView txtJobLocation = (TextView) v.findViewById(R.id.txtJobLocation);
        txtJobLocation.setText(job.getLocation());

        TextView txtTimeToExpiration = (TextView) v.findViewById(R.id.txtTimeToExpiration);
        txtTimeToExpiration.setText(job.formattedTimeFromNow());

        TextView txtJobAskingPrice = (TextView) v.findViewById(R.id.txtJobAskingPrice);
        String start = "$" + String.format("%.2f", job.getStartingPrice());
        txtJobAskingPrice.setText(start);

        txtJobCurrentBid = (TextView) v.findViewById(R.id.txtJobCurrentBid);
        String current = "$" + String.format("%.2f", job.getCurrentBid());
        txtJobCurrentBid.setText(current);

        txtPosterName = (TextView) v.findViewById(R.id.txtPosterName);

        rtgPosterRating = (RatingBar) v.findViewById(R.id.rtgPosterRating);
        rtgPosterRating.setRating(5);

        ImageView imgProfile = (ImageView) v.findViewById(R.id.imgProfile);
        imgProfile.setImageResource(R.drawable.baseline_person_24);

        ImageView imgJob = v.findViewById(R.id.imgJob);
        DatabaseManager.shared.setJobImage(job.getJobID(), imgJob);

        Button btnPlaceBid = v.findViewById(R.id.btnPlaceBid);

        // If job is expired but hasn't been removed from active_jobs table on database yet, hide the 'Place Bid' button
        if (job.isNowPastExpirationDate()) {
            btnPlaceBid.setVisibility(View.INVISIBLE);
        }

        // Show dialog to place a bid when 'Place Bid' button tapped and current user != poster
        btnPlaceBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (job.getPosterID().equals(DatabaseManager.shared.getFirebaseUser().getUid())) {
                    Toast.makeText(getActivity(), "You cannot bid on a job you posted!",
                            Toast.LENGTH_LONG).show();
                } else {
                    JobBidDialogFragment bidDialog = JobBidDialogFragment.newInstance(job);
                    bidDialog.show(getChildFragmentManager(), null);
                }
            }
        });

        //getting a handle to the view profile button
        Button btnView = v.findViewById(R.id.btnViewProfile);
        //adding a click listener to the button to navigate to profile editing fragment
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment userFrag = UserProfileFragment.newInstance(poster);
                FragmentManager manager = getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.replace(R.id.frameNull, userFrag);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        return v;
    }

    /* User Listener*/
    @Override
    public void newDataReceived(User user) {
        // Set profile info
        poster = user;
        txtPosterName.setText(user.getFirstName() + " " + user.getLastName());
        rtgPosterRating.setRating(user.getPosterRating().floatValue());
    }

    /* Job Listener*/
    @Override
    public void newDataReceived(Job job) {
        // Update current bid
        String current = "$" + String.format("%.2f", job.getCurrentBid());
        txtJobCurrentBid.setText(current);
    }

    @Override
    public void dataRemoved(Job job) { }

    @Override
    public void dataChanged(Job job) {
        // Update current bid
        String current = "$" + String.format("%.2f", job.getCurrentBid());
        txtJobCurrentBid.setText(current);
    }
}

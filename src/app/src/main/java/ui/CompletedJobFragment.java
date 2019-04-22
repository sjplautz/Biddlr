package ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.biddlr.R;

import classes.DatabaseManager;
import classes.Job;
import classes.User;
import enums.JobStatus;
import interfaces.JobDataListener;
import interfaces.UserDataListener;

/**
 * This controls the ui that allows a poster to mark
 * a job as completed
 */
public class CompletedJobFragment extends Fragment implements UserDataListener, JobDataListener {
    private static final String JOB_FRAGMENT_KEY = "job_fragment_key";
    private Job job;
    private User poster;

    private TextView txtPosterName;
    private RatingBar rtgPosterRating;

    /**
     * Method for creating an instance of this controller
     * @param job The job to be viewed
     * @return A CompletedJobFragment object
     */
    public static CompletedJobFragment newInstance(Job job) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(JOB_FRAGMENT_KEY, job);
        CompletedJobFragment fragment = new CompletedJobFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Sets the required listeners to get the relevant users
     * @param savedInstanceState The saved state of the Fragment if it exists
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the View for the ui that this controller manages
     * @param inflater Used to inflate the xml file
     * @param container
     * @param savedInstanceState The saved state of this fragment if it exists
     * @return A View describing the ui element
     */
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        job = getArguments().getParcelable(JOB_FRAGMENT_KEY);
        poster = new User();

        View v = inflater.inflate(R.layout.fragment_job_completion, container, false);

        // Firebase listeners
        DatabaseManager.shared.setUserFromIDListener(job.getPosterID(), this);
        DatabaseManager.shared.setJobFromIDListener(job.getJobID(), this);

        txtPosterName = v.findViewById(R.id.completed_txtPosterName);
        rtgPosterRating = v.findViewById(R.id.completed_rtgPosterRating);

        TextView txtJobTitle = v.findViewById(R.id.completed_txtJobTitle);
        txtJobTitle.setText(job.getTitle());

        TextView txtJobDescription = v.findViewById(R.id.completed_txtJobDescription);
        txtJobDescription.setText(job.getDescription());

        TextView txtJobLocation = v.findViewById(R.id.completed_txtJobLocation);
        txtJobLocation.setText(job.getLocation());

        TextView txtTimeToExpiration = v.findViewById(R.id.completed_txtTimeToExpiration);
        txtTimeToExpiration.setText(job.formattedTimeFromNow());

        TextView txtJobFinalPrice = v.findViewById(R.id.completed_txtJobFinalPrice);
        String price = "$" + String.format("%.2f", job.getCurrentBid());
        txtJobFinalPrice.setText(price);

        ImageView imgProfile = v.findViewById(R.id.completed_imgProfile);
        DatabaseManager.shared.setUserImage(job.getPosterID(), imgProfile);

        ImageView imgJob = v.findViewById(R.id.completed_imgJob);
        DatabaseManager.shared.setJobImage(job.getJobID(), imgJob);

        return v;
    }

    /* User Listener*/
    @Override
    public void newDataReceived(User user) {
        // Set profile info
        poster = user;
        txtPosterName.setText(user.getFirstName() + " " + user.getLastName());
        rtgPosterRating.setRating((float)user.getAvgBidderRating());
    }

    /* Job Listener*/
    @Override
    public void newDataReceived(Job job) {
    }

    @Override
    public void dataChanged(Job job) {

    }

    @Override
    public void dataRemoved(Job job) {

    }

}
package ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Rating;
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
import interfaces.UserDataListener;

/**
 * This controls the ui that allows a poster to mark
 * a job as completed
 */
public class JobCompletionFragment extends Fragment implements UserDataListener {
    private static final String JOB_FRAGMENT_KEY = "job_fragment_key";

    private Job job;

    private User bidder;

    private TextView txtPosterName;
    private RatingBar rtgPosterRating;

    /**
     * Method for creating an instance of this controller
     * @param job The job to potentially mark as completed
     * @return A JobCompletionFragment object
     */
    public static JobCompletionFragment newInstance(Job job) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(JOB_FRAGMENT_KEY, job);
        JobCompletionFragment fragment = new JobCompletionFragment();
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

        job = getArguments().getParcelable(JOB_FRAGMENT_KEY);
        DatabaseManager.shared.setUserFromIDListener(job.getPosterID(), this);
        DatabaseManager.shared.setUserFromIDListener(job.getSelectedBidderID(), this);
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
        View v = inflater.inflate(R.layout.fragment_job_completion, container, false);

        txtPosterName = v.findViewById(R.id.txtPosterName);
        rtgPosterRating = v.findViewById(R.id.rtgPosterRating);

        TextView txtJobTitle = v.findViewById(R.id.txtJobTitle);
        txtJobTitle.setText(job.getTitle());

        txtPosterName = v.findViewById(R.id.txtPosterName);

        TextView txtJobDescription = v.findViewById(R.id.txtJobDescription);
        txtJobDescription.setText(job.getDescription());

        TextView txtJobLocation = v.findViewById(R.id.txtJobLocation);
        txtJobLocation.setText(job.getLocation());

        TextView txtTimeToExpiration = v.findViewById(R.id.txtTimeToExpiration);
        txtTimeToExpiration.setText(job.formattedTimeFromNow());

        TextView txtJobFinalPrice = v.findViewById(R.id.txtJobFinalPrice);
        String price = "$" + String.format("%.2f", job.getCurrentBid());
        txtJobFinalPrice.setText(price);

        ImageView imgProfile = v.findViewById(R.id.imgProfile);
        DatabaseManager.shared.setUserImage(job.getPosterID(), imgProfile);

        ImageView imgJob = v.findViewById(R.id.imgJob);
        DatabaseManager.shared.setJobImage(job.getJobID(), imgJob);

        Button btnComplete = v.findViewById(R.id.btnComplete);
        if(!DatabaseManager.shared.currentUser.getId().equals(job.getPosterID())){
            btnComplete.setEnabled(false);
        }

        // Show dialog to place a bid when 'Place Bid' button tapped and current user != poster
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.bidder_rating, null);
                final RatingBar rtgRateBidder = view.findViewById(R.id.rtgRateBidder);

                //get the amount of points to pay to bidder
                Double bidAmount = job.getCurrentBid();
                Double adjustedAmount = 100 * bidAmount;
                Integer paymentAmount = adjustedAmount.intValue();

                //add positive amount of points to bidder account
                bidder.updateBiddlrPoints(paymentAmount);

                builder.setView(view);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bidder.addBidderRating((double)rtgRateBidder.getRating());
                        bidder.setJobsCompleted(bidder.getJobsCompleted() + 1);

                        job.setCurrentRating((double) rtgRateBidder.getRating());
                        job.setStatus(JobStatus.COMPLETED);
                        DatabaseManager.shared.markJobCompleted(job);
                        DatabaseManager.shared.updateUser(bidder, null);

                        FragmentTransaction trans = getFragmentManager().beginTransaction();
                        trans.replace(R.id.frameNull, JobsFragment.newInstance(1));
                        trans.commit();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });

        return v;
    }

    /**
     * Gets the users relevant to this Fragment
     * @param user The user returned from the database query
     */
    @Override
    public void newDataReceived(User user) {
        if(user.getId().equals(job.getPosterID())) {
            txtPosterName.setText(user.getName());
            rtgPosterRating.setRating(user.getPosterRating().floatValue());
        }
        else if(user.getId().equals(job.getSelectedBidderID())){
            bidder = user;
        }
    }
}
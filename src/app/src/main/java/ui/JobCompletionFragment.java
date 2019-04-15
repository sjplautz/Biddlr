package ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

public class JobCompletionFragment extends Fragment implements UserDataListener {
    private static final String JOB_FRAGMENT_KEY = "job_fragment_key";

    private Job job;

    private TextView txtPosterName;
    private RatingBar rtgPosterRating;

    public static JobCompletionFragment newInstance(Job job) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(JOB_FRAGMENT_KEY, job);
        JobCompletionFragment fragment = new JobCompletionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        job = getArguments().getParcelable(JOB_FRAGMENT_KEY);
        DatabaseManager.shared.setUserFromIDListener(job.getPosterID(), this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
                job.setStatus(JobStatus.COMPLETED);
                DatabaseManager.shared.markJobCompleted(job);

                getFragmentManager().popBackStack();
            }
        });

        return v;
    }

    @Override
    public void newDataReceived(User user) {
        txtPosterName.setText(user.getName());
        rtgPosterRating.setRating(user.getPosterRating().floatValue());
    }
}
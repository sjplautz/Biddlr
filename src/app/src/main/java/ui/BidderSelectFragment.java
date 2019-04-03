package ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.biddlr.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import adapters.BidderListAdapter;
import adapters.ListTouchListener;
import classes.DatabaseManager;
import classes.Job;
import classes.User;
import interfaces.JobDataListener;
import interfaces.UserDataListener;

/**
 * This controls the ui that allows the job poster to select a bidder
 * to do this job
 */
public class BidderSelectFragment extends Fragment implements JobDataListener, UserDataListener {
    private static final String JOB_ID = "job_id";
    private BidderListAdapter adapter;
    private ArrayList<User> users = new ArrayList<>();
    private int selectedIndex = -1;

    private Job job = null;
    private TextView txtLowBid;

    /**
     * Method for creating an instance of this controller
     * @param jobId The ID of the job that is being observed
     * @return An instance of this Fragment
     */
    public static BidderSelectFragment newInstance(String jobId) {
        Bundle bundle = new Bundle();
        bundle.putString(JOB_ID, jobId);
        BidderSelectFragment bsf = new BidderSelectFragment();
        bsf.setArguments(bundle);
        return bsf;
    }

    /**
     * Gets the job to search for bidders from
     * @param savedInstanceState Describes a saved version of this instance if one exists
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseManager.shared.setJobFromIDListener(getArguments().getString(JOB_ID), this);
    }

    /**
     * Creates the view for the ui that this controller manages
     * @param inflater Used to inflate the xml file
     * @param container
     * @param savedInstanceState Describes a saved version of this instance if one exists
     * @return A view describing this ui element
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bidder_select, container, false);

        txtLowBid = v.findViewById(R.id.txtUserBid);
        if(job != null){
            txtLowBid.setText(String.format(Locale.ENGLISH, "$%.02f", job.getCurrentBid()));
        }

        //Displays a bidder's profile for the job poster
        final Button btnViewProfile = v.findViewById(R.id.btnView);
        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedIndex != -1){
                    Fragment userFrag = UserProfileFragment.newInstance(users.get(selectedIndex));
                    FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                    trans.add(android.R.id.content, userFrag);
                    trans.addToBackStack("PROFILE");
                    trans.commit();
                }
            }
        });

        //Picks a bidder for the job
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

        adapter = new BidderListAdapter(getArguments().getString(JOB_ID), users, null);
        recycler.setAdapter(adapter);

        recycler.addOnItemTouchListener(new ListTouchListener(getContext().getApplicationContext(), recycler, new ListTouchListener.ClickListener(){
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

    /**
     * Gets the job that is being managed
     * @param job The job that is being managed
     */
    @Override
    public void newDataReceived(Job job){
        if(txtLowBid != null){
            txtLowBid.setText(String.format(Locale.ENGLISH, "$%.02f", job.getCurrentBid()));
        }

        this.job = job;

        HashMap<String, Double> bids = job.getBids();
        if(bids != null) {
            ArrayList<String> uids = new ArrayList<>(bids.keySet());
            DatabaseManager.shared.setUsersFromListListener(uids, this);
        }
    }

    @Override
    public void dataRemoved(Job job) { }

    @Override
    public void dataChanged(Job job) { }

    /**
     * Gets all of the bidders that have bidded on this job
     * @param user A user that has bidded on this job
     */
    @Override
    public void newDataReceived(User user){
        users.add(user);
        adapter.notifyDataSetChanged();
    }
}

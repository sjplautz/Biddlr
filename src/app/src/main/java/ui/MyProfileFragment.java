package ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.biddlr.R;

import java.util.ArrayList;
import java.util.List;

import adapters.JobListAdapter;
import adapters.ListTouchListener;
import classes.DatabaseManager;
import classes.Job;
import classes.User;

//The fragment that inflates when a user views their own profile
public class MyProfileFragment extends Fragment {

    private List<Job> jobList;
    private RecyclerView recycler;
    private Button btnEdit;
    private JobListAdapter adapter;
    private TextView txtEmpty;

    private User currUser;

    public static MyProfileFragment newInstance() { return new MyProfileFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currUser = DatabaseManager.shared.currentUser;
        jobList = new ArrayList<>();
        adapter = new JobListAdapter(jobList, null);

        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);

        TextView txtUserName = v.findViewById(R.id.myProfileName);
        txtUserName.setText(currUser.getName());

        //create handle for rating bar and set star count (stars possible) to 5
        RatingBar rating = v.findViewById(R.id.myRatingBar);
        rating.setRating((float)currUser.getAvgBidderRating());

        //create handle to user's point balance and set text view to the balance
        TextView txtPointBalance = v.findViewById(R.id.BiddlrPointsBalanceText);
        //setting user's point balance here does not persist currently
        String pointsString = String.valueOf(currUser.getBiddlrPoints());
        txtPointBalance.setText(pointsString);

        //setting the profile image resource to default baseline image
        ImageView imgProfile = v.findViewById(R.id.myProfileImage);
        DatabaseManager.shared.setUserImage(currUser.getId(), imgProfile);

        TextView txtUserBio = v.findViewById(R.id.myProfileBio);
        if(currUser.getBio() != null){
            txtUserBio.setText(currUser.getBio());
        }
        else{
            txtUserBio.setText("You have not yet created a bio. Please enter a description of yourself");
        }

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

        recycler.addOnItemTouchListener(new ListTouchListener(getContext(), recycler, new ListTouchListener.ClickListener() {
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

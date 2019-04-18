package ui;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.Toast;

import com.example.biddlr.R;

import java.util.ArrayList;
import java.util.List;

import adapters.JobListAdapter;
import adapters.ListTouchListener;
import classes.DatabaseManager;
import classes.Job;
import classes.User;

import static classes.DatabaseManager.*;

//The Fragment that inflates when viewing another User's profile
public class UserProfileFragment extends Fragment {
    private final static String USER = "user";
    private List<Job> jobList;
    private RecyclerView recycler;
    private Button btnContact;
    private JobListAdapter adapter;
    private TextView txtEmpty;

    public static UserProfileFragment newInstance(User user) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(USER, user);
        UserProfileFragment upf = new UserProfileFragment();
        upf.setArguments(bundle);
        return upf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //need to add method to database manager to get the user's profile
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final User user = getArguments().getParcelable(USER);
        jobList = new ArrayList<>();
        adapter = new JobListAdapter(jobList, null);

        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);

        TextView txtName = v.findViewById(R.id.userProfileName);
        txtName.setText(user.getFirstName() + " " + user.getLastName());

        //create handle for rating bar and set star ount (stars possible) to 5
        RatingBar rating = v.findViewById(R.id.userRatingBar);
        rating.setRating(user.getBidderRating().floatValue());

        //setting the profile image resource to default baseline image
        ImageView imgProfile = v.findViewById(R.id.userProfileImage);
        DatabaseManager.shared.setUserImage(user.getId(), imgProfile);

        TextView txtUserBio = v.findViewById(R.id.userProfileBio);
        if(user.getBio() != null){
            txtUserBio.setText(user.getBio());
        }
        else{
            txtUserBio.setText("This user has not yet set up a bio.");
        }

        //grabbing a handle to recycler view
        recycler = v.findViewById(R.id.userCompletedJobsRecycler);

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
        btnContact = v.findViewById(R.id.btnContact);
        //adding a click listener to the button to navigate to profile editing fragment
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getId().equals(DatabaseManager.shared.currentUser.getUserID())) {
                    Toast.makeText(getActivity(), "You cannot contact yourself!", Toast.LENGTH_LONG).show();
                    return;
                }
                ArrayList<User> users = new ArrayList<>();
                users.add(DatabaseManager.shared.currentUser);
                users.add(user);
                shared.addNewDialogForUsers(users);

                BottomNavigationView nav = getActivity().findViewById(R.id.menuNav);
                nav.setSelectedItemId(R.id.itemMessages);

//                Fragment editFrag = AllMessagesFragment.newInstance();
//                FragmentManager manager = getFragmentManager();
//                FragmentTransaction trans = manager.beginTransaction();
//                trans.replace(R.id.frameNull, editFrag);
//                trans.addToBackStack(null);
//                trans.commit();
            }
        });

        //grabbing handle to empty text view to sub in if recycler is empty
        txtEmpty = v.findViewById(R.id.userTxtEmpty);

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

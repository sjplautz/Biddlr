package ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.biddlr.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.core.view.filter.ChildChangeAccumulator;

import java.util.ArrayList;

import adapters.JobListAdapter;
import adapters.ListTouchListener;
import classes.DatabaseManager;
import classes.Job;
import classes.LatLngWrapped;
import interfaces.JobDataListener;

/**
 * Controller for the job exploration ui
 */
public class ExploreFragment extends Fragment implements JobDataListener {
    private JobListAdapter adapter;
    private ArrayList<Bitmap> pics = new ArrayList<>();
    private ArrayList<Job> jobs = new ArrayList<>();

    private ChildEventListener eventListener = null;
    private JobDataListener listener = this;

    private String filter = "";
    private double radius = -1;

    /**
     * Creates a new instance of the ExploreFragment
     * @return A new instance of the ExploreFragment
     */
    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    /**
     * Sets the listener to add jobs to the view as they are created
     * @param savedInstanceState Describes a saved version of this instance if one exists
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Call the query you want with 'this' as the listener
        //DatabaseManager.shared.setActiveJobsListener(50, this);
        eventListener = DatabaseManager.shared.setJobsFromSearchListener(new LatLngWrapped(MapFragment.mCurrentLocation.getLatitude(), MapFragment.mCurrentLocation.getLongitude()), filter, radius, this);
        adapter = new JobListAdapter(jobs, pics);
    }

    /**
     * Generates the ui from the xml file
     * @param inflater inflates the xml file
     * @param container
     * @param savedInstanceState Describes a saved version of this instance if one exists
     * @return A View decribing the ui for this Fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_explore, container, false);

        final EditText txtSearch = v.findViewById(R.id.txtSearch);
        ImageButton btnActiveSearch = v.findViewById(R.id.btnActiveSearch);
        btnActiveSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                DatabaseManager.shared.removeEventListener(eventListener);
                jobs.clear();
                pics.clear();
                filter = txtSearch.getText().toString();
                eventListener = DatabaseManager.shared.setJobsFromSearchListener(new LatLngWrapped(MapFragment.mCurrentLocation.getLatitude(), MapFragment.mCurrentLocation.getLongitude()), filter, radius, listener);
            }
        });

        ImageButton btnFilter = v.findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu dropdown = new PopupMenu(getContext(), v);
                dropdown.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String title = item.getTitle().toString();
                        switch(title) {
                            case "Within 1 mile":
                                radius = 1.0;
                                break;
                            case "Within 5 miles":
                                radius = 5.0;
                                break;
                            case "Within 10 miles":
                                radius = 10.0;
                                break;
                            case "Within 25 miles":
                                radius = 25.0;
                                break;
                            case "No radius":
                                radius = -1;
                                break;
                        }
                        Log.d("DISTANCE_SELECTED", "Distance: " + radius + " Id: " + item.getItemId());

                        DatabaseManager.shared.removeEventListener(eventListener);
                        jobs.clear();
                        pics.clear();
                        eventListener = DatabaseManager.shared.setJobsFromSearchListener(new LatLngWrapped(MapFragment.mCurrentLocation.getLatitude(), MapFragment.mCurrentLocation.getLongitude()), filter, radius, listener);

                        return false;
                    }
                });

                dropdown.getMenu().add("Within 1 mile");
                dropdown.getMenu().add("Within 5 miles");
                dropdown.getMenu().add("Within 10 miles");
                dropdown.getMenu().add("Within 25 miles");
                dropdown.getMenu().add("No radius");
                dropdown.show();
            }
        });

        RecyclerView recycler = v.findViewById(R.id.exploreRecycler);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext().getApplicationContext());
        recycler.setLayoutManager(manager);
        recycler.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration div = new DividerItemDecoration(recycler.getContext(), ((LinearLayoutManager) manager).getOrientation());
        recycler.addItemDecoration(div);

        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(new ListTouchListener(getContext().getApplicationContext(), recycler, new ListTouchListener.ClickListener() {
            @Override
            public void onClick(View v, int pos) {
                Job job = jobs.get(pos);
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

        return v;
    }

    /**
     * Adds jobs to the job list
     * @param job New job to be added
     */
    @Override
    public void newDataReceived(final Job job) {
        jobs.add(0, job);
        pics.add(0, null);
        DatabaseManager.shared.getImgRef(job.getJobID()).getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                int index = jobs.indexOf(job);
                pics.set(index, bmp);
                adapter.notifyDataSetChanged();
            }
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    public void dataRemoved(Job job) {
        int index = jobs.indexOf(job);
        jobs.remove(index);
        pics.remove(index);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void dataChanged(Job job) {
        int index = jobs.indexOf(job);
        jobs.set(index, job);
        adapter.notifyDataSetChanged();
    }
}

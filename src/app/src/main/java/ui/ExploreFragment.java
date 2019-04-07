package ui;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.biddlr.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.core.view.filter.ChildChangeAccumulator;

import java.util.ArrayList;

import adapters.JobListAdapter;
import adapters.ListTouchListener;
import classes.DatabaseManager;
import classes.Job;
import interfaces.JobDataListener;

/**
 * Controller for the job exploration ui
 */
public class ExploreFragment extends Fragment implements JobDataListener {
    private JobListAdapter adapter;
    private ArrayList<Bitmap> pics = new ArrayList<>();
    private ArrayList<Job> jobs = new ArrayList<>();

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
        DatabaseManager.shared.setActiveJobsListener(50, this);
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

package ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

import com.example.biddlr.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.function.Predicate;

import adapters.JobListAdapter;
import adapters.ListTouchListener;
import classes.DatabaseManager;
import classes.Job;
import interfaces.JobDataListener;

public class HomeFragment extends Fragment implements JobDataListener {
    private JobListAdapter adapter;
    private ArrayList<Job> jobs = new ArrayList<>();
    private ArrayList<Bitmap> pics = new ArrayList<>();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseManager.shared.setActiveJobsListener(50, this);
        adapter = new JobListAdapter(jobs, pics);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton btnCreateNewJob = v.findViewById(R.id.btnCreateNewJob);
        btnCreateNewJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).openJobCreationDialog();
            }
        });

        //Sets the layout for the recycler view to inflate with
        RecyclerView recycler = v.findViewById(R.id.homeRecycler);

        //Set the layout parameters
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(manager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

        //adding a divider between recyclerview list items
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.recycler_divider));
        recycler.addItemDecoration(divider);

        recycler.addOnItemTouchListener(new ListTouchListener(getContext(), recycler, new ListTouchListener.ClickListener() {
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

        //add the map fragment to home fragment and inflate it
        Fragment mapFragment = new MapFragment();
        FragmentTransaction trans = getChildFragmentManager().beginTransaction();
        trans.add(R.id.mapView, mapFragment).commit();

        return v;
    }

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
}

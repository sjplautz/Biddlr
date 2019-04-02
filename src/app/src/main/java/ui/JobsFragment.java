package ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.biddlr.R;

import adapters.JobsPagerAdapter;

/**
 * Controller that handles the Fragment related to jobs
 */
public class JobsFragment extends Fragment {
    public static JobsFragment newInstance() {
        return new JobsFragment();
    }

    /**
     * Called when Fragment is created
     * @param savedInstanceState Describes a saved version of this instance if one exists
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates a view based on the xml file for this Fragment
     * @param inflater Inflates the xml file
     * @param container
     * @param savedInstanceState Describes a saved version of this instance if one exists
     * @return A View describing this ui
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_jobs, container, false);

        TabLayout tabsJobs = v.findViewById(R.id.tabsJobs);
        ViewPager pagerJobs = v.findViewById(R.id.pagerJobs);
        FloatingActionButton btnCreateNewJobs = v.findViewById(R.id.btnCreateNewJob);

        JobsPagerAdapter adapter = new JobsPagerAdapter(getFragmentManager());
        pagerJobs.setAdapter(adapter);

        tabsJobs.setupWithViewPager(pagerJobs);

        btnCreateNewJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).openJobCreationDialog();
            }
        });
        return v;
    }
}

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

public class JobsFragment extends Fragment {
    public static JobsFragment newInstance() {
        JobsFragment fragment = new JobsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
                ((HomeActivity)getActivity()).openDialog();
            }
        });
        return v;
    }
}

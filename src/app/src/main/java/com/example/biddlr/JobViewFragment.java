package com.example.biddlr;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import classes.Job;


public class JobViewFragment extends Fragment {
    private static final String JOB_FRAGMENT_KEY = "job_fragment_key";
    private Job job;

    public static JobViewFragment newInstance() {
        JobViewFragment fragment = new JobViewFragment();
        return fragment;
    }

    public static JobViewFragment newInstance(Job job) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(JOB_FRAGMENT_KEY, job);
        JobViewFragment fragment = new JobViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        job = getArguments().getParcelable(JOB_FRAGMENT_KEY);
        View v = inflater.inflate(R.layout.fragment_job_view, container, false);
        TextView txtJobTitle = (TextView) v.findViewById(R.id.txtJobTitle);
        txtJobTitle.setText(job.getTitle());

        return v;
    }
}

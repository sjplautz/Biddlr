package com.example.biddlr;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;

import classes.Job;

public class ProfileFragment extends Fragment {
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        Button btnJobView = (Button) v.findViewById(R.id.btnJobView);
        btnJobView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showJobView();
            }
        });

        return v;
    }

    private void showJobView() {
        LocalDateTime date = LocalDateTime.of(2019,2,20,8,0);
        Job tempJob = new Job(0, "Wash Windows", "I need someone to wash the windows outside my building.",
                1, "thisisaphotoURL", "123 Main Stree Tuscaloosa, AL", date, 20.0);
        Fragment jobViewFrag = JobViewFragment.newInstance(tempJob);
        FragmentManager fragManager = getFragmentManager();
        FragmentTransaction transaction = fragManager.beginTransaction();
        transaction.replace(R.id.frameNull, jobViewFrag);
        transaction.commit();
    }
}

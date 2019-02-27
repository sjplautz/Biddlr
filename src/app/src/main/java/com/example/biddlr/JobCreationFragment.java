package com.example.biddlr;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class JobCreationFragment extends DialogFragment {
    public static JobCreationFragment newInstance() {
        JobCreationFragment fragment = new JobCreationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_job_creation, container, false);

        ImageButton btnDate = v.findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker dp = new DatePicker();
                dp.show(getChildFragmentManager(), null);
            }
        });

        ImageButton btnTime = v.findViewById(R.id.btnTime);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker tp = new TimePicker();
                tp.show(getChildFragmentManager(), null);
            }
        });

        return v;
    }
}

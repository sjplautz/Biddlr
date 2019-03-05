package com.example.biddlr;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//TODO Move submission code here
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

        LocalDateTime now = LocalDateTime.now();

        String date = now.format(DateTimeFormatter.ofPattern("MM/dd/uuuu"));
        TextView txtDate = v.findViewById(R.id.txtDate);
        txtDate.setText(date);

        String time = now.format(DateTimeFormatter.ofPattern("hh:mm a"));
        TextView txtTime = v.findViewById(R.id.txtTime);
        txtTime.setText(time);

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

package com.example.biddlr;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.time.LocalDateTime;

import classes.DatabaseManager;
import classes.Job;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView nav;
    ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bar = getSupportActionBar();

        // Set up dummy database
        DatabaseManager.shared.setUp();


        nav = findViewById(R.id.menuNav);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFrag = null;
                String tag = null;
                switch(item.getItemId()){
                    case R.id.itemHome:
                        selectedFrag = HomeFragment.newInstance();
                        tag = "HOME";
                        break;
                    case R.id.itemExplore:
                        selectedFrag = ExploreFragment.newInstance();
                        tag = "EXPLORE";
                        break;
                    case R.id.itemMyJobs:
                        selectedFrag = MyJobsFragment.newInstance();
                        tag = "MYJOBS";
                        break;
                    case R.id.itemProfile:
                        selectedFrag = ProfileFragment.newInstance();
                        tag = "PROFILE";
                        break;
                    case R.id.itemMessages:
                        selectedFrag = ProfileFragment.newInstance();
                        tag = "MESSAGES";
                        break;
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameNull, selectedFrag);
                transaction.addToBackStack(tag);
                transaction.commit();
                return true;
            }
        });

        //TODO Make a image picker
        FloatingActionButton btnNewJob = findViewById(R.id.btnNewJob);
        btnNewJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.frameNull, HomeFragment.newInstance());
        trans.commit();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    private void openDialog(){
        Fragment frag = JobCreationFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(android.R.id.content, frag, "DIALOG");
        transaction.addToBackStack(null);
        transaction.commit();

        bar.setCustomView(R.layout.dialog_action_bar);
        View barView = bar.getCustomView();

        ImageButton btnClose = barView.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        Button btnSubmit = barView.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            //TODO Make an error dialog appear when required fields are unfilled
            @Override
            public void onClick(View v) {
                submitDialog();
            }
        });

        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    private void closeDialog(){
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("DIALOG")).commit();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
    }

    private void submitDialog(){
        TextView txtTitle = findViewById(R.id.txtTitle);
        String title = txtTitle.getText().toString();
        if(title.trim().isEmpty()) return;

        TextView txtDesc = findViewById(R.id.txtDescription);
        String desc = txtDesc.getText().toString();

        TextView txtLocation = findViewById(R.id.txtLocation);
        String location = txtLocation.getText().toString();

        //TODO Implement a DatePicker and a TimePicker for these two fields
//                        TextView txtDate = findViewById(R.id.txtDate);
//                        TextView txtTime = findViewById(R.id.txtTime);

        TextView txtStartingPrice = findViewById(R.id.txtStartPrice);
        String startingPriceText = txtStartingPrice.getText().toString();
        if(startingPriceText.trim().isEmpty()) return;
        double startingPrice = Double.parseDouble(startingPriceText);


        Job job = new Job(0, title, desc, 0, "job1", location, LocalDateTime.now().plusHours(2), startingPrice);
        DatabaseManager.shared.addJob(job);

        closeDialog();
    }
}

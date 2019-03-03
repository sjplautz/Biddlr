package com.example.biddlr;

import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
                        selectedFrag = JobsFragment.newInstance();
                        tag = "EXPLORE";
                        break;
                    case R.id.itemProfile:
                        selectedFrag = ProfileFragment.newInstance();
                        tag = "PROFILE";
                        break;
                    case R.id.itemMessages:
                        selectedFrag = MessagesFragment.newInstance();
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

        TextView txtDate = findViewById(R.id.txtDate);
        TextView txtTime = findViewById(R.id.txtTime);

        String date[] = txtDate.getText().toString().split("/");
        String time[] = txtTime.getText().toString().split("[:, ]");

        int year = Integer.parseInt(date[2]);
        int month = Integer.parseInt(date[0]);
        int day = Integer.parseInt(date[1]);
        int hour = Integer.parseInt(time[0]);
        hour = time[2].equals("PM") ? 24 - hour : 12 - hour;
        int min = Integer.parseInt(time[1]);

        LocalDateTime expDate= LocalDateTime.of(year, month, day, hour, min);

        System.out.println(expDate.toString());

        TextView txtDesc = findViewById(R.id.txtDescription);
        String desc = txtDesc.getText().toString();

        TextView txtLocation = findViewById(R.id.txtLocation);
        String location = txtLocation.getText().toString();

        TextView txtStartingPrice = findViewById(R.id.txtStartPrice);
        String startingPriceText = txtStartingPrice.getText().toString();
        if(startingPriceText.trim().isEmpty()) return;
        double startingPrice = Double.parseDouble(startingPriceText);


        Job job = new Job(title, desc, 0, "job1", location, LocalDateTime.now().plusHours(2), startingPrice);
        DatabaseManager.shared.addJob(job);

        closeDialog();
    }

    // When user clicks 3 dots
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    // When user selects item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.logout:
                DatabaseManager.shared.mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                Toast.makeText(this, "You are signed out!", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

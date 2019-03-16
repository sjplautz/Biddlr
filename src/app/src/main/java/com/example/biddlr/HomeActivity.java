package com.example.biddlr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import classes.DatabaseManager;
import classes.Job;
import classes.LatLngWrapped;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView nav;
    ActionBar bar;
    public static LatLngWrapped coordinates;
    public static int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bar = getSupportActionBar();

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
                    case R.id.itemJobs:
                        selectedFrag = JobsFragment.newInstance();
                        tag = "JOBS";
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

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.frameNull, HomeFragment.newInstance());
        trans.commit();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    public void openDialog(){
        Fragment frag = JobCreationFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(android.R.id.content, frag, "DIALOG");
        transaction.addToBackStack("DIALOG");
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

        //need to add logic to submit dialog to ensure that the address has been verified before submitting the job
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

    public int locationVerification(String location){
        return 0;
    };

    private void submitDialog(){
        //add in logic for flag value on submission
        if(flag > 0){
            ImageButton btnImagePicker = findViewById(R.id.btnImagePicker);
            Bitmap image = ((BitmapDrawable) btnImagePicker.getDrawable()).getBitmap();

            int size = image.getRowBytes() * image.getHeight();
            ByteBuffer buffer = ByteBuffer.allocate(size);
            image.copyPixelsToBuffer(buffer);
            byte[] imgArr = buffer.array();

            TextView txtTitle = findViewById(R.id.txtTitle);
            String title = txtTitle.getText().toString();
            if(title.trim().isEmpty()) return;

            TextView txtDate = findViewById(R.id.txtDate);
            TextView txtTime = findViewById(R.id.txtTime);

            String date = txtDate.getText().toString();
            String time = txtTime.getText().toString();

            LocalDate lDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/uuuu"));
            LocalTime lTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a"));
            LocalDateTime expDate = LocalDateTime.of(lDate, lTime);

            TextView txtDesc = findViewById(R.id.txtDescription);
            String desc = txtDesc.getText().toString();

            TextView txtLocation = findViewById(R.id.txtLocation);
            String location = txtLocation.getText().toString();

            TextView txtStartingPrice = findViewById(R.id.txtStartPrice);
            String startingPriceText = txtStartingPrice.getText().toString();
            if(startingPriceText.trim().isEmpty()) return;
            double startingPrice = Double.parseDouble(startingPriceText);

            //job constructor modified to include coordinates
            Job job = new Job(title, desc,"job1", location, coordinates, expDate, startingPrice);
            DatabaseManager.shared.addJob(job);

            closeDialog();
        }
        else{
            Toast.makeText(this, "please enter and check a valid address before submission",
                    Toast.LENGTH_LONG).show();
        }

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

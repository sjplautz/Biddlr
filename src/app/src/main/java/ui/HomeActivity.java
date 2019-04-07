package ui;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biddlr.R;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import classes.DatabaseManager;
import classes.Job;
import classes.LatLngWrapped;

/**
 * The main activity for the app. Displays all of the Fragments
 * and nav bar
 */
public class HomeActivity extends AppCompatActivity {
    BottomNavigationView nav;
    ActionBar bar;
    public static LatLngWrapped coordinates;
    public static int flag = 0;

    /**
     * Sets the initial state of the app
     * @param savedInstanceState Describes a saved version of this instance if one exists
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bar = getSupportActionBar();

        //Describes the nav bar controls
        nav = findViewById(R.id.menuNav);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFrag = null;
                String tag = null;

                switch(item.getItemId()){
                    case R.id.itemHome:
                        tag = "HOME";
                        selectedFrag = HomeFragment.newInstance();
                        break;
                    case R.id.itemJobs:
                        tag = "JOBS";
                        selectedFrag = JobsFragment.newInstance();
                        break;
                    case R.id.itemProfile:
                        tag = "PROFILE";
                        selectedFrag = MyProfileFragment.newInstance();
                        break;
                    case R.id.itemMessages:
                        tag = "MESSAGES";
                        selectedFrag = AllMessagesFragment.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameNull, selectedFrag);
                transaction.addToBackStack(tag);
                transaction.commit();
                return true;
            }
        });

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.frameNull, HomeFragment.newInstance());
        trans.commit();
    }

    /**
     * Opens a fullscreen dialog for job creation
     */
    public void openJobCreationDialog(){
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
                submitNewJob();
            }
        });

        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    }

    /**
     * Closes any open dialog Fragment
     */
    private void closeDialog(){
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("DIALOG")).commit();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
    }

    public int locationVerification(String location){
        return 0;
    };

    /**
     * Submits a job from the JobCreationFragment
     */
    private void submitNewJob(){
        //add in logic for flag value on submission
        if(flag > 0){
            ImageView imgJobPic = findViewById(R.id.imgMyProfileImage);
            byte[] imgArr = null;
            if(imgJobPic.getDrawable() != null) {
                Bitmap image = ((BitmapDrawable) imgJobPic.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imgArr = stream.toByteArray();
                image.recycle();
            }

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
            Job job = new Job(title, desc, location, coordinates, expDate, startingPrice);
            DatabaseManager.shared.addNewJob(job, imgArr);

            closeDialog();
        }
        else{
            Toast.makeText(this, "please enter a valid address before submission",
                    Toast.LENGTH_LONG).show();
        }

    }

    // When user clicks menu (3 dots) in app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    // Menu item selected
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

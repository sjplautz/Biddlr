package ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.biddlr.R;

//Represents the screen that pops up when the user first opens Biddlr
public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME = 4000; //This is 4 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        //Code to start timer and take action after the timer ends
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity( new Intent(SplashActivity.this, LoginActivity.class));

                finish();
            }
        }, SPLASH_TIME);
    }
}


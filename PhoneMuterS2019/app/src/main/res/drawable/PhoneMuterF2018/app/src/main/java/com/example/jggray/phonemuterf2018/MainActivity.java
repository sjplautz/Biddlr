package com.example.jggray.phonemuterf2018;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private AudioManager audioManager;
    private boolean phoneSilent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

         phoneSilent = false;

        Intent temp = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
        startActivityForResult(temp, 0);

        checkPhoneStatus();
        setToggleListener();
        toggleGui();

    }

    private void checkPhoneStatus() {

        int ringerMode = audioManager.getRingerMode();

        phoneSilent = (ringerMode == AudioManager.RINGER_MODE_SILENT);

    }

    private void setToggleListener() {

        Button btnToggle = (Button) findViewById(R.id.btnToggle);

        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (phoneSilent) {
                    phoneSilent = false;
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                } else {
                    phoneSilent = true;
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                }

                toggleGui();

            }
        });

    }

    private void toggleGui() {

        ImageView imgPhone = (ImageView) findViewById(R.id.imgPhone);

        if(phoneSilent)
            imgPhone.setImageResource(R.drawable.phone_silent);
        else
            imgPhone.setImageResource(R.drawable.phone_on);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

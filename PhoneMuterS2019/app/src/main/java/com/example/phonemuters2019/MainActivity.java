package com.example.phonemuters2019;

import android.content.Intent;
import android.media.AudioManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private AudioManager audioManager;
    private boolean phoneSilent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
    phoneSilent = false;

    Intent temp = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

    startActivityForResult(temp, 0);

    checkPhoneStatus();
    setToggleListener();
    toggleGUO();


    }

    private void checkPhoneStatus()
    {
        int ringerMode = audioManager.getRingerMode();

        phoneSilent = (ringerMode == AudioManager.RINGER_MODE_SILENT);

    }
    private void setToggleListener()
    {
        Button btnToggle = (Button) findViewById(R.id.btnToggle);

        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(phoneSilent)
                {
                    phoneSilent = false;
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
                else
                {
                    phoneSilent = true;
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

                }

                toggleGUO();


            }
        });

    }

    private void toggleGUO()
    {
        ImageView imgPhone = (ImageView) findViewById(R.id.imgPhone);
        if(phoneSilent)
            imgPhone.setImageResource(R.drawable.phone_silent);
        else
            imgPhone.setImageResource(R.drawable.phone_on);

    }
}

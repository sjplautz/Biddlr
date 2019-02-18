package com.example.biddlr;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.frameNull, HomeFragment.newInstance());
        trans.commit();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}

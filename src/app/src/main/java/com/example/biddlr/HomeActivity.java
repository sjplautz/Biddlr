package com.example.biddlr;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView nav = findViewById(R.id.menuNav);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFrag = null;
                switch(item.getItemId()){
                    case R.id.itemHome:
                        selectedFrag = HomeFragment.newInstance();
                        break;
                    case R.id.itemExplore:
                        selectedFrag = ExploreFragment.newInstance();
                        break;
                    case R.id.itemMyJobs:
                        selectedFrag = MyJobsFragment.newInstance();
                        break;
                    case R.id.itemProfile:
                        selectedFrag = ProfileFragment.newInstance();
                        break;
                    case R.id.itemMessages:
                        selectedFrag = ProfileFragment.newInstance();
                        break;
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameNull, selectedFrag);
                transaction.commit();
                return true;
            }
        });

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.frameNull, HomeFragment.newInstance());
        trans.commit();
    }
}

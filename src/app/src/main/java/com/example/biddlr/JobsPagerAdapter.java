package com.example.biddlr;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class JobsPagerAdapter extends FragmentPagerAdapter {
    private String titles[] = {"Explore", "My Jobs"};

    public JobsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                ExploreFragment exploreFrag = new ExploreFragment();
                return exploreFrag;
            case 1:
                MyJobsFragment myJobsFrag = new MyJobsFragment();
                return myJobsFrag;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int i){
        return titles[i];
    }
}

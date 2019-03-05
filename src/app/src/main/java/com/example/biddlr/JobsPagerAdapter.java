package com.example.biddlr;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class JobsPagerAdapter extends FragmentStatePagerAdapter {
    private String titles[] = {"Explore", "My Jobs"};

    public JobsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return ExploreFragment.newInstance();
            case 1:
                return MyJobsFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int i){
        return titles[i];
    }
}

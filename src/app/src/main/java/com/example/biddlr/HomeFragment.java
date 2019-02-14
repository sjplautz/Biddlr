package com.example.biddlr;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Fragment mapFragment = new MapFragment();
        FragmentTransaction trans = getChildFragmentManager().beginTransaction();
        //FIXME Causes issues when API Key doesn't exist
//        trans.add(R.id.mapView, mapFragment).commit();

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}

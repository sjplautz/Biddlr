package ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.location.Location;
import android.location.LocationManager;

import com.example.biddlr.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import classes.DatabaseManager;
import classes.Job;
import classes.LatLngWrapped;
import interfaces.JobDataListener;

import static android.support.constraint.Constraints.TAG;

public class MapFragment extends Fragment implements OnMapReadyCallback, JobDataListener {

    public static GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private CameraPosition mCameraPosition;
    private Location mCurrentLocation;
    public static Location mLastKnownLocation;
    public static List<Marker> markerList = new ArrayList<Marker>();

    MapView mMapView;
    View mView;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 13;
    private static final double DEFAULT_LAT = 33.34;
    private static final double DEFAULT_LONG = -87.67;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private boolean mLocationPermissionGranted = false;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView != null){
            // Construct a FusedLocationProviderClient to handle location actions
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            //call the following init methods for the map
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        MapsInitializer.initialize(getContext());

        //get map handle and set type to normal
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //turns on the my location layer and related control on the map
        updateLocationUI();

        //gets the devices location
        Location currentLocation = getDeviceLocation();

        //in the event the current location was null, sets to defaults
        if(currentLocation == null){
            currentLocation = new Location("");
            currentLocation.setLatitude(DEFAULT_LAT);
            currentLocation.setLongitude(DEFAULT_LONG);
        }

        //wrap the location, and then retrieve jobs within a radius of that location, creating pins for these jobs
        LatLngWrapped wrappedCurrentLocation = LatLngWrapped.wrap(currentLocation);
        DatabaseManager.shared.setJobsForLocationListener(wrappedCurrentLocation,100.0,50, this);

        //sets the map to gotten location
        setMapCamera(currentLocation);
    }

    //updates the ui to indicate that location permissions are either allowed or not
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    //requests location permissions at runtime
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
                mLocationPermissionGranted = true;
        }
        else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    //handles the result of the location permissions request, then calls updateLocationUI
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    //gets the best and most recent location of the device, using a default if location is null
    public Location getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                //setting up criteria for determining the best provider
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(true);

                //returns the string of the best provider tpye
                String Provider = lm.getBestProvider(criteria, true);

                //get current location using the best provider
                mLastKnownLocation = lm.getLastKnownLocation(Provider);
                //Log.d("EXPERIMENT", "get device location method called, and retrieved location with coords: "
                        //+ mLastKnownLocation.getLatitude() + " " + mLastKnownLocation.getLongitude());
            }
        }
        catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
        return mLastKnownLocation;
    }

    //centers the maps camera on the user's location, if location could not be found on default coords
    private void setMapCamera(Location mLastKnownLocation){
        //if location extraction was successful, set the map to location's coords
        if(mLastKnownLocation != null){
            Log.d(TAG, "Current location was not null!");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        }
        //else set map to default coords
        else{
            Log.d(TAG, "Current location is null. Using defaults.");
            Log.e(TAG, "Exception");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(DEFAULT_LAT, DEFAULT_LONG), DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    @Override
    //saves the state of map upon pausing activity
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    //adds pins to the map for any jobs that are within the radius specified
    public void newDataReceived(Job job) {
        LatLngWrapped latLng = job.getCoordinates();
        Log.d("MAP PINS", "adding pin with lat: " + latLng.getLat() + " and lng: " + latLng.getLng());
        MarkerOptions jobMarkerOptions = new MarkerOptions().position(new LatLng(latLng.getLat(), latLng.getLng() )).title(job.getTitle());
        Marker markerHandle = mMap.addMarker(jobMarkerOptions);
        markerList.add(markerHandle);
    }

    @Override
    public void dataRemoved(Job job) {
        //get handle to correct marker from markerlist
        Marker markerToRemove = getMarkerHandle(markerList, job);
        //now remove the marker from the map
        //if(markerToRemove != null)
            markerToRemove.remove();
    }

    @Override
    public void dataChanged(Job job) { }

    private Marker getMarkerHandle(List<Marker> markerList, Job job){
        String jobTitle = job.getTitle();
        LatLngWrapped latLngWrapped = job.getCoordinates();
        LatLng jobLatLng = new LatLng(latLngWrapped.getLat(), latLngWrapped.getLng());
        for(int i = 0; i < markerList.size(); i++){
            Marker currentMarker = markerList.get(i);
            String currentTitle = currentMarker.getTitle();
            LatLng currentLatLng = currentMarker.getPosition();
            if((currentTitle.equals(jobTitle)) && (currentLatLng.equals(jobLatLng))){
                return currentMarker;
            }
        }
        return null;
    }
}
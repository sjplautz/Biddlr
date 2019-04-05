package ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
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

//class that represents the map fragment displayed on the home activity page
public class MapFragment extends Fragment implements OnMapReadyCallback, JobDataListener {

    public static GoogleMap mMap;
    public static Location mCurrentLocation;
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
    private static LocationManager locationManager;
    private static String Provider;

    private boolean mLocationPermissionGranted = false;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        mMapView = mView.findViewById(R.id.map);
        if(mMapView != null){
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

        //sets up the location manager to be used
        setUpLocationManager();

        //attempts to update the device's current location
        getCurrentLocation();

        //in the event the current location was null, sets to defaults
        if(mCurrentLocation == null){
            Log.d("CURRENT LOCATION", "Current location is null. Trying last known location.");
            getLastKnownLocation();
            if(mCurrentLocation == null){
                Log.d("CURRENT LOCATION", "Last known location is null. Using defaults.");
                mCurrentLocation = new Location("");
                mCurrentLocation.setLatitude(DEFAULT_LAT);
                mCurrentLocation.setLongitude(DEFAULT_LONG);
            }
        }

        //wrap the location, and then retrieve jobs within a radius of that location, creating pins for these jobs
        LatLngWrapped wrappedCurrentLocation = LatLngWrapped.wrap(mCurrentLocation);
        DatabaseManager.shared.setJobsForLocationListener(wrappedCurrentLocation,100.0,50, this);

        //sets the map to gotten location
        setMapCamera(mCurrentLocation);
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
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true;
            mMap.setMyLocationEnabled(true);
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

    //sets up the location manager
    public void setUpLocationManager(){
        if (mLocationPermissionGranted) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            //setting up criteria for determining the best provider
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);

            //returns the string of the best provider tpye
            Provider = locationManager.getBestProvider(criteria, true);
        }
    }

    //gets the best and most recent location of the device, using a default if location is null
    public void getCurrentLocation(){
        try {
                //Identify a listener that responds to location updates
                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        mCurrentLocation = location;
                    }
                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {}
                    @Override
                    public void onProviderEnabled(String s) {}
                    @Override
                    public void onProviderDisabled(String s) {}
                };

                //request a single update to the user's location
                locationManager.requestLocationUpdates(Provider, 0, 0, locationListener);
                //potentially turn off location updates here
        }
        catch(SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void getLastKnownLocation(){
        try {
            mCurrentLocation = locationManager.getLastKnownLocation(Provider);
        }
        catch(SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    //centers the maps camera on the user's location, if location could not be found on default coords
    private void setMapCamera(Location mLastKnownLocation){
        //if location extraction was successful, set the map to location's coords
        if(mLastKnownLocation != null){
            //Log.d(TAG, "Current location was not null!");
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
    //listener actions for when job data is removed
    public void dataRemoved(Job job) {
        //get handle to correct marker from markerlist
        Marker markerToRemove = getMarkerHandle(markerList, job);
        //now remove the marker from the map
        markerToRemove.setVisible(false);
        markerToRemove.remove();
        Log.d("REMOVE PIN", "now removing a pin with title: " + markerToRemove.getTitle());
    }

    @Override
    //listener actions for when job data is changed
    public void dataChanged(Job job) { }

    //called by dataRemoved listener, gets the pin associated with the job that was removed and removes it from the map
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
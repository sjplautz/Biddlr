package com.example.biddlr;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

import classes.HttpGetRequest;
import classes.LatLngWrapped;

//TODO Move submission code here
public class JobCreationFragment extends DialogFragment {

    public static JobCreationFragment newInstance() {
        JobCreationFragment fragment = new JobCreationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_job_creation, container, false);

        LocalDateTime now = LocalDateTime.now();

        String date = now.format(DateTimeFormatter.ofPattern("MM/dd/uuuu"));
        TextView txtDate = v.findViewById(R.id.txtDate);
        txtDate.setText(date);

        String time = now.format(DateTimeFormatter.ofPattern("hh:mm a"));
        TextView txtTime = v.findViewById(R.id.txtTime);
        txtTime.setText(time);

        ImageButton btnDate = v.findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker dp = new DatePicker();
                dp.show(getChildFragmentManager(), null);
            }
        });

        ImageButton btnTime = v.findViewById(R.id.btnTime);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker tp = new TimePicker();
                tp.show(getChildFragmentManager(), null);
            }
        });

        Button btnCheckAddress = v.findViewById(R.id.btnCheckAddress);
        btnCheckAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyAddress();
            }
        });


        return v;
    }

    public void verifyAddress(){
        TextView locationView = getActivity().findViewById(R.id.txtLocation);
        String location = locationView.getText().toString();

        if(!location.isEmpty()){
            HomeActivity.coordinates = geocode(location);
            Toast.makeText(getActivity(), "address checked out",
                    Toast.LENGTH_LONG).show();
            HomeActivity.flag = 1;
        }
        else{
            Toast.makeText(getActivity(), "please enter a non-empty address",
                    Toast.LENGTH_LONG).show();
            HomeActivity.flag = 0;
        }

        return;
    }

    //forward geocode for retrieving coordinates from address
    public static LatLngWrapped geocode(String address){
        String apiKey = "NypbUMfluOKXSv4v02Gq1Er3kIA9AfVB";
        String requestPath = "http://www.mapquestapi.com/geocoding/v1/address?key="+apiKey+"&location=";
        LatLng coordinates;
        double lat = 0;
        double lng = 0;

        //get a json response object using httpRequest to Mapquest API
        JSONObject response = httpRequest(requestPath, address);

        //Parse the response object to extract lat and lng
        try{
            //navigate through JSON object to get to desired attributes
            JSONArray results = response.getJSONArray("results");
            JSONObject resultsObject = results.getJSONObject(0);
            JSONArray locations = resultsObject.getJSONArray("locations");
            JSONObject locationsObject = locations.getJSONObject(0);
            JSONObject latLng = locationsObject.getJSONObject("latLng");

            lat = latLng.getDouble("lat");
            lng = latLng.getDouble("lng");

            Log.d("VERIFICATION", "latitude: " + lat + " longitude: " + lng);

            return new LatLngWrapped(lat, lng);
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("VERIFICATION", "inside of exception e in geocode method");
            return null;
        }
    }

    //makes an http request to a selected url and returns a response JSON object
    public static JSONObject httpRequest(String path, String address){

        String returned;
        JSONObject result;

        //Combining path and address for full http request
        String request = path;
        try{
            request += URLEncoder.encode(address,"UTF-8");
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

        //Instantiate new instance of Async Task get request class
        HttpGetRequest getRequest = new HttpGetRequest();

        //perform do in background method to offload from main thread to new networking thread
        try{
            returned = getRequest.execute(request).get();
        }
        catch(InterruptedException e){
            e.printStackTrace();
            return null;
        }
        catch(ExecutionException e){
            e.printStackTrace();
            return null;
        }

        try{
            result = new JSONObject(returned);

            //print out returned string to terminal for debugging
            Log.d("OUTPUT", returned);
        }
        catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return result;
    }
}

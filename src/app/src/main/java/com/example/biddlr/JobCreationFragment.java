package com.example.biddlr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import static android.app.Activity.RESULT_OK;

//TODO Move submission code here

/**
 * The Fragment that handles job creation
 */
public class JobCreationFragment extends DialogFragment {
    private ImageView imgJobPicture;
    private BottomSheetDialog typePicker;

    /**
     * Creates a JobCreationFragment
     * @return An instance of a JobCreationFragment
     */
    public static JobCreationFragment newInstance() {
        return new JobCreationFragment();
    }

    /**
     * Method called when the Fragment is created
     * @param savedInstanceState The last saved instance state of the fragment
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Method called when the Fragment is displayed
     * @param inflater Inflates fragment_job_creation.xml to a View
     * @param container Parent view of the layout to be inflated
     * @param savedInstanceState The last saved instance state of the fragment
     * @return The View to display in the Fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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

        imgJobPicture = v.findViewById(R.id.imgMyProfileImage);

        FloatingActionButton btnImagePicker = v.findViewById(R.id.btnImagePicker);
        btnImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typePicker = new BottomSheetDialog(getActivity());
                View sheetView = getActivity().getLayoutInflater().inflate(R.layout.dialog_image_type, null);

                ImageButton btnCamera = sheetView.findViewById(R.id.btnCamera);
                btnCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cameraPicker = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraPicker, 0);
                    }
                });

                ImageButton btnGallery = sheetView.findViewById(R.id.btnGallery);
                btnGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent galleryPicker = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryPicker, 1);
                    }
                });

                typePicker.setContentView(sheetView);
                typePicker.show();
            }
        });

        //creates a handle to the editText section for addresses
        final TextInputEditText textLocation = v.findViewById(R.id.txtLocation);

        //adds a listener to the use location button, calling the getaddress method and placing results in the address box
        Button btnUseLocation = v.findViewById(R.id.btnUseLocation);
        btnUseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = reverseGeocode();
                Log.d("ADDRESS", "value of address string after reverse geocode: "+address+" end");

                if(textLocation == null){
                    Log.d("ADDRESS", "couldn't get a handle on text box properly");
                }

                //setting the value of address editText box to contain resulting string from reverse geocode
                textLocation.setText(address);

                //clearing the flag to allow for job submission
                HomeActivity.flag = 1;
            }
        });

        //sets a listener on the location editText box, when user clicks off the box, the listener calls address verification method
        textLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    verifyAddress();
                }
            }
        });

        return v;
    }

    /**
     * Handles the results of the Intents used to retrieve the job image
     * @param requestCode The requestCode of the Intent
     * @param resultCode The success state of the Intent
     * @param data The result of the Intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    Bitmap camImage = (Bitmap)data.getExtras().get("data");
                    imgJobPicture.setImageBitmap(camImage);
                    break;
                case 1:
                    imgJobPicture.setImageURI(data.getData());
                    break;
            }
            imgJobPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
            typePicker.hide();
        }
        else{
            System.out.println(resultCode);
            System.out.println("ERROR");
        }
    }

    //given user entered address, ensures address is reasonable and then geocodes
    public void verifyAddress(){
        TextView locationView = getActivity().findViewById(R.id.txtLocation);
        String location = locationView.getText().toString();

        if(!location.isEmpty()){
            //checking that the address fits one of the regex's specified
            Boolean flag = checkFormat(location);
            if(flag){
                Toast.makeText(getActivity(), "valid address entered",
                        Toast.LENGTH_LONG).show();
                HomeActivity.coordinates = forwardGeocode(location);
                HomeActivity.flag = 1;
            }
            else{
                Toast.makeText(getActivity(), "please enter job address in standard mailing format",
                        Toast.LENGTH_LONG).show();
                HomeActivity.flag = 0;
            }
        }
        else{
            Toast.makeText(getActivity(), "please enter a non-empty address",
                    Toast.LENGTH_LONG).show();
            HomeActivity.flag = 0;
        }



        return;
    }

    //checks if address fits specified format for inputting addresses for job creation
    public Boolean checkFormat(String location){
        //             opt              opt                  opt
        //example: 3720 N. 33rd Dr., Apt# 709, Rockford, IL 36301

        //pattern allows for optional cardinal directions, abbreviations, and apt/suite numbers
        String pattern1 = "\\d{1,5}\\s(\\w*.?\\s)?\\w*\\s\\w*.?,?\\s(\\w*.?\\W?\\s\\W?\\d{1,5}(\\s)?)?(,?\\s)?\\w*,?\\s\\w*(\\s\\w*(-\\w*)?)?";

        //test against prescribed format
        if(location.matches(pattern1))
            return true;

        return false;
    }

    //forward geocode for retrieving coordinates from address
    public static LatLngWrapped forwardGeocode(String address){
        String apiKey = "NypbUMfluOKXSv4v02Gq1Er3kIA9AfVB";

        //creating path string to be executed for forward geocode
        String requestPath = "http://www.mapquestapi.com/geocoding/v1/address?key="+apiKey+"&location=";
        try{
            requestPath += URLEncoder.encode(address,"UTF-8");
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

        LatLng coordinates;
        double lat = 0;
        double lng = 0;

        //get a json response object using httpRequest to Mapquest API
        JSONObject response = httpRequest(requestPath);

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
            Log.d("VERIFICATION", "inside of exception e in forward geocode method");
            return null;
        }
    }

    //reverse geocode for retrieving address from coordinates
    public static String reverseGeocode(){
        String apiKey = "NypbUMfluOKXSv4v02Gq1Er3kIA9AfVB";

        //getting the location info retrieved in map fragment
        Location currentLocation = MapFragment.mLastKnownLocation;
        double lat = currentLocation.getLatitude();
        double lng = currentLocation.getLongitude();

        //creating path string to be executed for reverse geocode
        String requestPath = "http://www.mapquestapi.com/geocoding/v1/reverse?key="+apiKey+"&location="
                +Double.toString(lat)+","+Double.toString(lng)+","
                +"&includeRoadMetadata=true&includeNearestIntersection=true";

        //get a json response object using httpRequest to Mapquest API
        JSONObject response = httpRequest(requestPath);

        //Parse the response object to extract lat and lng
        try{
            //navigate through JSON object to get to desired attributes
            JSONArray results = response.getJSONArray("results");
            JSONObject resultsObject = results.getJSONObject(0);
            JSONArray locations = resultsObject.getJSONArray("locations");
            JSONObject locationsObject = locations.getJSONObject(0);

            //getting strings needed to build a resulting address string
            String street = locationsObject.getString("street");
            String city = locationsObject.getString("adminArea5");
            String state = locationsObject.getString("adminArea3");
            String zip = locationsObject.getString("postalCode");

            //now build the address string accordingly
            String addressResult = street+", "+city+", "+state+" "+zip;

            return addressResult;
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("VERIFICATION", "inside of exception e in reverse geocode method");
            return null;
        }
    }

    //makes an http request to a selected url and returns a response JSON object
    public static JSONObject httpRequest(String request){
        String returned;
        JSONObject result;

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

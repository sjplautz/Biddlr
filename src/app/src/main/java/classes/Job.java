package classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.*;

import enums.JobStatus;

public class Job implements Parcelable {

    private String jobID;
    private JobStatus status;
    private String title;
    private String description;
    private Integer posterID;
    private Integer selectedBidderID;
    private String photoURL;
    private String location;
    private LatLng coordinates;
    private LocalDateTime expirationDate;
    private Double startingPrice;
    private Double currentBid;
    private HashMap<Integer, Double> bids;  // <bidderID, bidValue>

    // Initializers
    public Job(String title, String description, int posterID, int selectedBidderID,
               String photoURL, String location, LocalDateTime expirationDate, JobStatus status, double startingPrice,
               double currentBid, HashMap<Integer, Double> bids) {
        this.jobID = null;
        this.status = status;
        this.title = title;
        this.description = description;
        this.posterID = posterID;
        this.selectedBidderID = selectedBidderID;
        this.photoURL = photoURL;
        this.location = location;
        this.expirationDate = expirationDate;
        this.startingPrice = startingPrice;
        this.currentBid = currentBid;
        this.bids = bids;
    }

    public Job(String title, String description, int posterID, String photoURL, String location,
               LocalDateTime expirationDate, double startingPrice) {
        this.jobID = null;
        this.status = JobStatus.IN_BIDDING;
        this.title = title;
        this.description = description;
        this.posterID = posterID;
        this.selectedBidderID = null;
        this.photoURL = photoURL;
        this.location = location;
        this.coordinates = geocode(location);
        this.expirationDate = expirationDate;
        this.startingPrice = startingPrice;
        this.currentBid = null;
        this.bids = null;
    }

    // Parcable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jobID);
        dest.writeInt(this.status.ordinal());
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeInt(this.posterID);
        dest.writeInt(this.selectedBidderID);
        dest.writeString(this.photoURL);
        dest.writeString(this.location);
        dest.writeString(this.expirationDate.toString());
        dest.writeDouble(this.startingPrice);
        dest.writeDouble(this.currentBid);
        dest.writeSerializable(this.bids);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    public Job(Parcel src){
        this.jobID = src.readString();
        this.status = JobStatus.values()[src.readInt()];
        this.title = src.readString();
        this.description = src.readString();
        this.posterID = src.readInt();
        this.selectedBidderID = src.readInt();
        this.photoURL = src.readString();
        this.location = src.readString();
        this.expirationDate = LocalDateTime.parse(src.readString());
        this.startingPrice = src.readDouble();
        this.currentBid = src.readDouble();
        this.bids = (HashMap<Integer, Double>) src.readSerializable();
    }


    // Accessors
    public String getJobID() {
        return this.jobID;
    }

    public JobStatus getJobStatus() {
        return this.status;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Integer getPosterID() {
        return this.posterID;
    }

    public Integer getSelectedBidderID() {
        return this.selectedBidderID;
    }

    public String getPhotoURL() {
        return this.photoURL;
    }

    public String getLocation() {
        return this.location;
    }

    public LocalDateTime getExpirationDate() {
        return this.expirationDate;
    }

    public Double getStartingPrice() {
        return this.startingPrice;
    }

    public Double getCurrentBid() {
        return this.currentBid == null ? 0 : this.currentBid;
    }

    public HashMap<Integer, Double> getBids() {
        return this.bids;
    }

    // Modifiers


    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public void updateJobStatus(JobStatus status) {
        this.status = status;
    }


    public void setSelectedBidder(int bidderID) {
        this.selectedBidderID = bidderID;
    }

    public void addBid(int bidderId, double bid) {
        this.bids.put(bidderId, bid);
        this.currentBid = bid < this.currentBid ? bid : this.currentBid;
    }

    public String getFormattedExpirationDate() {
        // We can mak our own formatter
        return this.expirationDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
    }

    public String getFormattedDateFromNow() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime temp = LocalDateTime.from( now );

        long days = temp.until( this.expirationDate, ChronoUnit.DAYS);
//        temp = temp.plusDays( days );

        long hours = temp.until( this.expirationDate, ChronoUnit.HOURS);
//        temp = temp.plusHours( hours );

        if (hours < 0) { return "Expired"; };
        if (days > 0) {
            return days + " days";
        }
        return hours + " hours";
    }

    /*
    //method used to execute http request within job class
    public static JSONObject httpRequest(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;
        String request = targetURL;

        //error handling in event of incompatible encoding type
        try{
            request += URLEncoder.encode(urlParameters,"UTF-8");
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

        try {
            //Create connection
            URL url = new URL(request);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Get Input
            InputStream dataInputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(dataInputStream));
            StringBuilder in = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                in.append(line);
                in.append(System.lineSeparator());
            }
            reader.close();

            //Build JSON object with returned results
            JSONObject json = new JSONObject(in.toString());
            return json;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    */

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
            returned = getRequest.execute(path).get();
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
        }
        catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return result;
    }


    //forward geocode for retrieving coordinates from address
    public static LatLng geocode(String address){
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
            JSONArray locations = results.getJSONArray(1);
            JSONObject location = locations.getJSONObject(1);

            lat = location.getDouble("lat");
            lng = location.getDouble("lng");

            coordinates = new LatLng(lat, lng);
            return coordinates;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}

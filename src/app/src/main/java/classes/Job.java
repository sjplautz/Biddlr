package classes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.*;

import enums.JobStatus;

public class Job implements Parcelable {

    private String jobID;
    private JobStatus status;
    private String title;
    private String description;
    private String posterID;
    private String selectedBidderID;
    private String photoURL;
    private String location;
    private LatLngWrapped coordinates;
    private LocalDateTimeWrapped expirationDate;
    private Double startingPrice;
    private Double currentBid;
    private HashMap<Integer, Double> bids;  // <bidderID, bidValue>

    // Initializers
    public Job() { }

    // Required init
    public Job(String jobID, String title, String description, String posterID, String selectedBidderID,
               String photoURL, String location, LatLngWrapped coordinates, LocalDateTimeWrapped expirationDate, JobStatus status, double startingPrice,
               double currentBid, HashMap<Integer, Double> bids) {
        this.jobID = jobID;
        this.status = status;
        this.title = title;
        this.description = description;
        this.posterID = posterID;
        this.selectedBidderID = selectedBidderID;
        this.photoURL = photoURL;
        this.location = location;
        this.coordinates = coordinates;
        this.expirationDate = expirationDate;
        this.startingPrice = startingPrice;
        this.currentBid = currentBid;
        this.bids = bids;
    }

    // General init
    public Job(String title, String description, String photoURL, String location,
               LocalDateTime expirationDate, double startingPrice) {
        this.jobID = null;
        this.status = JobStatus.IN_BIDDING;
        this.title = title;
        this.description = description;
        this.posterID = DatabaseManager.shared.mAuth.getCurrentUser().getUid();
        this.selectedBidderID = null;
        this.photoURL = photoURL;
        this.location = location;
        this.coordinates = geocode(location);
        this.expirationDate = new LocalDateTimeWrapped(expirationDate);
        this.startingPrice = startingPrice;
        this.currentBid = startingPrice;
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
        dest.writeString(this.posterID);
        dest.writeString(this.selectedBidderID);
        dest.writeString(this.photoURL);
        dest.writeString(this.location);
        dest.writeString(new LatLng(this.coordinates.lat, this.coordinates.lng).toString());
        dest.writeString(this.expirationDate.localDateTime);
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
        this.posterID = src.readString();
        this.selectedBidderID = src.readString();
        this.photoURL = src.readString();
        this.location = src.readString();
        this.coordinates = new LatLngWrapped(src.readString());
        this.expirationDate = new LocalDateTimeWrapped(src.readString());
        this.startingPrice = src.readDouble();
        this.currentBid = src.readDouble();
        this.bids = (HashMap<Integer, Double>) src.readSerializable();
    }

    // Getters and Setters
    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterID() {
        return posterID;
    }

    public void setPosterID(String posterID) {
        this.posterID = posterID;
    }

    public String getSelectedBidderID() {
        return selectedBidderID;
    }

    public void setSelectedBidderID(String selectedBidderID) {
        this.selectedBidderID = selectedBidderID;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LatLngWrapped getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLngWrapped coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTimeWrapped getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTimeWrapped expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(Double currentBid) {
        this.currentBid = currentBid;
    }

    public HashMap<Integer, Double> getBids() {
        return bids;
    }

    public void setBids(HashMap<Integer, Double> bids) {
        this.bids = bids;
    }


    // Custom Methods
    public void addBid(int bidderId, double bid) {
        this.bids.put(bidderId, bid);
        this.currentBid = bid < this.currentBid ? bid : this.currentBid;
    }

    public String formatExpirationDate() {
        // We can mak our own formatter
        return LocalDateTime.parse(getExpirationDate().getLocalDateTime()).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
    }

    public String formatDateFromNow() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime temp = LocalDateTime.from( now );

        long days = temp.until( LocalDateTime.parse(getExpirationDate().getLocalDateTime()), ChronoUnit.DAYS);
//        temp = temp.plusDays( days );

        long hours = temp.until( LocalDateTime.parse(getExpirationDate().getLocalDateTime()), ChronoUnit.HOURS);
//        temp = temp.plusHours( hours );

        if (hours < 0) { return "Expired"; };
        if (days > 0) {
            return days + " days";
        }
        return hours + " hours";
    }

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

            coordinates = new LatLng(lat, lng);
            return new LatLngWrapped(lat, lng);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
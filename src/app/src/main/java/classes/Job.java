package classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;

import enums.JobStatus;

public class Job implements Parcelable {

    private Integer jobID;
    private JobStatus status;
    private String title;
    private String description;
    private Integer posterID;
    private Integer selectedBidderID;
    private String photoURL;
    private String location;
    private Date expirationDate;
    private Double startingPrice;
    private Double currentBid;
    private HashMap<Integer, Double> bids;  // <bidderID, bidValue>

    // Initializers
    public Job(int jobID, String title, String description, int posterID, int selectedBidderID,
               String photoURL, String location, Date expirationDate, JobStatus status, double startingPrice,
               double currentBid, HashMap<Integer, Double> bids) {
        this.jobID = jobID;
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

    public Job(int jobID, String title, String description, int posterID, String photoURL, String location,
               Date expirationDate, double startingPrice) {
        this.jobID = jobID;
        this.status = JobStatus.IN_BIDDING;
        this.title = title;
        this.description = description;
        this.posterID = posterID;
        this.selectedBidderID = null;
        this.photoURL = photoURL;
        this.location = location;
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
        dest.writeInt(this.jobID);
        dest.writeInt(this.status.ordinal());
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeInt(this.posterID);
        dest.writeInt(this.selectedBidderID);
        dest.writeString(this.photoURL);
        dest.writeString(this.location);
        dest.writeLong(this.expirationDate.getTime());
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
        this.jobID = src.readInt();
        this.status = JobStatus.values()[src.readInt()];
        this.title = src.readString();
        this.description = src.readString();
        this.posterID = src.readInt();
        this.selectedBidderID = src.readInt();
        this.photoURL = src.readString();
        this.location = src.readString();
        this.expirationDate = new Date(src.readLong());
        this.startingPrice = src.readDouble();
        this.currentBid = src.readDouble();
        this.bids = (HashMap<Integer, Double>) src.readSerializable();
    }


    // Accessors
    public Integer getJobID() {
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

    public Date getExpirationDate() {
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

}

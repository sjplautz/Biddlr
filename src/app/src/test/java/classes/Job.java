package classes;

import java.util.Date;

import enums.JobStatus;

public class Job {

    private int jobID;
    private JobStatus status;
    private String title;
    private String description;
    private int posterID;
    private int selectedBidderID;
    private String photoURL;
    private String location;
    private Date expirationDate;
    private double startingPrice;
    private double currentBid;
    private Map<int, double> bids; // <bidderID, bidValue>

    // Initializers
    public Job(int jobID, String title, String description, int posterID, int selectedBidderID,
               String photoURL, String location, Date expirationDate, JobStatus status, double startingPrice,
               double currentBid, Map<int, double> bids) {
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

    // Accessors
    public int getJobID() {
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

    public int getPosterID() {
        return this.posterID;
    }

    public int getSelectedBidderID() {
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

    public double getStartingPrice() {
        return this.startingPrice;
    }

    public double getCurrentBid() {
        return this.currentBid;
    }

    public Map<int, double> getBids() {
        return this.bids;
    }



}

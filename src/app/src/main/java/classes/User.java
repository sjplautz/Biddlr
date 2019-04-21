package classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.stfalcon.chatkit.commons.models.IUser;

import java.util.HashMap;

public class User implements Parcelable, IUser {

    // Properties
    private String userID;
    private String firstName;
    private String lastName;
    private String email;
    private String bio;
    private String profilePic;
    private Double bidderRating;
    private Double posterRating;
    private HashMap<String, Double> biddedJobs; //string jobID
    private Integer jobsCompleted;
    private Integer biddlrPoints;

    // Initializers
    public User() { }

    public User(String firstName, String lastName, String email) {
        this.userID = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = null;
        this.bio = null;
        this.profilePic = null;
        this.bidderRating = 0.0;
        this.posterRating = 0.0;
        this.biddedJobs = null;
        this.jobsCompleted = 0;
        this.biddlrPoints = 100000;
    }

    public User(String id) {
        this.userID = id;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.bio = null;
        this.profilePic = null;
        this.bidderRating = null;
        this.posterRating = null;
        this.biddedJobs = null;
        jobsCompleted = null;
        this.biddlrPoints = 100000;
    }

    // Parcable methods - needed for passing objects between fragments
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userID);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
        dest.writeString(this.bio);
        dest.writeString(this.profilePic);
        dest.writeDouble(this.bidderRating);
        dest.writeDouble(this.posterRating);
        dest.writeSerializable(this.biddedJobs);
        dest.writeInt(this.biddlrPoints);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(Parcel src){
        this.userID = src.readString();
        this.firstName = src.readString();
        this.lastName = src.readString();
        this.email = src.readString();
        this.bio = src.readString();
        this.profilePic = src.readString();
        this.bidderRating = src.readDouble();
        this.posterRating = src.readDouble();
        this.biddedJobs = (HashMap<String, Double>) src.readSerializable();
        this.biddlrPoints = src.readInt();
    }

    // Accessors
    public String getUserID() { return userID; }

    public void setUserID(String userID) { this.userID = userID; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getBio() { return bio; }

    public void setBio(String bio) { this.bio = bio; }

    public String getProfilePic() { return profilePic; }

    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }

    public Double getBidderRating() { return bidderRating; }

    public void setBidderRating(Double bidderRating) { this.bidderRating = bidderRating; }

    public double getAvgBidderRating(){
        return bidderRating == 0.0 ? 0.0 : bidderRating / jobsCompleted;
    }

    public void addBidderRating(double newRating){
        this.bidderRating += newRating;
    }

    public Double getPosterRating() { return posterRating == null ? 5.0 : posterRating; }

    public void setPosterRating(Double posterRating) { this.posterRating = posterRating; }

    public HashMap<String, Double> getBiddedJobs() {
        return biddedJobs;
    }

    public void setBiddedJobs(HashMap<String, Double> biddedJobs) {
        this.biddedJobs = biddedJobs;
    }

    public Integer getJobsCompleted(){
        return jobsCompleted;
    }

    public void setJobsCompleted(Integer jobsCompleted){
        this.jobsCompleted = jobsCompleted;
    }

    public Integer getBiddlrPoints(){return biddlrPoints;}

    public void setBiddlrPoints(Integer biddlrPoints) {this.biddlrPoints = biddlrPoints;}

    public void updateBiddlrPoints(Integer biddlrPoints) {
        this.biddlrPoints += biddlrPoints;
        DatabaseManager.shared.updatePoints(this, this.biddlrPoints);
    }

    /**
     * Adds job to user's list of bidded jobs
     * @param jobID
     * @param bid
     */
    public void addBid(String jobID, Double bid) {
        if (biddedJobs == null) {
            biddedJobs = new HashMap<String, Double>();
        }
        biddedJobs.put(jobID, bid);
        DatabaseManager.shared.addBidForUser(this);
    }

    // Chat Kit methods
    @Override
    public String getId() {
        return userID;
    }

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getAvatar() {
        return userID;
    }

    public boolean isOnline() {
        return true;
    }

}

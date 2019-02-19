package classes;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import enums.JobStatus;

public class User implements Parcelable {

    // Properties
    private Integer userID;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String bio;
    private String profilePic;
    private Double bidderRating;
    private Double posterRating;
    private ArrayList<Integer> postedJobs;
    private ArrayList<Integer> activeJobs;
    private ArrayList<Integer> completedJobs;

    // Initializers
    public User(Integer userID, String password, String firstName, String lastName, String email,
                String bio, String profilePic, Double bidderRating, Double posterRating,
                ArrayList<Integer> postedJobs, ArrayList<Integer> activeJobs, ArrayList<Integer> completedJobs) {
        this.userID = userID;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.bio = bio;
        this.profilePic = profilePic;
        this.bidderRating = bidderRating;
        this.posterRating = posterRating;
        this.postedJobs = postedJobs;
        this.activeJobs = activeJobs;
        this.completedJobs = completedJobs;
    }

    public User(Integer userID, String password, String firstName, String lastName) {
        this.userID = userID;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = null;
        this.bio = null;
        this.profilePic = null;
        this.bidderRating = null;
        this.posterRating = null;
        this.postedJobs = null;
        this.activeJobs = null;
        this.completedJobs = null;
    }

    // Parcable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userID);
        dest.writeString(this.password);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
        dest.writeString(this.bio);
        dest.writeString(this.profilePic);
        dest.writeDouble(this.bidderRating);
        dest.writeDouble(this.posterRating);
        dest.writeList(this.postedJobs);
        dest.writeList(this.activeJobs);
        dest.writeList(this.completedJobs);
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
        this.userID = src.readInt();
        this.password = src.readString();
        this.firstName = src.readString();
        this.lastName = src.readString();
        this.email = src.readString();
        this.bio = src.readString();
        this.profilePic = src.readString();
        this.bidderRating = src.readDouble();
        this.posterRating = src.readDouble();
        src.readList(this.postedJobs, Integer.class.getClassLoader());
        src.readList(this.activeJobs, Integer.class.getClassLoader());
        src.readList(this.completedJobs, Integer.class.getClassLoader());
    }

    // Accessors
    public Integer getUserID() {
        return this.userID;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getBio() {
        return bio;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public Double getBidderRating() {
        return bidderRating;
    }

    public Double getPosterRating() {
        return posterRating;
    }

    public ArrayList<Integer> getPostedJobs() {
        return postedJobs;
    }

    public ArrayList<Integer> getActiveJobs() {
        return activeJobs;
    }

    public ArrayList<Integer> getCompletedJobs() {
        return completedJobs;
    }

    // Modifiers
    public void addPostedJob(Integer jobID) {
        postedJobs.add(jobID);
    }

    public void addActiveJob(Integer jobID) {
        activeJobs.add(jobID);
    }

    public void addCompletedJob(Integer jobID) {
        completedJobs.add(jobID);
    }


    // Class logic
    public Boolean validatePassword(String password) {
        return this.password  == password ? true : false;
    }
}

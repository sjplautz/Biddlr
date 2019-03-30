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
    private String userID;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String bio;
    private String profilePic;
    private Double bidderRating;
    private Double posterRating;

    // Initializers
    public User() { }

    public User(String password, String firstName, String lastName, String email) {
        this.userID = null;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = null;
        this.bio = null;
        this.profilePic = null;
        this.bidderRating = null;
        this.posterRating = null;
    }

    // Parcable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userID);
        dest.writeString(this.password);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
        dest.writeString(this.bio);
        dest.writeString(this.profilePic);
        dest.writeDouble(this.bidderRating);
        dest.writeDouble(this.posterRating);
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
        this.password = src.readString();
        this.firstName = src.readString();
        this.lastName = src.readString();
        this.email = src.readString();
        this.bio = src.readString();
        this.profilePic = src.readString();
        this.bidderRating = src.readDouble();
        this.posterRating = src.readDouble();
    }

    // Accessors
    public String getUserID() { return userID; }

    public void setUserID(String userID) { this.userID = userID; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

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

    public Double getPosterRating() { return posterRating; }

    public void setPosterRating(Double posterRating) { this.posterRating = posterRating; }


    // Class logic
    public Boolean validatePassword(String password) {
        return this.password  == password ? true : false;
    }
}

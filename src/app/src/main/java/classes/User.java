package classes;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {

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

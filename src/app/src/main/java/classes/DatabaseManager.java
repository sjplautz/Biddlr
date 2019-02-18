package classes;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DatabaseManager {

    private static ArrayList<Job> jobs;


    private static ArrayList<User> users;


    public static void setUp() {
        jobs = new ArrayList<Job>();
        jobs.add( new Job(0,"Window Washing", "In need of someone to wash all of the windows on my building. There are about 60 windows total",0,"jop1","123 Main St", LocalDateTime.of(2019, 2,21,12,30),20));
        jobs.add( new Job(1,"Lawn Mowing", "In need of someone to wash all of the windows on my building. There are about 60 windows total",0,"jop1","123 Main St", LocalDateTime.of(2019, 2,21,12,30),20));
        jobs.add( new Job(2,"Sort Change", "In need of someone to wash all of the windows on my building. There are about 60 windows total",0,"jop1","123 Main St", LocalDateTime.of(2019, 2,21,12,30),20));
        jobs.add( new Job(3,"Lawn Mowing", "In need of someone to wash all of the windows on my building. There are about 60 windows total",0,"jop1","123 Main St", LocalDateTime.of(2019, 2,21,12,30),20));
        jobs.add( new Job(4,"Seal 1,000 envelopes", "In need of someone to wash all of the windows on my building. There are about 60 windows total",0,"jop1","123 Main St", LocalDateTime.of(2019, 2,21,12,30),20));

        users = new ArrayList<User>();
        users.add( new User(0, "password", "John", "Smith"));
        users.add( new User(1,"password", "Jane", "Doe"));
    }

    // Jobs
    public static ArrayList<Job> getJobs() {
        return jobs;
    }

    public static void addJob(Job job) {
        jobs.add(job);
    }

    // Users
    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void addUser(User user) {
        users.add(user);
    }
}

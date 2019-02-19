package classes;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DatabaseManager {

    public static DatabaseManager shared = new DatabaseManager();
    private ArrayList<Job> jobs;
    private ArrayList<User> users;

    public void setUp() {
        shared.jobs = new ArrayList<Job>();
        shared.jobs.add( new Job(0,"Window Washing", "In need of someone to wash all of the windows on my building. There are about 60 windows total",0,"job1","123 Main St", LocalDateTime.of(2019, 2,21,12,30),20));
        shared.jobs.add( new Job(1,"Lawn Mowing", "Yard is approximately 10,000 square feet.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,21,12,30),20));
        shared.jobs.add( new Job(2,"Sort Change", "I have 20 jars of unsorted change that needs to be rolled. ",0,"job1","123 Main St", LocalDateTime.of(2019, 2,21,12,30),20));
        shared.jobs.add( new Job(3,"Lawn Mowing", "In need of someone to wash all of the windows on my building. There are about 60 windows total",0,"job1","123 Main St", LocalDateTime.of(2019, 2,21,12,30),20));
        shared.jobs.add( new Job(4,"Seal 1,000 envelopes", "In need of someone to seal 1,000 envelopes.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,21,12,30),20));

        shared.users = new ArrayList<User>();
        shared.users.add( new User(0, "password", "John", "Smith", "", "", "", 4.0, 4.0, null, null, null));
        shared.users.add( new User(1,"password", "Jane", "Doe"));
    }

    // Jobs
    public ArrayList<Job> getJobs() {
        return shared.jobs;
    }

    public void addJob(Job job) {
        shared.jobs.add(job);
    }

    // Users
    public ArrayList<User> getUsers() {
        return shared.users;
    }

    public void addUser(User user) {
        shared.users.add(user);
    }
}

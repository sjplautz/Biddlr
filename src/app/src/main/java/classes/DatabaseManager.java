package classes;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DatabaseManager {

    public static DatabaseManager shared = new DatabaseManager();
    private ArrayList<Job> jobs;
    private ArrayList<User> users;

    public void setUp() {
        shared.jobs = new ArrayList<Job>();
        shared.jobs.add( new Job(0,"Window Washing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(1,"Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(2,"Sort Change", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(3,"Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(4,"Seal 1,000 envelopes", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(5,"Window Washing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(6,"Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.I have 20 jars of unsorted change that needs to be rolled. ",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(8,"Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(9,"Seal 1,000 envelopes", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(10,"Window Washing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(11,"Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(12,"Sort Change", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(13,"Lawn Mowing", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));
        shared.jobs.add( new Job(14,"Seal 1,000 envelopes", "Lorem ipsum dolor sit amet, illum recteque his at, veniam verear ne ius, ad mea aliquam definitionem. Has elitr splendide argumentum in. Qui ei tantas doctus sensibus. Case efficiantur ex duo.",0,"job1","123 Main St", LocalDateTime.of(2019, 2,22,12,30),20));

        shared.users = new ArrayList<User>();
        shared.users.add( new User(0, "password", "John", "Smith", "", "", "", 4.0, 4.0, null, null, null));
        shared.users.add( new User(1,"password", "Jane", "Doe"));
    }

    // Jobs
    public ArrayList<Job> getJobs() {
        return shared.jobs;
    }

    //adds job to front of shared jobs lists
    public void addJob(Job job) {
        shared.jobs.add(0, job);
    }

    // Users
    public ArrayList<User> getUsers() {
        return shared.users;
    }

    public void addUser(User user) {
        shared.users.add(user);
    }
}

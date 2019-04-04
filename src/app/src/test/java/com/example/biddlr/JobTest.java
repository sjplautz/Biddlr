package com.example.biddlr;

import android.os.Parcel;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import adapters.JobListAdapter;
import classes.Job;
import classes.LatLngWrapped;
import classes.LocalDateTimeWrapped;
import enums.JobStatus;

import static org.junit.Assert.*;

public class JobTest {
    @Test
    public void isPastExpirationDate_isCorrect(){
        Job job = new Job();

        //Sets expiration date to one hour ago
        job.setExpirationDate(new LocalDateTimeWrapped(LocalDateTime.now().minusHours(1)));
        assertTrue(job.isNowPastExpirationDate());

        //Sets expiration date to one hour in the future
        job.setExpirationDate(new LocalDateTimeWrapped(LocalDateTime.now().plusHours(1)));
        assertFalse(job.isNowPastExpirationDate());
    }

    @Test
    public void formattedExpirationDate_isCorrect(){
        Job job = new Job();

        //Sets expiration date to Jan 1 2019
        job.setExpirationDate(new LocalDateTimeWrapped(LocalDateTime.of(2019, 1, 2, 0, 0)));
        assertEquals("Jan 2, 2019 12:00:00 AM", job.formattedExpirationDate());
    }

    @Test
    public void formattedTimeFromNow_isCorrect(){
        Job job = new Job();

        //Sets expiration date to one hour ago
        job.setExpirationDate(new LocalDateTimeWrapped(LocalDateTime.now().minusHours(1)));
        assertEquals("Expired", job.formattedTimeFromNow());

        job.setExpirationDate(new LocalDateTimeWrapped(LocalDateTime.now().plusHours(1)));
        assertEquals("1 hours", job.formattedTimeFromNow());

        job.setExpirationDate(new LocalDateTimeWrapped(LocalDateTime.now().plusDays(2)));
        assertEquals("2 days", job.formattedTimeFromNow());
    }

    @Test
    public void equals_isCorrect(){
        Job job1 = new Job();
        Job job2 = new Job();

        job1.setJobID("TestID");
        job2.setJobID("TestID");

        assertNotSame(job1, job2);
        assertEquals(job1, job2);
    }

    @Test
    public void writeToParcel_isCorrect(){
        Job job1 = new Job();
        job1.setJobID("ID");
        job1.setStatus(JobStatus.IN_PROGRESS);
        job1.setTitle("Title");
        job1.setDescription("Desc");
        job1.setPosterID("PosterID");
        job1.setSelectedBidderID("BidderID");
        job1.setLocation("Here");
        job1.setCoordinates(new LatLngWrapped(1.0, 1.0));
        job1.setExpirationDate(new LocalDateTimeWrapped(LocalDateTime.now()));
        job1.setStartingPrice(30.0);
        job1.setCurrentBid(20.0);
        HashMap<String, Double> bids = new HashMap<>();
        bids.put("BidderID", 20.0);
        job1.setBids(bids);

        Parcel p = Parcel.obtain();
        job1.writeToParcel(p, 0);

        Job job2 = (Job) Job.CREATOR.createFromParcel(p);

        assertEquals("ID", job2.getJobID());
        assertEquals("Here", job2.getLocation());
    }
}

package com.example.biddlr;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashMap;

import classes.User;

public class UserTest {
    @Test
    public void constructor_isCorrect(){
        User user1 = new User("Stephen","Plautz","email");
        User user2 = new User("Stephen","Plautz","email");

        assertNotSame(user1, user2);
        assertEquals(user1.getFirstName(), user2.getFirstName());
        assertEquals(user1.getLastName(), user2.getLastName());
        assertEquals(user1.getEmail(), user2.getEmail());
    }

    @Test
    public void setPosterRating_isCorrect(){
        User user1 = new User("Stephen","Plautz","email");
        Double correctRating = 3.6;
        user1.setPosterRating(3.6);

        assertEquals(correctRating, 3.6, 0);
    }

    @Test
    public void setBio_isCorrect(){
        User user1 = new User("Stephen","Plautz","email");
        String correctBio = "this is correct";
        user1.setBio("this is correct");

        assertEquals(correctBio, user1.getBio());
    }

    @Test
    public void getAvgBidderRating_isCorrect(){
        User user = new User();
        user.setJobsCompleted(5);
        user.setBidderRating(25.0);

        assertEquals(5.0, user.getAvgBidderRating(), .01);
    }
}

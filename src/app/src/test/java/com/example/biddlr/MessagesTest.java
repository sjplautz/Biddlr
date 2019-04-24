package com.example.biddlr;
import android.text.format.DateUtils;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import classes.ChatMessage;
import classes.User;
import ui.AllMessagesFragment;

import static org.junit.Assert.*;

public class MessagesTest {

    @Test
    public void id_isCorrect() {
        String id = "id";
        User user = new User("Abby", "Smith", "email@test.com");
        String message = "Message";
        Date time = new Date();

        ChatMessage message1 = new ChatMessage(id, user, message, time);

        ChatMessage message2 = new ChatMessage();
        message2.setId(id);
        message2.setUser(user);
        message2.setText(message);
        message2.setCreatedAt(time);

        assertEquals(message1.getId(), message2.getId());
    }

    @Test
    public void message_isCorrect() {
        String id = "id";
        User user = new User("Abby", "Smith", "email@test.com");
        String message = "Message";
        Date time = new Date();

        ChatMessage message1 = new ChatMessage(id, user, message, time);

        ChatMessage message2 = new ChatMessage();
        message2.setId(id);
        message2.setUser(user);
        message2.setText(message);
        message2.setCreatedAt(time);

        assertEquals(message1.getText(), message2.getText());
    }

    @Test
    public void createdAt_isCorrect() {
        String id = "id";
        User user = new User("Abby", "Smith", "email@test.com");
        String message = "Message";
        Date time = new Date();

        ChatMessage message1 = new ChatMessage(id, user, message, time);

        ChatMessage message2 = new ChatMessage();
        message2.setId(id);
        message2.setUser(user);
        message2.setText(message);
        message2.setCreatedAt(time);

        assertEquals(message1.getCreatedAt(), message2.getCreatedAt());
    }

    @Test
    public void representationId_isCorrect() {
        String id = "id";
        User user = new User("Abby", "Smith", "email@test.com");
        String message = "Message";
        Date time = new Date();

        ChatMessage message1 = new ChatMessage(id, user, message, time);
        HashMap<String, Object> rep1 = new HashMap<>();
        rep1.put("id", id);

        HashMap<String, Object> rep2 = message1.getRepresentation();

        assertEquals(rep1.get("id"), rep1.get("id"));
    }

    @Test
    public void representationText_isCorrect() {
        String id = "id";
        User user = new User("Abby", "Smith", "email@test.com");
        String message = "Message";
        Date time = new Date();

        ChatMessage message1 = new ChatMessage(id, user, message, time);
        HashMap<String, Object> rep1 = new HashMap<>();
        rep1.put("text", message);

        HashMap<String, Object> rep2 = message1.getRepresentation();

        assertEquals(rep1.get("text"), rep1.get("text"));
    }

    @Test
    public void representationCreatedAt_isCorrect() {
        String id = "id";
        User user = new User("Abby", "Smith", "email@test.com");
        String message = "Message";
        Date time = new Date();
        ChatMessage message1 = new ChatMessage(id, user, message, time);

        HashMap<String, Object> rep1 = new HashMap<>();
        rep1.put("createdAt", time);

        HashMap<String, Object> rep2 = message1.getRepresentation();

        assertEquals(rep1.get("createdAt"), rep1.get("createdAt"));
    }

    @Test
    public void formatDateTest() {
        Instant now = Instant.now(); //current date
        Instant yesterday = now.minus(Duration.ofDays(1));
        Date date = java.sql.Date.from(yesterday);

        AllMessagesFragment frag = new AllMessagesFragment();
        String formattedDate = frag.format(date);

        assertEquals("Yesterday", formattedDate);
    }

}

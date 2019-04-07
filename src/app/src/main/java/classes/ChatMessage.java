package classes;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.stfalcon.chatkit.commons.models.IMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatMessage implements IMessage {

    private String id;
    private String text;
    private Date createdAt;
    private User user;

    public ChatMessage(String id, User user, String text) {
        this(id, user, text, new Date());
    }

    public ChatMessage(String id, User user, String text, Date createdAt) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.createdAt = createdAt;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    public String getStatus() {
        return "Sent";
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public HashMap<String, Object> getRepresentation() {

        HashMap<String, Object> rep = new HashMap<>();
        rep.put("id", id);
        rep.put("text", text);
        rep.put("createdAt", createdAt);
        rep.put("user", user.getId());

        return rep;
    }

    static public ChatMessage parse(QueryDocumentSnapshot snapshot) {
        String id = snapshot.getString("id");
        String text = snapshot.getString("text");
        Date createdAt = snapshot.getTimestamp("createdAt").toDate();
//                               ArrayList<String> userIds = new ArrayList<String>();
//                                userIds = (ArrayList<String>) doc.get("userIds");
        ChatMessage m = new ChatMessage(id, DatabaseManager.shared.currentUser, text, createdAt);
        return m;
    }
}
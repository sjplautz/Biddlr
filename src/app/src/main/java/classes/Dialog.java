package classes;

import android.os.Message;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.stfalcon.chatkit.commons.models.IDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Dialog implements IDialog<ChatMessage> {

    private String id;
    private String dialogPhoto;
    private String dialogName;
    private ArrayList<User> users;
    private ChatMessage lastMessage;
    private Date lastMessageTime;

    private int unreadCount;

    public Dialog(String id, String name, String photo,
                  ArrayList<User> users, ChatMessage lastMessage, Date lastMessageTime, int unreadCount) {

        this.id = id;
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.users = users;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.unreadCount = unreadCount;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public ArrayList<User> getUsers() {
        return users;
    }

    @Override
    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Date getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Date lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public HashMap<String, Object> getRepresentation() {

        ArrayList<String> userIds = new ArrayList<String>();
        userIds.add(users.get(0).getId());
        userIds.add(users.get(1).getId());
        Collections.sort(userIds);

        ArrayList<String> userNames = new ArrayList<String>();
        userNames.add(users.get(0).getName());
        userNames.add(users.get(1).getName());

        HashMap<String, Object> rep = new HashMap<>();
        rep.put("id", id);
//        rep.put("dialogName", dialogName);
//        rep.put("dialogPhoto", dialogPhoto);
        rep.put("userIds", userIds);
        rep.put("userNames", userNames);
        rep.put("lastMessage", lastMessage.getId());
        rep.put("lastMessageTime", lastMessage.getCreatedAt());
        rep.put("unreadCount", unreadCount);

        return rep;
    }

    static public Dialog parse(DocumentSnapshot snapshot) {
        String id = snapshot.getString("id");

        ArrayList<String> userIds = (ArrayList<String>) snapshot.get("userIds");
        int idsIndex = userIds.get(0).equals(DatabaseManager.shared.currentUser.getUserID()) ? 1 : 0;

        ArrayList<User> users = new ArrayList<User>();
        users.add(DatabaseManager.shared.currentUser);

        ArrayList<String> userNames = (ArrayList<String>) snapshot.get("userNames");
        int namesIndex = userNames.get(0).equals(DatabaseManager.shared.currentUser.getName()) ? 1 : 0;

        Date lastMessageTime = snapshot.getDate("lastMessageTime");

        ChatMessage lastMessage = new ChatMessage("id", DatabaseManager.shared.currentUser, snapshot.getString("lastMessage"), lastMessageTime);
        int unreadCount = snapshot.getLong("unreadCount").intValue();

        Dialog d = new Dialog(id, userNames.get(namesIndex), userIds.get(idsIndex), users, lastMessage, lastMessageTime, unreadCount);
        return d;
    }
}
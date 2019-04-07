package classes;

import android.os.Message;

import com.stfalcon.chatkit.commons.models.IDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dialog implements IDialog<ChatMessage> {

    private String id;
    private String dialogPhoto;
    private String dialogName;
    private ArrayList<User> users;
    private ChatMessage lastMessage;

    private int unreadCount;

    public Dialog(String id, String name, String photo,
                  ArrayList<User> users, ChatMessage lastMessage, int unreadCount) {

        this.id = id;
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.users = users;
        this.lastMessage = lastMessage;
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

    public HashMap<String, Object> getRepresentation() {

        ArrayList<String> userIds = new ArrayList<String>();
        userIds.add(users.get(0).getId());
        userIds.add(users.get(1).getId());

        HashMap<String, Object> rep = new HashMap<>();
        rep.put("id", id);
        rep.put("dialogName", dialogName);
        rep.put("dialogPhoto", dialogPhoto);
        rep.put("users", userIds);
        rep.put("lastMessage", lastMessage.getId());
        rep.put("unreadCount", unreadCount);

        return rep;
    }
}
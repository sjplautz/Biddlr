package ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.biddlr.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import classes.ChatMessage;
import classes.DatabaseManager;
import classes.Dialog;
import classes.LocalDateTimeWrapped;
import classes.User;

public class MessageFragment extends Fragment implements MessagesListAdapter.SelectionListener, MessageInput.InputListener,
        MessageInput.AttachmentsListener, MessageInput.TypingListener, MessagesListAdapter.OnLoadMoreListener, DateFormatter.Formatter {
    private static final String MESSAGE_FRAGMENT_KEY = "message_fragment_key";
    private static final int TOTAL_MESSAGES_COUNT = 100;

    private String dialogID;
    private User user;

    protected final String senderId = DatabaseManager.shared.currentUser.getId();
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<ChatMessage> messagesAdapter;
    private MessagesList messagesList;

    private Menu menu;
    private int selectionCount;
    private Timestamp lastLoadedDate;

    public static MessageFragment newInstance(String dialogId) {
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE_FRAGMENT_KEY, dialogId);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogID = getArguments().getString(MESSAGE_FRAGMENT_KEY);

        DatabaseManager.shared.dialogsRef.document(dialogID).collection("messages").orderBy("createdAt", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("CHAT MESSAGE", "listen:error", e);
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    ChatMessage m = ChatMessage.parse(dc.getDocument());
                    switch (dc.getType()) {
                        case REMOVED:
                            Log.d("CHAT MESSAGE", "Removed message: " + m.getText());
                            messagesAdapter.addToStart(m, true);
                            break;
                        case MODIFIED:
                            Log.d("CHAT MESSAGE", "Modified message: " + m.getText());
                            break;
                        case ADDED:
                            Log.d("CHAT MESSAGE", "New message: " + m.getText());
                            messagesAdapter.addToStart(m, true);
                            break;
                    }
                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        messagesList = v.findViewById(R.id.messagesList);

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                DatabaseManager.shared.setUserImage(url, imageView);
            }
        };

        initAdapter();

        MessageInput input = v.findViewById(R.id.input);
        input.setInputListener(this);
        input.setTypingListener(this);
        input.setAttachmentsListener(this);

        return v;
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        messagesAdapter.addToStart(MessagesFixtures.getTextMessage(), true);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        this.menu = menu;
//        getMenuInflater().inflate(R.menu.chat_actions_menu, menu);
//        onSelectionChanged(0);
//        return true;
//    }

    public void onBackPressed() {
        if (selectionCount == 0) {
//            super.onBackPressed();
        } else {
            messagesAdapter.unselectAllItems();
        }
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        Log.i("TAG", "onLoadMore: " + page + " " + totalItemsCount);
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            loadMessages();
        }
    }

    @Override
    public void onSelectionChanged(int count) {
        this.selectionCount = count;
//        menu.findItem(R.id.action_delete).setVisible(count > 0);
//        menu.findItem(R.id.action_copy).setVisible(count > 0);
    }

    protected void loadMessages() {
        new Handler().postDelayed(new Runnable() { //imitation of internet connection
            @Override
            public void run() {

//                ArrayList<ChatMessage> messages = DatabaseManager.shared.getMessages();//lastLoadedDate);
//                lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
//                messagesAdapter.addToEnd(messages, false);
            }
        }, 1000);
    }

    private MessagesListAdapter.Formatter<ChatMessage> getMessageStringFormatter() {
        return new MessagesListAdapter.Formatter<ChatMessage>() {
            @Override
            public String format(ChatMessage message) {
                String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                        .format(message.getCreatedAt());

                String text = message.getText();
                if (text == null) text = "[attachment]";

                return String.format(Locale.getDefault(), "%s: %s (%s)",
                        message.getUser().getName(), text, createdAt);
            }
        };
    }

    @Override
    public boolean onSubmit(CharSequence input) {
//        messagesAdapter.addToStart(
                DatabaseManager.shared.addNewMessage(dialogID, input.toString());
//                , true);
        return true;
    }

    @Override
    public void onAddAttachments() {
//        super.messagesAdapter.addToStart(
//                MessagesFixtures.getImageMessage(), true);
    }

    private void initAdapter() {
        messagesAdapter = new MessagesListAdapter<>(senderId, imageLoader);
        messagesAdapter.enableSelectionMode(this);
        messagesAdapter.setLoadMoreListener(this);
        messagesAdapter.setDateHeadersFormatter(this);
        messagesAdapter.registerViewClickListener(R.id.messageUserAvatar,
                new MessagesListAdapter.OnMessageViewClickListener<ChatMessage>() {
                    @Override
                    public void onMessageViewClick(View view, ChatMessage message) {
//                        Toast.makeText(getActivity(), message.getUser() + " avatar click", Toast.LENGTH_LONG).show();
//                        Fragment userFrag = UserProfileFragment.newInstance(message.getUser());
//                        FragmentManager manager = getFragmentManager();
//                        FragmentTransaction trans = manager.beginTransaction();
//                        trans.replace(R.id.frameNull, userFrag);
//                        trans.addToBackStack(null);
//                        trans.commit();
                    }
                });
        this.messagesList.setAdapter(messagesAdapter);
    }

    @Override
    public void onStartTyping() {
        Log.v("Typing listener", "Start typing");
    }

    @Override
    public void onStopTyping() {
        Log.v("Typing listener", "Stop typing");
    }

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return "Today";
        } else if (DateFormatter.isYesterday(date)) {
            return "Yesterday";
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }

}
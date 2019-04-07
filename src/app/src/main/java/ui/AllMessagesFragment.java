package ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.biddlr.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.core.view.Change;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.List;

import adapters.ListTouchListener;
import classes.ChatMessage;
import classes.DatabaseManager;
import classes.Dialog;
import classes.Job;
import classes.User;
import interfaces.DialogDataListener;

public class AllMessagesFragment extends Fragment implements DialogsListAdapter.OnDialogClickListener<Dialog>, DialogsListAdapter.OnDialogLongClickListener<Dialog> {
    private  DialogsListAdapter dialogsAdapter;
    private DialogsList dialogsList;
    private ImageLoader imageLoader;
    private ListenerRegistration channelListener;

    public static AllMessagesFragment newInstance() {
        Bundle bundle = new Bundle();
        AllMessagesFragment fragment = new AllMessagesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseManager.shared.dialogsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String id = doc.getString("id");
                        String dialogName = doc.getString("dialogName");
                        String dialogPhoto = doc.getString("dialogPhoto");
//                                ArrayList<String> userIds = new ArrayList<String>();
//                                userIds = (ArrayList<String>) doc.get("userIds");
                        ArrayList<User> users = new ArrayList<User>();
                        users.add(DatabaseManager.shared.currentUser);
                        ChatMessage lastMessage = new ChatMessage(doc.getString("lastMessage"), DatabaseManager.shared.currentUser, "This is the last message");
                        int unreadCount = doc.getLong("unreadCount").intValue();
                        Dialog d = new Dialog(id, dialogName, dialogPhoto, users, lastMessage, unreadCount);
                        onNewDialog(d);
                        Log.d("ALL MESSAGES", "dialog: " + d);
                    }
                } else {
                    Log.d("ALL MESSAGES", "Error getting documents: ", task.getException());
                }
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_messages, container, false);
        dialogsList = v.findViewById(R.id.dialogsList);

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
//                Picasso.with(DemoDialogsActivity.this).load(url).into(imageView);
            }
        };

        initAdapter();
        return v;
    }

    public void onDialogClick(Dialog dialog) {
//        DefaultMessagesActivity.open(this);
//        Job job = jobs.get(pos);
        Fragment messageFrag = MessageFragment.newInstance();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.replace(R.id.frameNull, messageFrag);
        trans.addToBackStack(null);
        trans.commit();
    }

    @Override
    public void onDialogLongClick(Dialog dialog) {
        Toast.makeText(getActivity(), dialog.getDialogName(), Toast.LENGTH_LONG).show();
    }

    private void initAdapter() {
        dialogsAdapter = new DialogsListAdapter<>(imageLoader);
        dialogsAdapter.setItems(DatabaseManager.shared.getDialogs());

        dialogsAdapter.setOnDialogClickListener(this);
        dialogsAdapter.setOnDialogLongClickListener(this);

        dialogsList.setAdapter(dialogsAdapter);
    }

    //for example
    private void onNewMessage(String dialogId, ChatMessage message) {
        boolean isUpdated = dialogsAdapter.updateDialogWithMessage(dialogId, message);
        if (!isUpdated) {
            //Dialog with this ID doesn't exist, so you can create new Dialog or update all dialogs list
        }
    }

    //for example
    private void onNewDialog(Dialog dialog) {
        dialogsAdapter.addItem(dialog);
    }

//    private void handleDocumentChanges(List<DocumentChange> changes) {
//        while (!changes.isEmpty()) {
//            DocumentChange change = changes.remove(0);
//            Dialog d = new Dialog(change.getDocument());
//            switch (change.getType()) {
//                case ADDED:
//                    addChannelToTable(channel);
//                case MODIFIED:
//                    updateChannelInTable(channel);
//                case REMOVED:
//                    removeChannelFromTable(channel);
//        }
//    }

}

//    @Override
//    public void newDataReceived(Dialog dialog) {
//        dialogsAdapter.addItem(dialog);
//    }
//
//    @Override
//    public void dataChanged(Dialog dialog) { }
//
//    @Override
//    public void dataRemoved(Dialog dialog) { }


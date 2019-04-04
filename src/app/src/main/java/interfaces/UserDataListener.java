package interfaces;

import classes.User;

/**
 * Fragments implement this interface to receive updates to data from Firebase user listeners
 */
public interface UserDataListener {
    void newDataReceived(User user);
}

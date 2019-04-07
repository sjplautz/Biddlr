package interfaces;

import classes.Dialog;

/**
 * Fragments implement this interface to receive updates to data from Firebase job listeners
 */
public interface DialogDataListener {
    void newDataReceived(Dialog dialog);
    void dataChanged(Dialog dialog);
    void dataRemoved(Dialog dialog);
}
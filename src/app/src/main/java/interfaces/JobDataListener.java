package interfaces;

import classes.Job;

/**
 * Fragments implement this interface to receive updates to data from Firebase job listeners
 */
public interface JobDataListener {
    void newDataReceived(Job job);
    void dataChanged(Job job);
    void dataRemoved(Job job);
}

package interfaces;

import classes.Job;

public interface JobDataListener {
    void newDataReceived(Job job);
    void dataRemoved(Job job);
}

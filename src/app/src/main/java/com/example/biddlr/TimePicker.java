package com.example.biddlr;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Creates a dialog which displays a clock
 * for the purpose of selecting the time.
 */
public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    //TODO make this method generic... somehow
    /**
     * Creates the dialog to display and sets the time to the current time in the
     * job creation TextView
     * @param savedInstance The last saved instance state of the dialog
     * @return The dialog instance to be displayed
     */
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstance){
        TextView txtTime = getActivity().findViewById(R.id.txtTime);
        String time = txtTime.getText().toString();
        LocalTime currTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a"));

        int hour = currTime.getHour();
        int min = currTime.getMinute();

        return new TimePickerDialog(getActivity(), this, hour, min, DateFormat.is24HourFormat(getActivity()));
    }

    //TODO make this method generic too... somehow
    /**
     * Sets the job creation TextView to the selected time when the user selects ok
     * @param view The TimePicker that is setting the time
     * @param hourOfDay The hour of day (24 hour time)
     * @param minute The minute of the hour
     */
    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        LocalTime selectedTime = LocalTime.of(hourOfDay, minute);

        //TODO Add warning if time is set past current time and date is current date

        TextView txtTime = getActivity().findViewById(R.id.txtTime);

        String time = selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
        txtTime.setText(time);
    }
}

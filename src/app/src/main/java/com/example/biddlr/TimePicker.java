package com.example.biddlr;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        TextView txtTime = getActivity().findViewById(R.id.txtTime);
        String time = txtTime.getText().toString();
        LocalTime currTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("hh:mm a"));

        int hour = currTime.getHour();
        int min = currTime.getMinute();

        return new TimePickerDialog(getActivity(), this, hour, min, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        LocalTime selectedTime = LocalTime.of(hourOfDay, minute);

        //TODO Add warning if time is set past current time and date is current date

        TextView txtTime = getActivity().findViewById(R.id.txtTime);

        String time = selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
        txtTime.setText(time);
    }
}

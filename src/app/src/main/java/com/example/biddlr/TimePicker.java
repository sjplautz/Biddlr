package com.example.biddlr;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        final Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, min, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        TextView txtTime = getActivity().findViewById(R.id.txtTime);

        int hour12 = hourOfDay > 12 ? hourOfDay - 12 : hourOfDay;
        char m = hourOfDay > 11 ? 'P' : 'A';

        String time = String.format(Locale.ENGLISH, "%d:%02d %cM", hour12, minute, m);
        txtTime.setText(time);
    }
}

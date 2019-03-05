package com.example.biddlr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TextView txtDate = getActivity().findViewById(R.id.txtDate);
        String date = txtDate.getText().toString();
        LocalDate currDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/uuuu"));

        int year = currDate.getYear();
        int month = currDate.getMonthValue() - 1; //Sub 1 because DatePickerDialog takes month index, not month value
        int day = currDate.getDayOfMonth();

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        //Add 1 because parameter gives month index, not month value
        LocalDate selectedDate = LocalDate.of(year, month + 1, day);

        //TODO Add warning if time is past current time and disable submission button
        //TODO Do the same for current error

        TextView txtDate = getActivity().findViewById(R.id.txtDate);
        //Add 1 because parameter gives month index, not month value
        String date = selectedDate.format(DateTimeFormatter.ofPattern("MM/dd/uuuu"));
        txtDate.setText(date);
    }
}

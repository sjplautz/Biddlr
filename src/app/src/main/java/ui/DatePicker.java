package ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

import com.example.biddlr.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Creates a dialog which displays a calendar
 * for the purpose of selecting a date.
 */
public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    //TODO make this method generic... somehow
    /**
     * Creates the dialog to display and sets the date to the current date in the
     * job creation TextView
     * @param savedInstanceState The last saved instance state of the dialog
     * @return The dialog instance to be displayed
     */
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TextView txtDate = getActivity().findViewById(R.id.txtDate);
        String date = txtDate.getText().toString();
        LocalDate currDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/uuuu"));

        //Sub 1 from month because DatePickerDialog takes month index, not month value
        int year = currDate.getYear();
        int month = currDate.getMonthValue() - 1;
        int day = currDate.getDayOfMonth();

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    //TODO make this method generic too... somehow
    /**
     * Sets the job creation TextView to the selected date when the user selects ok
     * @param view The DatePicker that is setting the date
     * @param year The year that the user selects
     * @param month The index of the month that the user selects
     * @param day The day of the month that the user selects
     */
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

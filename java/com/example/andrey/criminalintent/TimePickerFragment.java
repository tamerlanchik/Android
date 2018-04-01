package com.example.andrey.criminalintent;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by PC on 17.03.2018.
 */

public class TimePickerFragment extends DialogFragment implements DialogInterface.OnClickListener{
    private static final String ARG_TIME = "time";
    public static final String EXTRA_TIME = "com.andrey.criminalintent.time";
    private TimePicker mTimePicker;
    private Date date;
    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        int minute = mTimePicker.getCurrentMinute();
        int hour = mTimePicker.getCurrentHour();
        Date dat = new Date();
        date.setHours(hour);
        date.setMinutes(minute);
        sendResult(Activity.RESULT_OK, date);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        date = (Date) getArguments().getSerializable(ARG_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        mTimePicker = v.findViewById(R.id.dialog_time_picker);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minutes);
        mTimePicker.setIs24HourView(true);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Time of crime:")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int minute = mTimePicker.getCurrentMinute();
                        int hour = mTimePicker.getCurrentHour();
                        Date dat = new Date();
                        date.setHours(hour);
                        date.setMinutes(minute);
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date time) {
        if(getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);
        getTargetFragment().onActivityResult(getTargetRequestCode(),
                                             resultCode, intent);
        Log.d("RESULT", Integer.toString(resultCode));
    }
}

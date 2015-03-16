package com.example.conor.senan.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.conor.senan.R;

import java.util.Calendar;

/**
 * Created by Conor on 03/02/2015.
 */
public class DialogUtils {


    public static Dialog getTimeDateDialog(Context context){

        View view = View.inflate(context, R.layout.custom_date_time_picker, null);
        final View datePickerButton = view.findViewById(R.id.set_date_button);
        final View timePickerButton = view.findViewById(R.id.set_time_button);

        final View datePicker = view.findViewById(R.id.date_picker);
        final View timePicker = view.findViewById(R.id.time_picker);

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerButton.setEnabled(false);
                datePicker.setVisibility(View.VISIBLE);

                timePickerButton.setEnabled(true);
                timePicker.setVisibility(View.GONE);
            }
        });

        timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerButton.setEnabled(true);
                datePicker.setVisibility(View.GONE);

                timePickerButton.setEnabled(false);
                timePicker.setVisibility(View.VISIBLE);
            }
        });


        Dialog dialog = new Dialog(context);
        dialog.setContentView(view);

        return dialog;

    }


    public static long getTimeStampFromPickers(View datePicker, View timePicker){

        DatePicker date = (DatePicker)datePicker;
        TimePicker time = (TimePicker)timePicker;

        final Calendar c = Calendar.getInstance();
        c.set(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getCurrentHour(), time.getCurrentMinute(), 0);
        return c.getTimeInMillis();

    }

}

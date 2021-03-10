package com.musleep.Musleep;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.util.Calendar;

public class init_time extends AppCompatActivity {

    EditText timeHour,timeHour1;
    EditText timeMinute,timeMinute1;
    LinearLayout setTime, setTime1;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_time);
        //起床
        timeHour = findViewById(R.id.timeHour);
        timeMinute = findViewById(R.id.timeMinute);
        setTime = findViewById(R.id.setTime);
        //就寢
        timeHour1 = findViewById(R.id.timeHour1);
        timeMinute1 = findViewById(R.id.timeMinute1);
        setTime1 = findViewById(R.id.setTime1);
        //起床
        setTime.setOnClickListener((v) -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(init_time.this,(timePicker, hourOfDay, minutes) -> {
                timeHour.setText(String.format("%02d",hourOfDay));
                timeMinute.setText(String.format("%02d",minutes));
            },currentHour, currentMinute, false);

            timePickerDialog.show();
        });
        //就寢
        setTime1.setOnClickListener((v) -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(init_time.this,(timePicker, hourOfDay1, minutes1) -> {
                timeHour1.setText(String.format("%02d",hourOfDay1));
                timeMinute1.setText(String.format("%02d",minutes1));
            },currentHour, currentMinute, false);

            timePickerDialog.show();
        });
    }
}
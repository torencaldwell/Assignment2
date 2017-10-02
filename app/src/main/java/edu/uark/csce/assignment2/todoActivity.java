package edu.uark.csce.assignment2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class todoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    Button submitButton;
    int year,month,day,hour,minute;
    Calendar calendar;

    TextView datetime, title, description;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        calendar = Calendar.getInstance();

        submitButton = (Button)findViewById(R.id.submitButton);

        datetime = (TextView)findViewById(R.id.datetime);
        title = (TextView)findViewById(R.id.todoTitle);
        description = (TextView)findViewById(R.id.todoDescription);

        checkBox = (CheckBox)findViewById(R.id.doneBox);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        datetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    public void onDateSet(DatePicker view, int _year, int _month, int _day){
        DialogFragment timeFragment = new TimePickerFragment();
        timeFragment.show(getFragmentManager(), "timePicker");

        year = _year;
        month = _month;
        day = _day;
    }

    public void onTimeSet(TimePicker view, int _hour, int _minute){
        hour = _hour;
        minute = _minute;

        SimpleDateFormat sdf = new SimpleDateFormat("EE d MMM yyyy h:MM a");
        calendar.set(year, month, day, hour, minute);
        datetime.setText(sdf.format(calendar.getTime()));

    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}


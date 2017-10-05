package edu.uark.csce.assignment2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class todoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    Button submitButton;
    int year,month,day,hour,minute;
    int ID;
    Calendar calendar;

    EditText title, description;
    TextView datetime;
    CheckBox checkBox;

    boolean edit = false;

    Cursor mCursor;

    ContentValues newValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        String[] mProjection = {ToDoProvider.TODO_TABLE_COL_TITLE, ToDoProvider.TODO_TABLE_COL_DESCRIPTION, ToDoProvider.TODO_TABLE_COL_DATE};
        String mSelectionClause = null;
        String[] mSelectionArgs = {""};

       // mCursor = getContentResolver().query(ToDoProvider.CONTENT_URI, mProjection, mSelectionClause, mSelectionArgs, null);
        newValues = new ContentValues();

        calendar = Calendar.getInstance();

        submitButton = (Button)findViewById(R.id.submitButton);

        datetime = (TextView)findViewById(R.id.datetime);
        title = (EditText)findViewById(R.id.todoTitle);
        description = (EditText)findViewById(R.id.todoDescription);

        checkBox = (CheckBox)findViewById(R.id.doneBox);

        Bundle bundle = getIntent().getExtras();

        int itemIndex = bundle.getInt("index");
        if(itemIndex != -1) {
            queryList(itemIndex);
            edit = true;
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleString = title.getText().toString();
                String descString  = description.getText().toString();
                String date = datetime.getText().toString();
                int done;
                if(checkBox.isChecked())
                    done = 1;
                else
                    done = 0;

                newValues.put(ToDoProvider.TODO_TABLE_COL_TITLE, titleString);
                newValues.put(ToDoProvider.TODO_TABLE_COL_DESCRIPTION, descString);
                newValues.put(ToDoProvider.TODO_TABLE_COL_DATE, date);
                newValues.put(ToDoProvider.TODO_TABLE_COL_DONE, done);

                if(!edit) {
                    Uri newUri = getContentResolver().insert(ToDoProvider.CONTENT_URI, newValues);
                }else{
                    String mSelectionClause = ToDoProvider.TODO_TABLE_COL_ID + " = ?";
                    String[] mSelectionArgs = {Integer.toString(ID)};

                    //newValues.putNull(ToDoProvider.TODO_TABLE_COL_ID);

                    int mRowsUpdated = getContentResolver().update(ToDoProvider.CONTENT_URI, newValues, mSelectionClause, mSelectionArgs);
                }
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

    public void queryList(int index){

        mCursor = getContentResolver().query(ToDoProvider.CONTENT_URI, null, null, null, null);

        if(mCursor != null){
            mCursor.moveToPosition(index);

            int column = mCursor.getColumnIndex(ToDoProvider.TODO_TABLE_COL_TITLE);
            title.setText(mCursor.getString(column));

            column = mCursor.getColumnIndex(ToDoProvider.TODO_TABLE_COL_DESCRIPTION);
            description.setText(mCursor.getString(column));

            column = mCursor.getColumnIndex(ToDoProvider.TODO_TABLE_COL_DATE);
            datetime.setText(mCursor.getString(column));

            column = mCursor.getColumnIndex(ToDoProvider.TODO_TABLE_COL_ID);
            ID = mCursor.getInt(column);

            column = mCursor.getColumnIndex(ToDoProvider.TODO_TABLE_COL_DONE);
            int done = mCursor.getInt(column);

            if(done == 1)
                checkBox.setChecked(true);
            else
                checkBox.setChecked(false);
        }



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


package edu.uark.csce.assignment2;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Date;

public class todoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    Button submitButton;
    int year,month,day,hour,minute;
    int ID, itemIndex;
    Calendar calendar;

    EditText title, description;
    TextView datetime;
    CheckBox checkBox;

    boolean edit = false;

    Cursor mCursor;

    ContentValues newValues;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        String[] mProjection = {ToDoProvider.TODO_TABLE_COL_TITLE, ToDoProvider.TODO_TABLE_COL_DESCRIPTION, ToDoProvider.TODO_TABLE_COL_DATE};
        String mSelectionClause = null;
        String[] mSelectionArgs = {""};

        newValues = new ContentValues();

        calendar = Calendar.getInstance();

        submitButton = (Button)findViewById(R.id.submitButton);

        datetime = (TextView)findViewById(R.id.datetime);
        title = (EditText)findViewById(R.id.todoTitle);
        description = (EditText)findViewById(R.id.todoDescription);

        checkBox = (CheckBox)findViewById(R.id.doneBox);

        bundle = getIntent().getExtras();

        itemIndex = bundle.getInt("index");
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
                    getContentResolver().insert(ToDoProvider.CONTENT_URI, newValues);
                }else{
                    cancelNotification(getBaseContext(), 0);
                    String mSelectionClause = ToDoProvider.TODO_TABLE_COL_ID + " = ?";
                    String[] mSelectionArgs = {Integer.toString(ID)};

                    getContentResolver().update(ToDoProvider.CONTENT_URI, newValues, mSelectionClause, mSelectionArgs);
                }
                scheduleNotification(getBaseContext(), calendar.getTimeInMillis(), 0);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_delete:
                cancelNotification(getBaseContext(), 0);
                Uri uri_id = Uri.withAppendedPath(ToDoProvider.CONTENT_URI, Integer.toString(ID));
                getContentResolver().delete(uri_id, null, null);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        SimpleDateFormat sdf = new SimpleDateFormat("EE d MMM yyyy h:mm a");
        calendar.set(year, month, day, hour, minute, 0);
        datetime.setText(sdf.format(calendar.getTime()));

    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }

    public void scheduleNotification(Context context, long alarmTime, int notificationId){
        long[] vibrationPattern = {500,250,1000};

        //Builds the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("Todo")
                .setContentText(title.getText())
                .setSmallIcon(R.drawable.ic_timelapse_white_24dp)
                .setVibrate(vibrationPattern);

        //Intent that is fired when the notification is clicked
        Intent activity = new Intent(getBaseContext(), todoActivity.class);
        activity.putExtra("index", bundle.getInt("index"));

        //A pending intent for the activity intent above
        PendingIntent pendActivity = PendingIntent.getActivity(getBaseContext(), notificationId, activity, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendActivity);

        Notification notification = builder.build();

        //Creates an intent that calls the alarm broadcast receiver
        Intent notificationIntent = new Intent(getBaseContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);

        //A pending intent for the alarm broadcast receiver
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Sets the alarm which calls the broadcast receiver intent
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);

        Date date = new Date(alarmTime);
        String dateString = date.toString();

        Log.i("Set Alarm", dateString);
        Log.i("Alarm ID", Integer.toString(itemIndex));

    }

    public void cancelNotification(Context context, int notificationId){
        Intent notificationIntent = new Intent(getBaseContext(), NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);

        Log.i("Alarm Cancelled", Integer.toString(itemIndex));
    }



}


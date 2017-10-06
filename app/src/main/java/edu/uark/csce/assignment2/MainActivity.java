package edu.uark.csce.assignment2;

import android.app.ListActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Cursor mCursor;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Starts the broadcast reciever to listen for network changes
        BroadcastReceiver networkReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkReceiver, filter);

        //Items are stored in a listview
        listView = (ListView)findViewById(R.id.itemsList);

        //Floating Action Button is used to add items to the list
        FloatingActionButton add_fab = (FloatingActionButton) findViewById(R.id.fab);
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), todoActivity.class);
                intent.putExtra("index", -1);
                startActivity(intent);
            }
        });

        //OnClickListener for each menu item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //pass list object into todoActivity so data can be shown and updated
                Intent intent = new Intent(getApplicationContext(), todoActivity.class);
                intent.putExtra("index", position);

                startActivity(intent);
            }
        });

        queryList();
        refreshList();
    }

    //Queries the SQLite database
    public void queryList(){
        String[] mProjection = {ToDoProvider.TODO_TABLE_COL_TITLE, ToDoProvider.TODO_TABLE_COL_DESCRIPTION, ToDoProvider.TODO_TABLE_COL_DATE, ToDoProvider.TODO_TABLE_COL_ID};
        String mSelectionClause = null;
        String[] mSelectionArgs = {""};

        mCursor = getContentResolver().query(ToDoProvider.CONTENT_URI, null, null, null, null);
    }

    //Populates listView with items from SQLite DB
    public void refreshList(){
        ArrayList<String> listItems = new ArrayList<>();

        if(mCursor != null){
            while(mCursor.moveToNext()){
                //Get title field
                int index = mCursor.getColumnIndex(ToDoProvider.TODO_TABLE_COL_TITLE);
                String item = mCursor.getString(index);

                //Get Done field
                index = mCursor.getColumnIndex(ToDoProvider.TODO_TABLE_COL_DONE);
                int done = mCursor.getInt(index);

                //If item is marked as done, note it accordingly
                if(done == 1)
                    item += " (Done)";

                listItems.add(item);
            }
        }else{
            Log.i("Query", "Error: mCursor is null");
        }

        //ArrayAdapter that is used to put items into the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, listItems);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();

        //Refresh the list every time the activity resumes
        queryList();
        refreshList();
    }
}

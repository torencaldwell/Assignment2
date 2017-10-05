package edu.uark.csce.assignment2;

import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

        listView = (ListView)findViewById(R.id.itemsList);  //TODO: change to recylerview

        FloatingActionButton add_fab = (FloatingActionButton) findViewById(R.id.fab);
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), todoActivity.class);
                intent.putExtra("index", -1);
                startActivity(intent);
            }
        });

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

    public void queryList(){
        String[] mProjection = {ToDoProvider.TODO_TABLE_COL_TITLE, ToDoProvider.TODO_TABLE_COL_DESCRIPTION, ToDoProvider.TODO_TABLE_COL_DATE};
        String mSelectionClause = null;
        String[] mSelectionArgs = {""};

        mCursor = getContentResolver().query(ToDoProvider.CONTENT_URI, null, null, null, null);
    }

    public void refreshList(){
        ArrayList<String> listItems = new ArrayList<>();

        if(mCursor != null){
            while(mCursor.moveToNext()){
                int index = mCursor.getColumnIndex(ToDoProvider.TODO_TABLE_COL_TITLE);
                String item = mCursor.getString(index);

                index = mCursor.getColumnIndex(ToDoProvider.TODO_TABLE_COL_DONE);
                int done = mCursor.getInt(index);
                if(done == 1)
                    item += " (Done)";

                listItems.add(item);
            }
        }else{
            Log.i("Query", "Error: mCursor is null");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, listItems);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i("MainActivity", "Activity Paused");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i("MainActivity", "Activity is Resuming...");
        queryList();
        refreshList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

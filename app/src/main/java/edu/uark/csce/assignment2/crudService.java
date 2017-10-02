package edu.uark.csce.assignment2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.IBinder;
import android.provider.BaseColumns;

public class crudService extends Service {
    public crudService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){

    }

    @Override
    public void onDestroy(){

    }


    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
            FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," + FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + "TEXT," + FeedReaderContract.FeedEntry.COLUMN_DESCRIPTION_TITLE
            + "TEXT," + FeedReaderContract.FeedEntry.COLUMN_DATE_TITLE + "TEXT," + FeedReaderContract.FeedEntry.COLUMN_TIME_TITLE + "TEXT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    public class FeedReaderDbHelper extends SQLiteOpenHelper{
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "FeedReader.db";

        public FeedReaderDbHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db){
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}

final class FeedReaderContract{
    private FeedReaderContract(){}

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "TodoList";
        public static final String COLUMN_NAME_TITLE = "Title";
        public static final String COLUMN_DESCRIPTION_TITLE = "Description";
        public static final String COLUMN_DATE_TITLE = "date";
        public static final String COLUMN_TIME_TITLE = "time";
    }
}


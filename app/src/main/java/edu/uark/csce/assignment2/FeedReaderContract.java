package edu.uark.csce.assignment2;

import android.provider.BaseColumns;

/**
 * Created by tccaldwe on 10/2/2017.
 */


public final class FeedReaderContract{
    private FeedReaderContract(){}

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "TodoList";
        public static final String COLUMN_NAME_TITLE = "Title";
        public static final String COLUMN_DESCRIPTION_TITLE = "Description";
        public static final String COLUMN_DATE_TITLE = "date";
        public static final String COLUMN_TIME_TITLE = "time";
    }
}
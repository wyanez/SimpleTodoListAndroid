package com.wyanez.simpletodolist.db;

import android.provider.BaseColumns;

public final class DbContract {

    private DbContract(){}

    public static final String SQL_CREATE_TASK =
            "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" +
                    TaskEntry._ID + " INTEGER PRIMARY KEY," +
                    TaskEntry.COLUMN_TITLE + " TEXT  NOT NULL," +
                    TaskEntry.COLUMN_DESCRIPTION + " TEXT  NOT NULL," +
                    TaskEntry.COLUMN_TAGS + " TEXT NOT NULL," +
                    TaskEntry.COLUMN_PRIORITY + " INTEGER  DEFAULT 1," +
                    TaskEntry.COLUMN_DEADLINE + " TEXT NOT NULL," +
                    TaskEntry.COLUMN_ACTIVE + " INTEGER  DEFAULT 0)";

    public static final String SQL_DELETE_TASK =
            "DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME;


    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TAGS = "tags";
        public static final String COLUMN_PRIORITY = "priority";
        public static final String COLUMN_DEADLINE = "deadline";
        public static final String COLUMN_ACTIVE = "active";

    }
}

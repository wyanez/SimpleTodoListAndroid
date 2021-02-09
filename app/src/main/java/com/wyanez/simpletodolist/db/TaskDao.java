package com.wyanez.simpletodolist.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wyanez.simpletodolist.model.Task;

public class TaskDao {

    private DbHelper dbHelper;

    public TaskDao(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insert(Task task){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.TaskEntry.COLUMN_TITLE,task.getTitle());
        values.put(DbContract.TaskEntry.COLUMN_DESCRIPTION,task.getDescription());
        values.put(DbContract.TaskEntry.COLUMN_TAGS,task.getTags());
        values.put(DbContract.TaskEntry.COLUMN_PRIORITY,task.getPriority());
        values.put(DbContract.TaskEntry.COLUMN_DEADLINE,task.getDeadlineYMD());
        values.put(DbContract.TaskEntry.COLUMN_ACTIVE,task.isActive() ? 1 : 0);

        Log.d("Task/insert",values.toString());
        long newId = -1;
        try {
            newId = db.insert(DbContract.TaskEntry.TABLE_NAME, null, values);
            Log.d("Task/Insert", String.format("Task Insert %d sucesfully!",newId));
        }
        catch(Exception ex){
            Log.d("Task/Insert", "ERROR: " + ex.toString());
            newId = -1;
        }
        finally{
            db.close();
        }
        return newId;
    }
}

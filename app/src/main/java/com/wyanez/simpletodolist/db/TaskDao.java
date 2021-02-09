package com.wyanez.simpletodolist.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wyanez.simpletodolist.model.Task;
import com.wyanez.simpletodolist.util.Utilities;

import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    private DbHelper dbHelper;
    private SQLiteDatabase db;

    private int columnId;
    private int columnTitle;
    private int columnDescription;
    private int columnTags;
    private int columnPriority;
    private int columnDeadline;
    private int columnActive;


    public TaskDao(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insert(Task task){
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.TaskEntry.COLUMN_TITLE,task.getTitle());
        values.put(DbContract.TaskEntry.COLUMN_DESCRIPTION,task.getDescription());
        values.put(DbContract.TaskEntry.COLUMN_TAGS,task.getTags());
        values.put(DbContract.TaskEntry.COLUMN_PRIORITY,task.getPriority());
        values.put(DbContract.TaskEntry.COLUMN_DEADLINE,task.getDeadlineYMD());
        values.put(DbContract.TaskEntry.COLUMN_ACTIVE,task.isActive() ? 1 : 0);

        Log.d("Task/insert",values.toString());
        long newId;
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

    private void initColumns(Cursor cursor) {
        columnId = cursor.getColumnIndex(DbContract.TaskEntry._ID);
        columnTitle = cursor.getColumnIndex(DbContract.TaskEntry.COLUMN_TITLE);
        columnDescription = cursor.getColumnIndex(DbContract.TaskEntry.COLUMN_DESCRIPTION);
        columnTags = cursor.getColumnIndex(DbContract.TaskEntry.COLUMN_TAGS);
        columnPriority = cursor.getColumnIndex(DbContract.TaskEntry.COLUMN_PRIORITY);
        columnDeadline = cursor.getColumnIndex(DbContract.TaskEntry.COLUMN_DEADLINE);
        columnActive = cursor.getColumnIndex(DbContract.TaskEntry.COLUMN_ACTIVE);
    }

    public List<Task> listTaskActive(){
        db = dbHelper.getReadableDatabase();
        List<Task> listTasks = new ArrayList<>();

        String sql= String.format("SELECT * FROM %s WHERE %s=%d ORDER BY date(%s) ASC,priority ASC",
                DbContract.TaskEntry.TABLE_NAME,
                DbContract.TaskEntry.COLUMN_ACTIVE,1,
                DbContract.TaskEntry.COLUMN_DEADLINE);

        //String sql= String.format("SELECT * FROM %s", DbContract.TaskEntry.TABLE_NAME);

        Log.d("listTasks","SQL = " +sql);
        Cursor cursor = db.rawQuery(sql, null);
        initColumns(cursor);
        while(cursor.moveToNext()) {
            Task task = getTaskFromCursor(cursor);
            listTasks.add(task);
        }
        db.close();
        return listTasks;
    }

    private Task getTaskFromCursor(Cursor cursor) {
        Task task = new Task();
        task.setId(cursor.getInt(columnId));
        task.setTitle(cursor.getString(columnTitle));
        task.setDescription(cursor.getString(columnDescription));
        task.setTags(cursor.getString(columnTags));
        task.setPriority(cursor.getInt(columnPriority));
        task.setDeadline(Utilities.getCalendarFromString(cursor.getString(columnDeadline)));

        return task;
    }


}

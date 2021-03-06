package com.wyanez.simpletodolist.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.wyanez.simpletodolist.base.BaseDaoSQLite;
import com.wyanez.simpletodolist.base.IBaseDaoSQLite;
import com.wyanez.simpletodolist.model.Task;
import com.wyanez.simpletodolist.util.Utilities;

import java.util.List;

public class TaskDao extends BaseDaoSQLite<Task> implements IBaseDaoSQLite<Task> {
    private int columnId;
    private int columnTitle;
    private int columnDescription;
    private int columnTags;
    private int columnPriority;
    private int columnDeadline;
    private int columnActive;

    public TaskDao() {
        super();
        this.tableName = DbContract.TaskEntry.TABLE_NAME;
    }

    @Override
    protected void initColumns(Cursor cursor) {
        columnId = cursor.getColumnIndex(DbContract.TaskEntry._ID);
        columnTitle = cursor.getColumnIndex(DbContract.TaskEntry.COLUMN_TITLE);
        columnDescription = cursor.getColumnIndex(DbContract.TaskEntry.COLUMN_DESCRIPTION);
        columnTags = cursor.getColumnIndex(DbContract.TaskEntry.COLUMN_TAGS);
        columnPriority = cursor.getColumnIndex(DbContract.TaskEntry.COLUMN_PRIORITY);
        columnDeadline = cursor.getColumnIndex(DbContract.TaskEntry.COLUMN_DEADLINE);
        columnActive = cursor.getColumnIndex(DbContract.TaskEntry.COLUMN_ACTIVE);
    }

    @Override
    protected ContentValues getContentValues(Task task) {
        ContentValues values = new ContentValues();
        if(task.getId()>0) values.put(DbContract.TaskEntry._ID,task.getId());
        values.put(DbContract.TaskEntry.COLUMN_TITLE,task.getTitle());
        values.put(DbContract.TaskEntry.COLUMN_DESCRIPTION,task.getDescription());
        values.put(DbContract.TaskEntry.COLUMN_TAGS,task.getTags());
        values.put(DbContract.TaskEntry.COLUMN_PRIORITY,task.getPriority());
        values.put(DbContract.TaskEntry.COLUMN_DEADLINE,task.getDeadlineYMD());
        values.put(DbContract.TaskEntry.COLUMN_ACTIVE,task.isActive() ? 1 : 0);
        return values;
    }

    @Override
    protected Task getEntityFromCursor(Cursor cursor) {
        Task task = new Task();
        task.setId(cursor.getInt(columnId));
        task.setTitle(cursor.getString(columnTitle));
        task.setDescription(cursor.getString(columnDescription));
        task.setTags(cursor.getString(columnTags));
        task.setPriority(cursor.getInt(columnPriority));
        task.setDeadline(Utilities.getCalendarFromString(cursor.getString(columnDeadline)));
        task.setActive(cursor.getInt(columnActive)==1);
        return task;
    }

    @Override
    public List<Task> list() {
        String sql = String.format("SELECT * FROM %s WHERE %s=%d ORDER BY date(%s) ASC,priority ASC",
                DbContract.TaskEntry.TABLE_NAME,
                DbContract.TaskEntry.COLUMN_ACTIVE, 1,
                DbContract.TaskEntry.COLUMN_DEADLINE);

        return super.doList(sql);
    }

    @Override
    public long insert(Task task) {
        return doInsert(task);
    }

    @Override
    public int delete(long id) {
        return doDelete(id, DbContract.TaskEntry._ID);
    }

    @Override
    public int update(Task task) {
        return doUpdate(task, task.getId(), DbContract.TaskEntry._ID);
    }

    @Override
    public Task get(long id) {
        return doGet(id, DbContract.TaskEntry._ID);
    }
}

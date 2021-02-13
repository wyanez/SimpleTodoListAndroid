package com.wyanez.simpletodolist.base;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wyanez.simpletodolist.db.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Base Abstract Class for DAO
 *
 * @param <T> Type of entity
 */
public abstract class BaseDaoSQLite<T> {
    private DbHelper dbHelper;
    protected SQLiteDatabase db;
    protected String tableName;

    public void setDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    protected abstract ContentValues getContentValues(T entity);

    protected abstract T getEntityFromCursor(Cursor cursor);

    protected abstract void initColumns(Cursor cursor);

    protected long doInsert(T entity) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = getContentValues(entity);

        Log.d("Dao/insert", values.toString());
        long newId;
        try {
            newId = db.insert(tableName, null, values);
            Log.d("Dao/Insert", String.format("Insert %d sucesfully!", newId));
        } catch (Exception ex) {
            Log.d("Dao/Insert", "ERROR: " + ex.toString());
            newId = -1;
        } finally {
            db.close();
        }
        return newId;
    }

    protected int doUpdate(T entity, long id, String columnIdName) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = getContentValues(entity);
        Log.d("Dao/update", values.toString());

        String whereClause = String.format("%s = ?", columnIdName);
        String[] whereParams = {String.valueOf(id)};
        int rowsAffected = db.update(tableName, values, whereClause, whereParams);
        db.close();
        return rowsAffected;
    }

    protected int doDelete(long id, String columnIdName) {
        int rowsAffected = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = String.format("%s = ?", columnIdName);
        String[] whereParams = {String.valueOf(id)};
        try {
            rowsAffected = db.delete(tableName, whereClause, whereParams);
            Log.d("Dao/delete", "RESULT: " + rowsAffected);
        } catch (Exception ex) {
            Log.d("Dao/delete", "ERROR: " + ex.toString());
        }
        db.close();
        return rowsAffected;
    }

    protected List<T> doList(String sql) {
        db = dbHelper.getReadableDatabase();
        List<T> listEntities = new ArrayList<>();
        Log.d("Dao/List", "SQL = " + sql);
        Cursor cursor = db.rawQuery(sql, null);
        initColumns(cursor);
        while (cursor.moveToNext()) {
            T entity = getEntityFromCursor(cursor);
            listEntities.add(entity);
        }
        db.close();
        return listEntities;
    }

    //Optional<T> require API min 24, current 21
    protected T doGet(long id, String columnIdName) {
        db = dbHelper.getReadableDatabase();
        T entity = null;
        String sql = String.format("SELECT * FROM %s WHERE %s = %d", tableName, columnIdName, id);
        Log.d("Dao/Get", "SQL = " + sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            initColumns(cursor);
            entity = getEntityFromCursor(cursor);
        }
        db.close();
        return entity;  //Optional.ofNullable(entity) require API min 24, current 21
    }

}

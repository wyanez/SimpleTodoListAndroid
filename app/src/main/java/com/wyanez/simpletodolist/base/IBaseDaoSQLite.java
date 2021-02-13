package com.wyanez.simpletodolist.base;

import com.wyanez.simpletodolist.db.DbHelper;

import java.util.List;

public interface IBaseDaoSQLite<T> {

    long insert(T entity);

    int update(T entity);

    int delete(long id);

    List<T> list();

    T get(long id);

    void setDbHelper(DbHelper dbHelper);
}

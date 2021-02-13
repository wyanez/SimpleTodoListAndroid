package com.wyanez.simpletodolist.base;

import android.content.Context;

import com.wyanez.simpletodolist.db.DbHelper;
import com.wyanez.simpletodolist.util.IConsumerResult;

/**
 * Base Class for CRUD Service
 *
 * @param <T> Type of Result
 */

public abstract class BaseCrudService<T> {
    protected final Context context;
    protected IConsumerResult<T> consumerResult;

    public BaseCrudService(Context context, IConsumerResult<T> consumerResult) {
        this.context = context;
        this.consumerResult = consumerResult;
    }
}

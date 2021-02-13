package com.wyanez.simpletodolist.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.wyanez.simpletodolist.db.DbHelper;
import com.wyanez.simpletodolist.db.TaskDao;
import com.wyanez.simpletodolist.util.IConsumerResult;

/**
 * Base Class for CRUD Async Task
 *
 * @param <T> Input Type for doBackground method of AsyncTask
 * @param <R> Type of Result
 */
public abstract class BaseCrudTask<T, R> extends AsyncTask<T, Void, R> {
    protected Context context;
    private ProgressDialog loading;
    private String titleDialogLoading;
    protected TaskDao taskDao;
    private IConsumerResult<R> consumerResult;

    public BaseCrudTask(Context context, String titleDialogLoading) {
        this.titleDialogLoading = titleDialogLoading;
        this.context = context;

        DbHelper dbHelper = new DbHelper(context);
        taskDao = new TaskDao(dbHelper);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(context, this.titleDialogLoading, "Wait...", false, false);
    }

    @Override
    protected void onPostExecute(R result) {
        super.onPostExecute(result);
        Log.d("onPostExecute", result.toString());
        this.loading.dismiss();
        if (this.consumerResult != null) this.consumerResult.process(result);
        else Log.d("onPostExecute", "WARNING: ConsumerResult is null");

    }

    public void setProcessResult(IConsumerResult<R> consumerResult) {
        this.consumerResult = consumerResult;
    }
}

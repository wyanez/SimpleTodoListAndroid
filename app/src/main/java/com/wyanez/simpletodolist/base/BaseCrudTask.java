package com.wyanez.simpletodolist.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.wyanez.simpletodolist.db.DbHelper;
import com.wyanez.simpletodolist.util.IConsumerResult;

/**
 * Base Class for CRUD Async Task
 *
 * @param <T> Input Type for doBackground method of AsyncTask
 * @param <R> Type of Result
 * @param <TDao> Type of Dao required for the task
 */
public abstract class BaseCrudTask<T, R, TDao extends IBaseDaoSQLite> extends AsyncTask<T, Void, R> {
    protected Context context;
    private ProgressDialog loading;
    private String titleDialogLoading;
    private IConsumerResult<R> consumerResult;
    protected TDao dao;

    public BaseCrudTask(Context context, String titleDialogLoading) {
        this.titleDialogLoading = titleDialogLoading;
        this.context = context;
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

    public void setDao(TDao daoInstance) {
        this.dao = daoInstance;
        this.dao.setDbHelper(new DbHelper(context));
    }
}

package com.wyanez.simpletodolist.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.wyanez.simpletodolist.db.DbHelper;
import com.wyanez.simpletodolist.db.TaskDao;
import com.wyanez.simpletodolist.util.IConsumerResult;

public class TaskDeleteService {
    private Context context;
    private IConsumerResult<Integer> consumerResult;

    public TaskDeleteService(Context context, IConsumerResult<Integer> consumerResult) {
        this.consumerResult = consumerResult;
        this.context = context;
    }

    public void delete(int taskId) {
        TaskDelete  deleteTask = new TaskDelete(context,"Deleting task...");
        deleteTask.execute(taskId);
    }

    private void processResult(Integer result) {
        Log.d("processResult",result.toString());
        consumerResult.process(result);
    }

    private class TaskDelete  extends AsyncTask<Integer,Void,Integer> {
        private Context context;
        private ProgressDialog loading;
        private String titleDialogLoading;
        private TaskDao taskDao;

        public TaskDelete(Context context,String titleDialogLoading) {
            this.titleDialogLoading = titleDialogLoading;
            this.context = context;
            DbHelper dbHelper = new DbHelper(context);
            taskDao = new TaskDao(dbHelper);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(context,this.titleDialogLoading,"Espere...",false,false);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            int taskId = params[0];
            return taskDao.delete(taskId);
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Log.d("onPostExecute",result.toString());
            this.loading.dismiss();
            processResult(result);
        }
    }
}

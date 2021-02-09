package com.wyanez.simpletodolist.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.wyanez.simpletodolist.db.DbHelper;
import com.wyanez.simpletodolist.db.TaskDao;
import com.wyanez.simpletodolist.model.Task;
import com.wyanez.simpletodolist.util.IConsumerResult;

public class TaskSaveService {
    private Context context;
    private IConsumerResult<Long> consumerResult;

    public TaskSaveService(Context context, IConsumerResult<Long> consumerResult) {
        this.consumerResult = consumerResult;
        this.context = context;
    }

    public void save(Task task) {
        TaskSave saveTask = new TaskSave(context,"Saving task...","create");
        saveTask.execute(task);
    }

    private void processResult(Long result) {
        Log.d("processResult",result.toString());
        consumerResult.process(result);
    }

    private class TaskSave  extends AsyncTask<Task, Void, Long> {
        private Context context;
        private ProgressDialog loading;
        private String titleDialogLoading;
        private TaskDao taskDao;
        private Task task;
        private String mode;

        public TaskSave(Context context,String titleDialogLoading, String mode) {
            this.titleDialogLoading = titleDialogLoading;
            this.context = context;
            this.mode = mode;
            DbHelper dbHelper = new DbHelper(context);
            taskDao = new TaskDao(dbHelper);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(context,this.titleDialogLoading,"Espere...",false,false);
        }

        @Override
        protected Long doInBackground(Task... params) {
            task = params[0];
            long newTaskId = 0;
            if(mode.equals("create"))  newTaskId = taskDao.insert(task);
            else{
                //taskDao.update(task);
                //newTaskId = task.getId();
            }
            return newTaskId;
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            Log.d("onPostExecute",result.toString());
            this.loading.dismiss();
            processResult(result);
        }
    }
}

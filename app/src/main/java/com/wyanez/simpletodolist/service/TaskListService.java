package com.wyanez.simpletodolist.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.wyanez.simpletodolist.db.DbHelper;
import com.wyanez.simpletodolist.db.TaskDao;
import com.wyanez.simpletodolist.model.Task;
import com.wyanez.simpletodolist.util.IConsumerResult;

import java.util.List;

public class TaskListService {
    private Context context;
    private IConsumerResult<List<Task>> consumerResult;

    public TaskListService(Context context, IConsumerResult<List<Task>> consumerResult) {
        this.context = context;
        this.consumerResult = consumerResult;
    }

    public void list() {
        TaskListTask task = new TaskListTask(context, "Loading ToDoList");
        task.execute();
    }

    private void processResult(List<Task> list) {
        consumerResult.process(list);
    }

    private class TaskListTask extends AsyncTask<String, Void, List<Task>> {
        private Context context;
        private ProgressDialog loading;
        private String titleDialogLoading;
        private TaskDao taskDao;

        public TaskListTask(Context context, String titleDialogLoading) {
            this.titleDialogLoading = titleDialogLoading;
            this.context = context;
            taskDao = new TaskDao(new DbHelper(context));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(context, this.titleDialogLoading, "Wait...", false, false);
        }

        @Override
        protected List<Task> doInBackground(String... params) {
            List<Task> listTasks = taskDao.listTaskActive();
            return listTasks;
        }

        @Override
        protected void onPostExecute(List<Task> listTasks) {
            super.onPostExecute(listTasks);
            this.loading.dismiss();
            Log.d("onPostExecute",String.format("Hay %d Tasks",listTasks.size()));
            processResult(listTasks);
        }
    }
}

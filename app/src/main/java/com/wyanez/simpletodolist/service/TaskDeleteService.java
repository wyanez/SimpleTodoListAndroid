package com.wyanez.simpletodolist.service;

import android.content.Context;

import com.wyanez.simpletodolist.base.BaseCrudService;
import com.wyanez.simpletodolist.base.BaseCrudTask;
import com.wyanez.simpletodolist.db.TaskDao;
import com.wyanez.simpletodolist.model.Task;
import com.wyanez.simpletodolist.util.IConsumerResult;

public class TaskDeleteService extends BaseCrudService<Integer> {

    public TaskDeleteService(Context context, IConsumerResult<Integer> consumerResult) {
        super(context, consumerResult);
    }

    public void delete(Task task) {
        TaskDelete  deleteTask = new TaskDelete(context,"Deleting task...");
        deleteTask.setProcessResult(this.consumerResult);
        deleteTask.setDao(new TaskDao());
        deleteTask.execute(task);
    }

    private static class TaskDelete extends BaseCrudTask<Task, Integer, TaskDao> {

        TaskDelete(Context context, String titleDialogLoading) {
            super(context, titleDialogLoading);
        }

        @Override
        protected Integer doInBackground(Task... params) {
            long taskId = params[0].getId();
            return this.dao.delete(taskId);
        }

    }
}

package com.wyanez.simpletodolist.service;

import android.content.Context;

import com.wyanez.simpletodolist.base.BaseCrudService;
import com.wyanez.simpletodolist.base.BaseCrudTask;
import com.wyanez.simpletodolist.model.Task;
import com.wyanez.simpletodolist.util.IConsumerResult;

public class TaskDeleteService extends BaseCrudService<Long> {

    public TaskDeleteService(Context context, IConsumerResult<Long> consumerResult) {
        super(context, consumerResult);
    }

    public void delete(Task task) {
        TaskDelete  deleteTask = new TaskDelete(context,"Deleting task...");
        deleteTask.setProcessResult(this.consumerResult);
        deleteTask.execute(task);
    }

    private static class TaskDelete extends BaseCrudTask<Task, Long> {

        TaskDelete(Context context, String titleDialogLoading) {
            super(context, titleDialogLoading);
        }

        @Override
        protected Long doInBackground(Task... params) {
            long taskId = params[0].getId();
            return taskDao.delete(taskId);
        }

    }
}

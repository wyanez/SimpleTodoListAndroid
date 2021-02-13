package com.wyanez.simpletodolist.service;

import android.content.Context;

import com.wyanez.simpletodolist.base.BaseCrudService;
import com.wyanez.simpletodolist.base.BaseCrudTask;
import com.wyanez.simpletodolist.db.TaskDao;
import com.wyanez.simpletodolist.model.Task;
import com.wyanez.simpletodolist.util.IConsumerResult;

public class TaskSaveService extends BaseCrudService<Long> {
    public enum Operation {
        CREATE,
        EDIT
    }
    public TaskSaveService(Context context, IConsumerResult<Long> consumerResult) {
        super(context, consumerResult);
    }

    public void save(Task task, Operation mode) {
        TaskSave saveTask = new TaskSave(context,"Saving task...",mode);
        saveTask.setProcessResult(this.consumerResult);
        saveTask.setDao(new TaskDao());
        saveTask.execute(task);
    }

    private static class TaskSave extends BaseCrudTask<Task, Long, TaskDao> {
        private Task task;
        private Operation mode;

        TaskSave(Context context, String titleDialogLoading, Operation mode) {
            super(context, titleDialogLoading);
            this.mode = mode;
        }

        @Override
        protected Long doInBackground(Task... params) {
            task = params[0];
            long taskId = 0;
            switch (mode) {
                case CREATE:
                    taskId = this.dao.insert(task);
                    break;

                case EDIT:
                    int result = this.dao.update(task);
                    taskId = result > 0 ? task.getId() : result;
                    break;
            }
            return taskId;
        }
    }
}

package com.wyanez.simpletodolist.service;

import android.content.Context;

import com.wyanez.simpletodolist.base.BaseCrudService;
import com.wyanez.simpletodolist.base.BaseCrudTask;
import com.wyanez.simpletodolist.db.TaskDao;
import com.wyanez.simpletodolist.model.Task;
import com.wyanez.simpletodolist.util.IConsumerResult;

import java.util.List;

public class TaskListService extends BaseCrudService<List<Task>> {

    public TaskListService(Context context, IConsumerResult<List<Task>> consumerResult) {
        super(context, consumerResult);
    }

    public void list() {
        TaskListTask task = new TaskListTask(context, "Loading ToDoList");
        task.setProcessResult(this.consumerResult);
        task.setDao(new TaskDao());
        task.execute();
    }

    private static class TaskListTask extends BaseCrudTask<Void, List<Task>, TaskDao> {

        TaskListTask(Context context, String titleDialogLoading) {
            super(context, titleDialogLoading);
        }

        @Override
        protected List<Task> doInBackground(Void... params) {
            return dao.list();
        }

    }
}

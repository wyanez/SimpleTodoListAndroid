package com.wyanez.simpletodolist;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wyanez.simpletodolist.model.Task;
import com.wyanez.simpletodolist.service.TaskDeleteService;
import com.wyanez.simpletodolist.service.TaskListService;
import com.wyanez.simpletodolist.util.IConsumerResult;
import com.wyanez.simpletodolist.util.Utilities;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final TaskListService listService;
    private final TaskDeleteService deleteService;
    private TaskForm taskForm;

    private ListView listview;
    private TextView tvRecordCount;

    private TaskListAdapter listViewAdapter;


    public MainActivity() {
        IConsumerResult<List<Task>> consumerResult = this::listRecords;
        this.listService = new TaskListService(this, consumerResult);

        IConsumerResult<Integer> consumerResultDelete = this::processDeleteResult;
        this.deleteService = new TaskDeleteService(this, consumerResultDelete);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = findViewById(R.id.listview);
        tvRecordCount = findViewById(R.id.textViewRecordCount);

        final Button btnNuevo = findViewById(R.id.btnAddTask);
        btnNuevo.setOnClickListener(view -> {
            taskForm = new TaskForm(MainActivity.this);
            taskForm.showForm();
        });
        this.listTasks();
        registerForContextMenu(listview);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.listview) {
            MenuInflater inflater = getMenuInflater();
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Task task = listViewAdapter.getItem(info.position);
            menu.setHeaderTitle("Task id " + task.getId());
            inflater.inflate(R.menu.menu_task_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Log.d("onContextItemSelected","position = " +info.position);
        Task task;
        if(info.position >= 0) {
            task = listViewAdapter.getItem(info.position);
            switch (item.getItemId()) {
                case R.id.menuEdit:
                    showDialogEdit(task);
                    return true;
                case R.id.menuDelete:
                    showDialogDeleteTask(task);
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        return super.onContextItemSelected(item);
    }

    public void listTasks() {
        listService.list();
    }

    private void listRecords(List<Task> listTasks) {
        tvRecordCount.setText(String.format("%d Tasks",listTasks.size()));
        setupListView(listTasks);
    }


    private void setupListView(List<Task> list) {
        listViewAdapter = new TaskListAdapter(this,list);
        listview.setAdapter(listViewAdapter);
    }


    class TaskListAdapter extends ArrayAdapter<Task> {
        private final List<Task> listTasks;
        private final String[] arrPriorities;
        private final Calendar today;
        private final Calendar tomorrow;

        public TaskListAdapter(@NonNull Context context, @NonNull List<Task> listTasks ) {
            super(context, android.R.layout.simple_list_item_1, listTasks);
            this.listTasks = listTasks;
            this.arrPriorities = getResources().getStringArray(R.array.array_priorities);
            this.today = Calendar.getInstance();
            this.tomorrow = Calendar.getInstance();
            this.tomorrow.add(Calendar.DAY_OF_MONTH,1);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.task_list_item, parent, false);

            TextView tvTitle = rowView.findViewById(R.id.textViewTitle);
            TextView tvPriority = rowView.findViewById(R.id.textViewPriority);
            TextView tvDeadline =rowView.findViewById(R.id.textViewDeadline);

            Task task = getItem(position);
            tvPriority.setTextColor(getResources().getColor(R.color.colorPrimary));


            boolean taskInPast = task.getDeadline().before(today) || Utilities.CalendarEquals(task.getDeadline(),today);
            if(taskInPast || Utilities.CalendarEquals(task.getDeadline(),tomorrow)) {
                tvDeadline.setTextColor(getResources().getColor(R.color.colorAccent));
                if (taskInPast) tvDeadline.setTypeface(tvDeadline.getTypeface(), Typeface.BOLD);
            }
            else tvDeadline.setTextColor(getResources().getColor(R.color.colorPrimary));

            if(task.getPriority()<=2) tvPriority.setTypeface(tvPriority.getTypeface(), Typeface.BOLD);

            tvTitle.setText(task.getTitle());
            tvPriority.setText(arrPriorities[task.getPriority()]);
            tvDeadline.setText("Deadline: " + task.getDeadlineYMD());
            return rowView;
        }

        public void deleteTask(Task task){
            listTasks.remove(task);
            this.notifyDataSetChanged();
            tvRecordCount.setText(String.format("%d Tasks",listTasks.size()));
        }
    }

    protected void showDialogDeleteTask(Task currentTask) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Task - " + currentTask.getId());
        String msg = String.format("Are you sure to delete task #%s ?", currentTask.getId());
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            deleteService.delete(currentTask.getId());
            listViewAdapter.deleteTask(currentTask);
        });

        builder.setNegativeButton("No", (dialog, which) -> Log.d("DeleteTask","Not Delete Selected"));

        builder.show();
    }

    private void processDeleteResult(Integer result) {
        String msg;
        if(result>0) msg = "Task deleted sucessfully";
        else msg = "ATENTION: Task Can't be deleted!";
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    private void showDialogEdit(Task currentTask){
        taskForm = new TaskForm(MainActivity.this);
        taskForm.showForm();
        taskForm.setTask(currentTask);
    }

}

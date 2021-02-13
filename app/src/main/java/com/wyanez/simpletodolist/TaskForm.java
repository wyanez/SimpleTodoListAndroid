package com.wyanez.simpletodolist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.wyanez.simpletodolist.model.Task;
import com.wyanez.simpletodolist.service.TaskSaveService;
import com.wyanez.simpletodolist.util.IConsumerResult;

import java.util.Calendar;

public class TaskForm {

    private final MainActivity mainActivity;
    private final TaskSaveService taskSaveService;
    private AlertDialog dialogTask ;

    private EditText editTitle;
    private EditText editDescription;
    private EditText editTags;
    private EditText editDeadline;
    private Spinner spinnerPriority;
    private CheckBox checkActive;
    private Calendar deadline;
    private String mode;

    private Task currentTask;

    public TaskForm(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        IConsumerResult<Long> consumerResult = this::finishSaveTask;
        taskSaveService = new TaskSaveService(mainActivity,consumerResult);
        this.mode = "create";
        currentTask = null;
    }

    public void showForm() {
        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.activity_task_form,null, false);

        editTitle = formElementsView.findViewById(R.id.editTitleTask);
        editDescription = formElementsView.findViewById(R.id.editDescription);
        editTags = formElementsView.findViewById(R.id.editTags);
        editDeadline = formElementsView.findViewById(R.id.editDeadline);
        spinnerPriority = formElementsView.findViewById(R.id.spinnerPriorityTask);
        checkActive = formElementsView.findViewById(R.id.checkActive);
        deadline = Calendar.getInstance();

        editDeadline.setOnClickListener(v -> showDatePickerDialog(editDeadline,deadline));

        AlertDialog.Builder builderDialogTask = new AlertDialog.Builder(mainActivity)
                .setView(formElementsView)
                .setTitle("New Task")
                .setNegativeButton("Cancel",
                        (dialog, which) -> Log.d("FormTask","Cancel Selected"))
                .setPositiveButton("Save",
                        (dialog, which) -> { });

        dialogTask = builderDialogTask.create();
        dialogTask.show();

        dialogTask.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> saveDataForm());
    }

    private void saveDataForm(){
        String title = editTitle.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String tags = editTags.getText().toString().trim();

        int priority = spinnerPriority.getSelectedItemPosition();
        boolean active = checkActive.isChecked();

        boolean isValid = validateData(title,description,tags,deadline,priority,active);
        if(isValid){
            if(this.mode.equals("create")) currentTask = new Task();
            currentTask.setTitle(title);
            currentTask.setDescription(description);
            currentTask.setTags(tags);
            currentTask.setPriority(priority);
            currentTask.setActive(active);
            currentTask.setDeadline(deadline);
            Log.d("saveTask", currentTask.toString());
            taskSaveService.save(currentTask,mode);
        }
    }

    private boolean validateData(String title, String description,String tags, Calendar deadline, int priority, boolean active){

        if(title.equals("")){
            Toast.makeText(mainActivity.getApplicationContext(), "Attention: you must indicate the title", Toast.LENGTH_LONG).show();
            return false;
        }

        if(description.equals("")){
            Toast.makeText(mainActivity.getApplicationContext(), "Attention: you must indicate the description", Toast.LENGTH_LONG).show();
            return false;
        }

        if(tags.equals("")){
            Toast.makeText(mainActivity.getApplicationContext(), "Attention: you must indicate the tags", Toast.LENGTH_LONG).show();
            return false;
        }

        if(priority<=0){
            Toast.makeText(mainActivity.getApplicationContext(), "Attention: you must select the priority", Toast.LENGTH_LONG).show();
            return false;
        }

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR,0);
        today.set(Calendar.MINUTE,0);
        today.set(Calendar.SECOND,0);

        if(deadline.before(today)){
            Toast.makeText(mainActivity.getApplicationContext(), "Attention: you must indicate the deadline today or in the future", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void showDatePickerDialog(final EditText editTextDate, final Calendar selectedDate){
        DatePickerDialog.OnDateSetListener listener= (view, year, monthOfYear, dayOfMonth) -> {
            editTextDate.setText(String.format("%02d/%02d/%d",dayOfMonth,monthOfYear+1, year));
            selectedDate.set(year,monthOfYear,dayOfMonth);
        };
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpDialog = new DatePickerDialog(mainActivity, listener, year, month, day);
        dpDialog.show();
    }

    private void finishSaveTask(Long taskId) {
        String msg;
        if(taskId>0) msg = String.format("Task saved sucessfully! id = %d", taskId);
        else msg = "An error occurred while saving Task";
        Toast.makeText(mainActivity.getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        if (taskId>0){
            dialogTask.cancel();
            mainActivity.listTasks();
        }
    }

    public void setTask(Task task){
        editTitle.setText(task.getTitle());
        editDescription.setText(task.getDescription());
        editTags.setText(task.getTags());
        spinnerPriority.setSelection(task.getPriority());
        checkActive.setChecked(task.isActive());
        deadline = task.getDeadline();

        int dayOfMonth = deadline.get(Calendar.DAY_OF_MONTH);
        int monthOfYear = deadline.get(Calendar.MONTH);
        int year = deadline.get(Calendar.YEAR);
        editDeadline.setText(String.format("%02d/%02d/%d",dayOfMonth,monthOfYear+1, year));
        this.mode = "edit";
        this.currentTask = task;
        dialogTask.setTitle("Edit Task");
    }

}

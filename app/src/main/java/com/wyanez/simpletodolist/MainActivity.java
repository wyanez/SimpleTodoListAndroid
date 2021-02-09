package com.wyanez.simpletodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import java.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.wyanez.simpletodolist.model.Task;
import com.wyanez.simpletodolist.service.TaskSaveService;
import com.wyanez.simpletodolist.util.IConsumerResult;

public class MainActivity extends AppCompatActivity {


    private final TaskSaveService taskSaveService;
    AlertDialog dialogTask ;

    public MainActivity() {
        IConsumerResult<Long> consumerResult = (result) -> { finishSaveTask(result);};
        taskSaveService = new TaskSaveService(this,consumerResult);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnNuevo = findViewById(R.id.btnAddTask);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFormAddTask();
            }
        });
        
    }

    private void showFormAddTask() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.activity_task_form,null, false);

        final EditText editTitle = formElementsView.findViewById(R.id.editTitleTask);
        final EditText editDescription = formElementsView.findViewById(R.id.editDescription);
        final EditText editTags = formElementsView.findViewById(R.id.editTags);
        final EditText editDeadline = formElementsView.findViewById(R.id.editDeadline);
        final Spinner spinnerPriority = formElementsView.findViewById(R.id.spinnerPriorityTask);
        final CheckBox checkActive = formElementsView.findViewById(R.id.checkActive);
        final Calendar deadline = Calendar.getInstance();

        editDeadline.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editDeadline,deadline);
            }
        });

        AlertDialog.Builder builderDialogTask = new AlertDialog.Builder(this)
                .setView(formElementsView)
                .setTitle("New Task")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                Log.d("New Task","Cancel Selected");
                            }
                        })
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                            }
                        });

        dialogTask = builderDialogTask.create();
        dialogTask.show();

        dialogTask.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            saveDataForm(editTitle,
                         editDescription,
                         editTags,
                         deadline,
                         spinnerPriority,
                         checkActive);

            }
        });
    }

    private void saveDataForm(EditText editTitle,
                         EditText editDescription,
                         EditText editTags,
                         Calendar deadline,
                         Spinner spinnerPriority,
                         CheckBox checkActive){

        String title = editTitle.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String tags = editTags.getText().toString().trim();

        int priority = spinnerPriority.getSelectedItemPosition();
        boolean active = checkActive.isChecked();

        boolean isValid = validateData(title,description,tags,deadline,priority,active);
        if(isValid){
            Task task = new Task(title,description,tags,deadline,priority,active);
            Log.d("NewTask",task.toString());
            taskSaveService.save(task);
        }
    }

    private boolean validateData(String title, String description,String tags, Calendar deadline, int priority, boolean active){

        if(title.equals("")){
            Toast.makeText(this, "Attention: you must indicate the title", Toast.LENGTH_LONG).show();
            return false;
        }

        if(description.equals("")){
            Toast.makeText(this, "Attention: you must indicate the description", Toast.LENGTH_LONG).show();
            return false;
        }

        if(tags.equals("")){
            Toast.makeText(this, "Attention: you must indicate the tags", Toast.LENGTH_LONG).show();
            return false;
        }

        //String optionSelect = getResources().getStringArray(R.array.array_priorities)[0];
        if(priority<=0){
            Toast.makeText(this, "Attention: you must select the priority", Toast.LENGTH_LONG).show();
            return false;
        }

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR,0);
        today.set(Calendar.MINUTE,0);
        today.set(Calendar.SECOND,0);

        if(deadline.before(today)){
            Toast.makeText(this, "Attention: you must indicate the deadline today or in the future", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public void showDatePickerDialog(final EditText editTextDate, final Calendar selectedDate){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
            {
                editTextDate.setText(String.format("%2d/%2d/%d",dayOfMonth,monthOfYear+1, year));
                selectedDate.set(year,monthOfYear,dayOfMonth);

            }};
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpDialog = new DatePickerDialog(MainActivity.this, listener, year, month, day);
        dpDialog.show();
    }

    private void finishSaveTask(Long taskId) {
        String msg;
        if(taskId>0) msg = String.format("Task saved sucessfully! id = %d", taskId);
        else msg = "An error occurred while saving Task";
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
        if (taskId>0) dialogTask.cancel();
    }

}

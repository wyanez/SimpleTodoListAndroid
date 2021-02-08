package com.wyanez.simpletodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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

        //final EditText editNombre = (EditText)formElementsView.findViewById(R.id.editNombreCli);
        //final Spinner spinnerEstado = (Spinner) formElementsView.findViewById(R.id.spinnerEstadoCli);

        AlertDialog.Builder builderDialogTask = new AlertDialog.Builder(this)
                .setView(formElementsView)
                .setTitle("New Task")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                //dialog.cancel();
                                Log.d("New Task","Seleccciono Cancelar");
                            }
                        })
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                            }
                        });

        final AlertDialog dialogTask = builderDialogTask.create();
        dialogTask.show();

        dialogTask.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Task saved sucessfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

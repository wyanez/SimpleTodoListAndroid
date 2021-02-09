package com.wyanez.simpletodolist;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TaskForm taskForm;


    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnNuevo = findViewById(R.id.btnAddTask);
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskForm = new TaskForm(MainActivity.this);
                taskForm.showFormAddTask();
            }
        });
        
    }

}

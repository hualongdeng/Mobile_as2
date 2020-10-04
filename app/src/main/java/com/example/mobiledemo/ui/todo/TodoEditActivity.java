package com.example.mobiledemo.ui.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.setting.MonitorSettingActivity;

public class TodoEditActivity extends AppCompatActivity {

    private TodoEditViewModel todoEditViewModel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_edit);
        todoEditViewModel = ViewModelProviders.of(this).get(TodoEditViewModel.class);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.todo_edit_container, new TodoEditFragment())
                .commit();
        final Button backButton = findViewById(R.id.todo_edit_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoEditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

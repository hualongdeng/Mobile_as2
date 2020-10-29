package com.example.mobiledemo.ui.todo_new;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.notifications.TodoEntity;
import com.example.mobiledemo.ui.setting.MonitorSettingActivity;
import com.example.mobiledemo.ui.todo.TodoEditActivity;

import java.util.List;

public class TodoNewActivity extends AppCompatActivity {

    private TodoNewViewModel todoNewViewModel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_new);
        todoNewViewModel = ViewModelProviders.of(this).get(TodoNewViewModel.class);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.todo_new_container, new TodoNewFragment())
                .commit();

        final Button backButton = findViewById(R.id.todo_new_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoNewActivity.this, TodoEditActivity.class);
                startActivity(intent);
            }
        });
    }
}

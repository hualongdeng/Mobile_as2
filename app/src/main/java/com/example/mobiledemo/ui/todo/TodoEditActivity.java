package com.example.mobiledemo.ui.todo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobiledemo.R;

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
    }
}

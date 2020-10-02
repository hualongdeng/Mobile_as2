package com.example.mobiledemo.ui.todo_new;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobiledemo.R;

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
    }
}

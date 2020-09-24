package com.example.mobiledemo.ui.password;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.account.AccountActivity;
import com.example.mobiledemo.ui.login.LoginActivity;

public class PasswordActivity extends AppCompatActivity {

    private PasswordViewModel passwordViewModel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        passwordViewModel = ViewModelProviders.of(this).get(PasswordViewModel.class);
        final Button backButton = findViewById(R.id.password_back);
        final Button saveButton = findViewById(R.id.password_save);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
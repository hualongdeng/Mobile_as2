package com.example.mobiledemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;




//the sign up page class.
public class SignUp extends AppCompatActivity {

    EditText birthday_text, email_text, password_text, phone_text;
    String name, email, password, confirm;

    private static final String TAG = "SignUp";

    // initialize the items.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        birthday_text = findViewById(R.id.register_birthday);
        email_text = findViewById(R.id.username);
        password_text = findViewById(R.id.password);
        phone_text = findViewById(R.id.editTextPhone);


    }


}


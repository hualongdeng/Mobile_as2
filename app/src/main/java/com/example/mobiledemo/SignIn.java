package com.example.mobiledemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SignIn extends AppCompatActivity {

    EditText email_text, password_text;
    TextView logIn;

    String email, password;
    private static final String TAG = "Sign_in";

    //initialize the class, and connect items.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_text = findViewById(R.id.username);
        password_text = findViewById(R.id.password);
        logIn = findViewById(R.id.login);


        logIn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });
    }

    // The login event.
    public void loginBtn(View view) {
        email = email_text.getText().toString();
        password = password_text.getText().toString();
    }
}
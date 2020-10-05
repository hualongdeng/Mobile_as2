package com.example.mobiledemo.ui.type1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.account.AccountActivity;

public class Type1Activity extends AppCompatActivity{

    private Type1ViewModel type1ViewModel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type1);
        type1ViewModel = ViewModelProviders.of(this).get(Type1ViewModel.class);
        final Button backButton = findViewById(R.id.type1_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Type1Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
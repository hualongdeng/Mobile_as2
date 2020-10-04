package com.example.mobiledemo.ui.type1_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.account.AccountActivity;

public class Type1_1Activity extends AppCompatActivity{

    private Type1_1ViewModel type1_1ViewModel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type1_1);
        type1_1ViewModel = ViewModelProviders.of(this).get(Type1_1ViewModel.class);
        final Button backButton = findViewById(R.id.type1_1_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Type1_1Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
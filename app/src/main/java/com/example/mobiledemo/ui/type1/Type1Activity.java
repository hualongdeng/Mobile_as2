package com.example.mobiledemo.ui.type1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.account.AccountActivity;

public class Type1Activity extends AppCompatActivity{

    private Type1ViewModel type1ViewModel;
    private TextView type1Tv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type1);
        type1ViewModel = ViewModelProviders.of(this).get(Type1ViewModel.class);
        final Button backButton = findViewById(R.id.type1_back);
        type1Tv = findViewById(R.id.type1_text);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Type1Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        boolean achieved = getIntent().getBooleanExtra("achieved", false);
        if (achieved) {
            type1Tv.setText("Congratulations! Your cumulative time has exceeded 10 minutes!");
        } else {
            type1Tv.setText("You need to accumulate 10 minutes of use to win this medal.");
        }
    }
}
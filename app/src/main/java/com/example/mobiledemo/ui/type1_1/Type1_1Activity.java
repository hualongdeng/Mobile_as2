package com.example.mobiledemo.ui.type1_1;

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

public class Type1_1Activity extends AppCompatActivity{

    private Type1_1ViewModel type1_1ViewModel;
    private TextView type1_1Tv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type1_1);
        type1_1ViewModel = ViewModelProviders.of(this).get(Type1_1ViewModel.class);
        final Button backButton = findViewById(R.id.type1_1_back);
        type1_1Tv = findViewById(R.id.type1_1_text);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Type1_1Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        boolean achieved = getIntent().getBooleanExtra("achieved", false);
        if (achieved) {
            type1_1Tv.setText("Congratulations! Your cumulative time has exceeded 1 hour!");
        } else {
            type1_1Tv.setText("You need to accumulate 1 hour of use to win this medal.");
        }

    }
}
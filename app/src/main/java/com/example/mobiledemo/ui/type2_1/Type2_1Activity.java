package com.example.mobiledemo.ui.type2_1;

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

public class Type2_1Activity extends AppCompatActivity{

    private Type2_1ViewModel type2_1ViewModel;
    private TextView type2_1Tv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type2_1);
        type2_1ViewModel = ViewModelProviders.of(this).get(Type2_1ViewModel.class);
        final Button backButton = findViewById(R.id.type2_1_back);
        type2_1Tv = findViewById(R.id.type2_1_text);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Type2_1Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        boolean achieved = getIntent().getBooleanExtra("achieved", false);
        if (achieved) {
            type2_1Tv.setText("Congratulations to you! You've used the timer more than 100 times!");
        } else {
            type2_1Tv.setText("You need to accumulate 100 times of use to win this medal.");
        }
    }
}
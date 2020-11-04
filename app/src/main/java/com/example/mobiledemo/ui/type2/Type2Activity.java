package com.example.mobiledemo.ui.type2;

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

public class Type2Activity extends AppCompatActivity{

    private Type2ViewModel type2ViewModel;
    private TextView type2Tv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type2);
        type2ViewModel = ViewModelProviders.of(this).get(Type2ViewModel.class);
        final Button backButton = findViewById(R.id.type2_back);
        type2Tv = findViewById(R.id.type2_text);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Type2Activity.this, MainActivity.class);
////                startActivity(intent);
                onBackPressed();
            }
        });

        boolean achieved = getIntent().getBooleanExtra("achieved", false);
        if (achieved) {
            type2Tv.setText("Congratulations to you! You've used the timer more than 10 times!");
        } else {
            type2Tv.setText("You need to accumulate 10 times of use to win this medal.");
        }

    }
}
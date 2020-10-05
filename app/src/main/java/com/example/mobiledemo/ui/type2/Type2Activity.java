package com.example.mobiledemo.ui.type2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.account.AccountActivity;

public class Type2Activity extends AppCompatActivity{

    private Type2ViewModel type2ViewModel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type2);
        type2ViewModel = ViewModelProviders.of(this).get(Type2ViewModel.class);
        final Button backButton = findViewById(R.id.type2_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Type2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
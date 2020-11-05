package com.example.mobiledemo.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.password.PasswordViewModel;

public class ProfilePhotoActivity extends AppCompatActivity {
    private ProfilePhotoViewModel ProfilePhotoViewModel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ProfilePhotoViewModel = ViewModelProviders.of(this).get(ProfilePhotoViewModel.class);
        final Button backButton = findViewById(R.id.account_back2);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePhotoActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
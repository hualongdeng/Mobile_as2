package com.example.mobiledemo.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;

import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;

public class SettingActivity extends AppCompatActivity {

    private SettingViewModel settingViewModel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingFragment())
                .commit();
        final Button backButton = findViewById(R.id.setting_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                intent.putExtra("fragment", 1);
                startActivity(intent);
            }
        });
    }
}
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
import com.example.mobiledemo.ui.report.ReportActivity;

public class MonitorSettingActivity extends AppCompatActivity {

    private MonitorSettingViewModel monitorSettingViewModel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_setting);
        monitorSettingViewModel = ViewModelProviders.of(this).get(MonitorSettingViewModel.class);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.monitor_settings_container, new MonitorSettingFragment())
                .commit();
        final Button backButton = findViewById(R.id.monitor_setting_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MonitorSettingActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }
}

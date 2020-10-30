package com.example.mobiledemo.ui.report;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.data.recordTime;
import com.example.mobiledemo.ui.account.AccountActivity;
import com.example.mobiledemo.utils.DbUtils;

import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private ReportViewModel reportViewModel;
    private TextView reportTv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        final Button backButton = findViewById(R.id.report_back);
        reportTv = findViewById(R.id.report_text);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        initData();
    }

    private void initData() {
       String type = getIntent().getStringExtra("type");
       long sum = 0;
       List<recordTime> recordTimeList = DbUtils.getQueryAll(recordTime.class);
        for (recordTime record :
                recordTimeList) {
            sum+=record.getTimeLength()/1000;
        }
       if (type.equals("day")) {
           reportTv.setText("Today, you have used this app for " + sum + " seconds.");
       } else if (type.equals("week")) {
           reportTv.setText("This week, you have used this app for " + sum + " seconds.");
       } else if (type.equals("month")) {
           reportTv.setText("This month, you have used this app for " + sum + " seconds.");
       }
    }
}
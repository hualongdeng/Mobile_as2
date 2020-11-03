package com.example.mobiledemo.ui.report;

import android.content.Intent;
import android.graphics.Color;
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
import com.example.mobiledemo.utils.TimeUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private ReportViewModel reportViewModel;
    private TextView reportTv;
    private LineChart lineChart;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
        final Button backButton = findViewById(R.id.report_back);
        reportTv = findViewById(R.id.report_text);
        lineChart = findViewById(R.id.lineChart);
        lineChart.animateXY(1100,1100);

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
        if (type.equals("day")) {
            List<recordTime> recordTimeList = DbUtils.getQueryAll(recordTime.class);
            for (recordTime record :
                    recordTimeList) {
                sum+=record.getTimeLength()/1000;
            }
            reportTv.setText("Today, you have used this app for " + sum + " seconds.");
        } else if (type.equals("week")) {
            reportTv.setText("This week, you have used this app for " + sum + " seconds.");
        } else if (type.equals("month")) {
            reportTv.setText("This month, you have used this app for " + sum + " seconds.");
        }

        drawLineChart();
    }

    public void drawLineChart(){

        List<recordTime> recordTimes = DbUtils.getQueryAll(recordTime.class);
        Collections.reverse(recordTimes);
        ArrayList<Entry> values = new ArrayList<>();
        //add data
        if (!recordTimes.isEmpty()) {
            int i=0;
            for (; i<7&&i<recordTimes.size(); i++) {
                recordTime record = recordTimes.get(i);
                values.add(new Entry(i+1, (long)record.getTimeLength()/1000));
            }
            for (; i<7; i++) {
                values.add(new Entry(i+1, 0));
            }
        }

        xText();
        yText();

        //execute
        if (!values.isEmpty()) {
            text_all(values);
        }
    }

    // set y axis
    private void xText() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
    }

    //set x axis
    private void yText() {
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setEnabled(false);
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);
    }

    private void text_all(ArrayList<Entry> values) {
        LineDataSet set1;
        set1 = new LineDataSet(values, "Time Report");
        set1.setMode(LineDataSet.Mode.LINEAR);
        set1.setColor(Color.BLACK);
        set1.setLineWidth(3);
        set1.setCircleRadius(4);
        set1.enableDashedHighlightLine(2,2,1);
        set1.setHighlightLineWidth(2);
        set1.setHighlightEnabled(false);
        set1.setHighLightColor(Color.RED);
        set1.setValueTextSize(15);
        set1.setDrawFilled(false);

        LineData data = new LineData(set1);
        data.setValueFormatter(new MonthlyIntegerYValueFormatter());
        lineChart.setData(data);
        lineChart.invalidate();
    }

    public static class MonthlyIntegerYValueFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return (int) (value) + "";
        }
    }
}
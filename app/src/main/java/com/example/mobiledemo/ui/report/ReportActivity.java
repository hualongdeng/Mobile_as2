package com.example.mobiledemo.ui.report;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.data.RecordTime;
import com.example.mobiledemo.ui.account.AccountActivity;
import com.example.mobiledemo.utils.DbUtils;
import com.example.mobiledemo.utils.SPUtils;
import com.example.mobiledemo.utils.TimeUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private ReportViewModel reportViewModel;
    private TextView reportTv;
    private LineChart lineChart;
    private List<RecordTime> recordTimeList;
    private String get_url = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/recordtime";

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
                onBackPressed();
            }
        });

        initData();
    }

    private void initData() {

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest deleteRequest = new StringRequest(Request.Method.GET, get_url + "?user_id=" + SPUtils.getString(this, "account"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", response);

                Gson gson = new Gson();//Create Gson object
                JsonParser jsonParser = new JsonParser();
                JsonArray jsonElements = jsonParser.parse(response).getAsJsonArray();//Get JsonArray object
                ArrayList<RecordTime> beans = new ArrayList<>();
                for (JsonElement bean : jsonElements) {
                    RecordTime bean1 = gson.fromJson(bean, RecordTime.class);//Parsing
                    beans.add(bean1);
                }
                recordTimeList = beans;


                String type = getIntent().getStringExtra("type");
                long sum = 0;
                long hour = 0;
                long minute = 0;
                long second = 0;
                if (type.equals("day")) {
                    for (RecordTime record :
                            recordTimeList) {
                        if (Long.parseLong(record.getStart_time()) > TimeUtils.getLastDayTimestamp()/1000)
                            sum+=Long.parseLong(record.getTime_length())/1000;
                    }
                    hour = (long)sum/3600;
                    minute = (long)(sum-hour*3600)/60;
                    second = sum-hour*3600-minute*60;
                    reportTv.setText("Today, you have used this app for " + hour + (hour>1?" hours, ":" hour, ") + minute + (minute>1?" minutes, ":" minute, ") + second + (second>1?" seconds.":" second."));
                } else if (type.equals("week")) {
                    for (RecordTime record :
                            recordTimeList) {
                        if (Long.parseLong(record.getStart_time()) > TimeUtils.getLastWeekTimestamp()/1000)
                            sum+=Long.parseLong(record.getTime_length())/1000;
                    }
                    hour = (long)sum/3600;
                    minute = (long)(sum-hour*3600)/60;
                    second = sum-hour*3600-minute*60;
                    reportTv.setText("This week, you have used this app for " + hour + (hour>1?" hours, ":" hour, ") + minute + (minute>1?" minutes, ":" minute, ") + second + (second>1?" seconds.":" second."));
                } else if (type.equals("month")) {
                    for (RecordTime record :
                            recordTimeList) {
                        if (Long.parseLong(record.getStart_time()) > TimeUtils.getLastMonthTimestamp()/1000)
                            sum+=Long.parseLong(record.getTime_length())/1000;
                    }
                    hour = (long)sum/3600;
                    minute = (long)(sum-hour*3600)/60;
                    second = sum-hour*3600-minute*60;
                    reportTv.setText("This month, you have used this app for " + hour + (hour>1?" hours, ":" hour, ") + minute + (minute>1?" minutes, ":" minute, ") + second + (second>1?" seconds.":" second."));
                }

                drawLineChart();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(deleteRequest);

    }

    public void drawLineChart(){

        List<RecordTime> tempTimes = recordTimeList;
        Collections.sort(tempTimes);

        List<RecordTime> recordTimes = new ArrayList<RecordTime>();

        String type = getIntent().getStringExtra("type");
        for(int i=0; i<tempTimes.size(); i++) {
            RecordTime record = tempTimes.get(i);
            if (type.equals("day")) {
                if (Long.parseLong(record.getStart_time()) > TimeUtils.getLastDayTimestamp()/1000)
                    recordTimes.add(record);
            } else if (type.equals("week")) {
                if (Long.parseLong(record.getStart_time()) > TimeUtils.getLastWeekTimestamp()/1000)
                    recordTimes.add(record);
            } else if (type.equals("month")) {
                if (Long.parseLong(record.getStart_time()) > TimeUtils.getLastMonthTimestamp()/1000)
                    recordTimes.add(record);
            }
        }

        ArrayList<Entry> values = new ArrayList<>();
        //add data
        if (!recordTimes.isEmpty()) {
            int i=0;
            for (; values.size()<7&&i<recordTimes.size(); i++) {
                RecordTime record = recordTimes.get(i);
                values.add(new Entry(i+1, Long.parseLong(record.getTime_length())/1000));
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

        setDesc();

    }

    // set y axis
    private void xText() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return (int)value;
//            }
//        });
    }

    //set x axis
    private void yText() {
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setEnabled(true);
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int)value+"s";
            }
        });
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);
    }

    private void text_all(ArrayList<Entry> values) {
        LineDataSet set1;
        set1 = new LineDataSet(values, "");
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
    public void setDesc(){
        lineChart.setDescription(null);// Add to LineChart
    }


    public static class MonthlyIntegerYValueFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return (int) (value) + "";
        }
    }
}
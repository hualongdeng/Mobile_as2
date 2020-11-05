package com.example.mobiledemo.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.Button;
import android.widget.Toast;
import android.os.SystemClock;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.todo.TodoEditActivity;
import com.example.mobiledemo.utils.DbUtils;
import com.example.mobiledemo.utils.SPUtils;
import com.example.mobiledemo.utils.TimeUtils;

import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment implements View.OnClickListener, Chronometer.OnChronometerTickListener{

    private Chronometer chronometer;
    private Button btn_start,btn_stop,btn_base,btn_format;
    private long lastPause;
    private boolean start = false;
    private boolean isRunning = false;

    private String update_url = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/recordtime_update";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chronometer = getActivity().findViewById(R.id.chronometer);
        btn_start = getActivity().findViewById(R.id.btnStart);
        btn_stop = getActivity().findViewById(R.id.btnStop);
        btn_base = getActivity().findViewById(R.id.btnReset);

        chronometer.setOnChronometerTickListener(this);
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_base.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStart:
                if (!isRunning) {
                    if (!start) {
                        start = true;
                        chronometer.setBase(SystemClock.elapsedRealtime());
                    } else {
                        chronometer.setBase(chronometer.getBase() + SystemClock.elapsedRealtime() - lastPause);
                    }
                    chronometer.start();
                    isRunning = true;
                }
                break;
            case R.id.btnStop:
                if (isRunning) {
                    lastPause = SystemClock.elapsedRealtime();
                    chronometer.stop();
                    isRunning = false;
                }
                break;
            case R.id.btnReset:
                if (!isRunning) {
//                    recordTime record = new recordTime();
//                    record.setStartTime(TimeUtils.getCurrentLongTime());
                    final long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
//                    record.setTimeLength(elapsedMillis);
//                    DbUtils.insert(record);


                    RequestQueue mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, update_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("TAG", response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", error.getMessage(), error);
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("user_id", SPUtils.getString(getContext(),"account"));
                            map.put("start_time", TimeUtils.getCurrentLongTime()+"");
                            map.put("time_length", elapsedMillis+"");

                            return map;
                        }
                    };
                    mQueue.add(stringRequest);

                    start = false;
                    chronometer.setBase(SystemClock.elapsedRealtime());
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
//        String time = chronometer.getText().toString();
    }
}
package com.example.mobiledemo.ui.dashboard;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
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
import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.setting.AppEntity;
import com.example.mobiledemo.ui.todo.TodoEditActivity;
import com.example.mobiledemo.utils.DbUtils;
import com.example.mobiledemo.utils.SPUtils;
import com.example.mobiledemo.utils.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

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

        if ((getActivity().getSharedPreferences("timer", MODE_PRIVATE).getLong("timer", Context.MODE_PRIVATE)) == 0) {
        } else {
            if ((getActivity().getSharedPreferences("timer", MODE_PRIVATE).getInt("timerOn", Context.MODE_PRIVATE)) == 1) {
                chronometer.setBase(getActivity().getSharedPreferences("timer", MODE_PRIVATE).getLong("timer", Context.MODE_PRIVATE));
                isRunning = true;
                chronometer.start();
            } else {
                chronometer.setBase(getActivity().getSharedPreferences("timer", MODE_PRIVATE).getLong("timer", Context.MODE_PRIVATE) + SystemClock.elapsedRealtime() - getActivity().getSharedPreferences("timer", MODE_PRIVATE).getLong("timerPulse", Context.MODE_PRIVATE));
                isRunning = false;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStart:
                if (!isRunning) {
                    if ((getActivity().getSharedPreferences("timer", MODE_PRIVATE).getLong("timerPulse", Context.MODE_PRIVATE)) == 0) {
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        SharedPreferences timerPref = getActivity().getSharedPreferences("timer", Context.MODE_PRIVATE);
                        SharedPreferences.Editor timerEditor = timerPref.edit();
                        timerEditor.putInt("timerOn", 1);
                        timerEditor.putLong("timer", SystemClock.elapsedRealtime());
                        timerEditor.commit();
                    } else {
                        chronometer.setBase(getActivity().getSharedPreferences("timer", MODE_PRIVATE).getLong("timer", Context.MODE_PRIVATE) + SystemClock.elapsedRealtime() - getActivity().getSharedPreferences("timer", MODE_PRIVATE).getLong("timerPulse", Context.MODE_PRIVATE));
                        SharedPreferences timerPref = getActivity().getSharedPreferences("timer", Context.MODE_PRIVATE);
                        SharedPreferences.Editor timerEditor = timerPref.edit();
                        timerEditor.putInt("timerOn", 1);
                        timerEditor.putLong("timer", chronometer.getBase());
                        timerEditor.commit();
                    }
                    chronometer.start();
                    isRunning = true;
                    ((MainActivity) getActivity()).startAppMonitor();
                    ((MainActivity) getActivity()).startVoiceMonitor();
                    ((MainActivity) getActivity()).startLocMonitor();
                }
                break;
            case R.id.btnStop:
                if (isRunning) {
                    lastPause = SystemClock.elapsedRealtime();
                    chronometer.stop();
                    isRunning = false;
                    SharedPreferences timerPref = getActivity().getSharedPreferences("timer", Context.MODE_PRIVATE);
                    SharedPreferences.Editor timerEditor = timerPref.edit();
                    timerEditor.putInt("timerOn", 0);
                    timerEditor.putLong("timerPulse", SystemClock.elapsedRealtime());
                    timerEditor.commit();
                    stopAppMonitor();
                    stopVoiceMonitor();
                    stopLocationMonitor();
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
                    SharedPreferences timerPref = getActivity().getSharedPreferences("timer", Context.MODE_PRIVATE);
                    SharedPreferences.Editor timerEditor = timerPref.edit();
                    timerEditor.putInt("timerOn", 0);
                    timerEditor.putLong("timer", 0);
                    timerEditor.putLong("timerPulse", 0);
                    timerEditor.commit();
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

    public static String getRecentTask(Context context) {
        String currentApp = null;
        try {
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = null;
            if (usm != null) {
                appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            }
            if (appList != null && !appList.isEmpty()) {
                SortedMap<Long, UsageStats> sortedMap = new TreeMap<>();
                for (UsageStats usageStats : appList) {
                    sortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (!sortedMap.isEmpty()) {
                    currentApp = sortedMap.get(sortedMap.lastKey()).getPackageName();
                }
            }
            Log.e("ActivityTAG", "Application in foreground: " + currentApp);
            return currentApp;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    public void stopAppMonitor() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("thread_killer", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("thread_killer", 0);
        editor.commit();
    }

    public void stopVoiceMonitor() {
        if ((getActivity().getSharedPreferences("monitor", MODE_PRIVATE).getInt("volume_monitor", Context.MODE_PRIVATE)) == -1) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("monitor", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("volume_monitor", -1);
            editor.commit();
        } else {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("monitor", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("volume_monitor", 0);
            editor.commit();
        }
    }

    public void stopLocationMonitor() {
        if ((getActivity().getSharedPreferences("locationMonitor", MODE_PRIVATE).getInt("locationMonitor", Context.MODE_PRIVATE)) == -1) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("locationMonitor", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("locationMonitor", -1);
            editor.commit();
        } else {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("locationMonitor", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("locationMonitor", 0);
            editor.commit();
        }
    }
}
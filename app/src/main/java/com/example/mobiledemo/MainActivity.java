package com.example.mobiledemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobiledemo.ui.home.HomeTodoFragment;
import com.example.mobiledemo.ui.notifications.NotificationsFragment;
import com.example.mobiledemo.ui.notifications.TodoEntity;
import com.example.mobiledemo.ui.setting.AppEntity;
import com.example.mobiledemo.utils.DbUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    int fragment = 0;
    private List<LocalDateTime> mDatas;
    String email = "123@123.com";
    String url = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/todo?email=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", Settings.ACTION_USAGE_ACCESS_SETTINGS);
//        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        SharedPreferences volumeMonitorPref = getSharedPreferences("volume_monitor", Context.MODE_PRIVATE);
        SharedPreferences.Editor volumeMonitorEditor = volumeMonitorPref.edit();
        volumeMonitorEditor.putInt("volume_monitor", 0);
        volumeMonitorEditor.putInt("volume_count", 0);
        volumeMonitorEditor.commit();

        try {
            SharedPreferences preferences = getSharedPreferences("appList", MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
            List<AppEntity> applist = new ArrayList<AppEntity>();
            PackageManager packageManager = this.getPackageManager();
            List<PackageInfo> packageInfoList = packageManager .getInstalledPackages(0);
            for (int i = 0; i < packageInfoList.size(); i++) {
                PackageInfo pak = (PackageInfo) packageInfoList.get(i);
                if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                    applist.add(new AppEntity(pak.packageName, 1));
                }
            }
            SharedPreferences.Editor editor = getSharedPreferences("appList", MODE_PRIVATE).edit();
            Gson gson = new Gson();
            String json = gson.toJson(applist);
            Log.d("TAG", "saved json is "+ json);
            editor.putString("appListJson", json);
            editor.commit();
        }
        try {
            fragment = getIntent().getIntExtra("fragment", 0);
            Log.d("TAG", String.valueOf(fragment));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fragment == 0) {
            SharedPreferences sharedPref = getSharedPreferences("thread_killer", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("thread_killer", 0);
            editor.commit();
            Thread thread2 = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    while ((getSharedPreferences("thread_killer", MODE_PRIVATE).getInt("thread_killer", Context.MODE_PRIVATE)) == 0) {
                        try {
                            String currentapp = getRecentTask(getApplicationContext());
                            Thread.sleep(1000);
                            SharedPreferences preferences = getSharedPreferences("appList", MODE_PRIVATE);
                            String json = preferences.getString("appListJson", null);
                            if (json != null)
                            {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<AppEntity>>(){}.getType();
                                List<AppEntity> alterSamples = new ArrayList<AppEntity >();
                                alterSamples = gson.fromJson(json, type);
                                for (int i = 0; i < alterSamples.size(); i++)
                                {
                                    Log.d("TAG", currentapp);
                                    if (alterSamples.get(i).getAppName().equals(currentapp) & alterSamples.get(i).getAppBlocked() == 1 & !alterSamples.get(i).getAppName().equals("com.example.mobiledemo")) {
                                        Log.d("TAG", alterSamples.get(i).getAppName());
                                        int importance = NotificationManager.IMPORTANCE_HIGH;
                                        createNotificationChannel("alert", "stop", importance);
                                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "alert")
                                                .setContentTitle("You open the blocked application!")
                                                .setContentText("Please close it.")
                                                .setWhen(System.currentTimeMillis())
                                                .setSmallIcon(R.drawable.avatar_icon_1)
//                                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                                .setAutoCancel(true)
                                                .build();
                                        manager.notify(100, notification);
                                    }
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName());
                    }
                }
            });
            thread2.start();
        }

        Thread alarm_thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                while (true) {
                    try {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH)+1;
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int min = calendar.get(Calendar.MINUTE);
                        String start_date = "&start_time=" + year+"-"+month+"-"+day;
                        LocalDateTime now = LocalDateTime.of(year, month, day, hour, min);
                        initListData(start_date);

                        Thread.sleep(1000);
                        Log.d("SIZE", String.valueOf(mDatas.size()));
                        for (int i = 0; i < mDatas.size(); i++) {
                            Log.d("TIME", now.toString());
                            Log.d("TIME", mDatas.get(i).toString());
                            if (mDatas.get(i).toString().equals(now.toString())) {
                                int importance = NotificationManager.IMPORTANCE_HIGH;
                                createNotificationChannel("alert", "stop", importance);
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                Notification notification = new NotificationCompat.Builder(getApplicationContext(), "alert")
                                        .setContentTitle("It is time!")
                                        .setContentText("Just do it.")
                                        .setWhen(System.currentTimeMillis())
                                        .setSmallIcon(R.drawable.avatar_icon_1)
//                                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                        .setAutoCancel(true)
                                        .build();
                                manager.notify(100, notification);
                            }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        alarm_thread.start();

        navView.setItemIconTintList(null);
        navView.setItemIconSize(200);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        if (fragment == 3) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, new NotificationsFragment())
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.text_home_container, new HomeTodoFragment())
                    .commit();
        }
        DbUtils.createDb(this, "TimeRecord");
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
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initListData(String start_date) {
//        mDatas = new ArrayList<HotListDataBean>(); //测试无数据情况
        mDatas = new ArrayList<LocalDateTime>(10);
        RequestQueue mQueue = Volley.newRequestQueue(this.getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + "123@123.com" + start_date, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i=0; i<response.length(); i++){
                                String [] start_time_draft = response.getJSONObject(i).getString("start_time").split("[-:T]");
                                int start_year = Integer.parseInt(start_time_draft[0]);
                                int start_mon = Integer.parseInt(start_time_draft[1]);
                                int start_day = Integer.parseInt(start_time_draft[2]);
                                int start_hour = Integer.parseInt(start_time_draft[3]);
                                int start_minute = Integer.parseInt(start_time_draft[4]);
                                int remind = response.getJSONObject(i).getInt("remind");
                                LocalDateTime start_time = LocalDateTime.of(start_year, start_mon, start_day, start_hour, start_minute);
                                if (remind == 1) {
                                    LocalDateTime new_start_time = start_time.minusMinutes(10);
                                    mDatas.add(new_start_time);
                                }
                                else if (remind == 2){
                                    LocalDateTime new_start_time = start_time.minusMinutes(30);
                                    mDatas.add(new_start_time);
                                }
                                else {
                                    mDatas.add(start_time);
                                }
                            }
                            Log.d("TAG", mDatas.size() + "zxc");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(jsonArrayRequest);
    }
}

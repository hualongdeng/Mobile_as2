package com.example.mobiledemo;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobiledemo.ui.dashboard.DashboardFragment;
import com.example.mobiledemo.ui.home.HomeFragment;
import com.example.mobiledemo.ui.notifications.NotificationsFragment;
import com.example.mobiledemo.ui.setting.AppEntity;
import com.example.mobiledemo.utils.DbUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
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
    String url = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/todo?email=";
    String login_account;
    String start_date;
    AudioRecord mAudioRecorder;
    static final int SAMPLE_RATE_IN_HZ = 8000;
    static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(
            SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
            AudioFormat.ENCODING_PCM_16BIT);
    Object mLock;
    Context main_context;
    RequestQueue mQueue;
    JsonArrayRequest jsonArrayRequest;
    TextView voiceShow;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        main_context = this;
        mDatas = new ArrayList<LocalDateTime>();

        if (getRecentTask(main_context) == null) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
        try {
            fragment = getIntent().getIntExtra("fragment", 0);
            Log.d("TAG", String.valueOf(fragment));
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH) + 1;
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        voiceShow = findViewById(R.id.voiceShow);
        start_date = "&start_time=" + year + "-" + month + "-" + day;
        login_account = getSharedPreferences("account", MODE_PRIVATE).getString("account", "");
        mQueue = Volley.newRequestQueue(this);
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + login_account + start_date, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                String[] start_time_draft = response.getJSONObject(i).getString("start_time").split("[-:T]");
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
                                } else if (remind == 2) {
                                    LocalDateTime new_start_time = start_time.minusMinutes(30);
                                    mDatas.add(new_start_time);
                                } else {
                                    mDatas.add(start_time);
                                }
                            }
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

        createNotificationChannel("alert", "stop", NotificationManager.IMPORTANCE_HIGH);
        final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final Notification notification = new NotificationCompat.Builder(getApplicationContext(), "alert")
                .setContentTitle("It is time!")
                .setContentText("Just do it.")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.avatar_icon_1)
                .setAutoCancel(true)
                .build();
        if ((getSharedPreferences("alarm", MODE_PRIVATE).getInt("alarm", Context.MODE_PRIVATE)) == 0) {
            SharedPreferences alarmPref = getSharedPreferences("alarm", Context.MODE_PRIVATE);
            SharedPreferences.Editor alarmEditor = alarmPref.edit();
            alarmEditor.putInt("alarm", 1);
            alarmEditor.commit();
            Thread alarm_thread = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    while ((getSharedPreferences("alarm", MODE_PRIVATE).getInt("alarm", Context.MODE_PRIVATE)) == 1) {
                        try {
                            Calendar calendar1 = Calendar.getInstance();
                            LocalDateTime now = LocalDateTime.of(year, month, day, calendar1.get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE));
                            initListData();
                            Thread.sleep(1000);
                            for (int i = 0; i < mDatas.size(); i++) {
                                if (mDatas.get(i).toString().equals(now.toString())) {
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
        }
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
                    .replace(R.id.nav_host_fragment, new HomeFragment())
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
    private void initListData() {
        mDatas.clear();
        mQueue.add(jsonArrayRequest);
    }

    public void startAppMonitor() {
        SharedPreferences sharedPref = getSharedPreferences("thread_killer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("thread_killer", 1);
        editor.commit();
        Thread thread2 = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                while ((getSharedPreferences("thread_killer", MODE_PRIVATE).getInt("thread_killer", Context.MODE_PRIVATE)) == 1) {
                    try {
                        String currentapp = getRecentTask(main_context);
                        Log.d("TAG", currentapp);
                        Thread.sleep(1000);
                        SharedPreferences preferences = getSharedPreferences("appList", MODE_PRIVATE);
                        String json = preferences.getString("appListJson", null);
                        if (json != null) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<AppEntity>>() {
                            }.getType();
                            List<AppEntity> alterSamples = new ArrayList<AppEntity>();
                            alterSamples = gson.fromJson(json, type);
                            for (int i = 0; i < alterSamples.size(); i++) {
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

    public void startVoiceMonitor() {
        if ((getSharedPreferences("monitor", MODE_PRIVATE).getInt("volume_monitor", Context.MODE_PRIVATE)) == 0) {
            SharedPreferences sharedPref = getSharedPreferences("monitor", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("volume_monitor", 1);
            editor.commit();
            mLock = new Object();
            Thread thread = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    mAudioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                            SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
                            AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
                    mAudioRecorder.startRecording();
                    int voice_count = 0;
                    double voice_sum = 0;
                    while ((getSharedPreferences("monitor", MODE_PRIVATE).getInt("volume_monitor", Context.MODE_PRIVATE)) == 1) {
                        short[] buffer = new short[BUFFER_SIZE];
                        double volume = getVolume(buffer);
                        Fragment fragment1 =  getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().getFragments().get(0);
                        if (fragment1 instanceof DashboardFragment) {
                            ((DashboardFragment) fragment1).update(volume + "");
//                            Log.e("123", "123456778899");
                        }
                        if (voice_count < 15) {
                            voice_count = voice_count + 1;
                            voice_sum = voice_sum + volume;
                        } else {
                            if ((voice_sum/15) > 50) {
                                int importance = NotificationManager.IMPORTANCE_HIGH;
                                createNotificationChannel("alert", "loud", importance);
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                Notification notification = new NotificationCompat.Builder(getApplicationContext(), "alert")
                                        .setContentTitle("Your environment is to loud!")
                                        .setContentText("Please make it quiet.")
                                        .setWhen(System.currentTimeMillis())
                                        .setSmallIcon(R.drawable.avatar_icon_1)
                                        .setAutoCancel(true)
                                        .build();
                                manager.notify(100, notification);
                            }
                            voice_sum = 0;
                            voice_count = 0;
                        }
                        synchronized (mLock) {
                            try {
                                mLock.wait(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    mAudioRecorder.stop();
                    mAudioRecorder.release();
                    mAudioRecorder = null;
                }
            });
            thread.start();
        } else if ((getSharedPreferences("monitor", MODE_PRIVATE).getInt("volume_monitor", Context.MODE_PRIVATE)) == 1) {
            makeToast("Monitor is running.");
        } else {
            makeToast("Voice monitor is unavailable, you enable it in settings");
        }
    }

    private double getVolume(short[] buffer) {
        int r = mAudioRecorder.read(buffer, 0, BUFFER_SIZE);
        long v = 0;
        for (int i = 0; i < buffer.length; i++) {
            v += buffer[i] * buffer[i];
        }
        double mean = v / (double) r;
        double volume = 10 * Math.log10(mean);
        return volume;
    }

    private void makeToast(String response) {
        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        LocationManager locationManager;
        String locationProvider;
        locationManager = (LocationManager) main_context.getSystemService(Context.LOCATION_SERVICE);
        String test = locationManager.toString();
        Log.e("TAG-test", test);
        List<String> providers = locationManager.getProviders(true);
        String output = providers.toString();
        Log.e("TAG-output", output);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, mLocationListener);
            Log.e("BUG", "NETWORK_PROVIDER");
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
            Log.e("BUG", "GPS_PROVIDER");
        } else {
            Log.e("BUG", "No Location Provider, Please check permission");
            return null;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        Location location = locationManager.getLastKnownLocation(locationProvider);
        return location;
    }

    public void startLocMonitor() {
        if ((getSharedPreferences("locationMonitor", MODE_PRIVATE).getInt("locationMonitor", Context.MODE_PRIVATE)) == 0) {
            SharedPreferences sharedPref = getSharedPreferences("locationMonitor", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("locationMonitor", 1);
            editor.commit();
            Thread thread2 = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    Location first_location = getLocation();
                    while ((getSharedPreferences("locationMonitor", MODE_PRIVATE).getInt("locationMonitor", Context.MODE_PRIVATE)) == 1) {
                        try {
                            Thread.sleep(10000);
                            Log.e("abd", getLocation().getLatitude() + "abd" + getLocation().getLongitude());
                            Log.e("distance", String.valueOf(getLocation().distanceTo(first_location)));
                            if (getLocation().distanceTo(first_location) > 30) {
                                int importance = NotificationManager.IMPORTANCE_HIGH;
                                createNotificationChannel("alert", "location", importance);
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                Notification notification = new NotificationCompat.Builder(getApplicationContext(), "alert")
                                        .setContentTitle("You have moved!")
                                        .setContentText("Please go back.")
                                        .setWhen(System.currentTimeMillis())
                                        .setSmallIcon(R.drawable.avatar_icon_1)
//                                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                        .setAutoCancel(true)
                                        .build();
                                manager.notify(100, notification);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName());
                    }
                }
            });
            thread2.start();
        } else if ((getSharedPreferences("locationMonitor", MODE_PRIVATE).getInt("locationMonitor", Context.MODE_PRIVATE)) == 1){
            makeToast("Monitor is running now.");
        } else {
            makeToast("GPS monitor is unavailable, you enable it in settings");
        }
    }

    private static LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("TAG", "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("TAG", "onProviderEnabled");

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("TAG", "onProviderDisabled");

        }

        @Override
        public void onLocationChanged(Location location) {
        }
    };

}

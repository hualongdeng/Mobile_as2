package com.example.mobiledemo;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.mobiledemo.ui.home.HomeTodoFragment;
import com.example.mobiledemo.ui.notifications.NotificationsFragment;
import com.example.mobiledemo.ui.todo_new.TodoNewFragment;
import com.example.mobiledemo.utils.DbUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.lang.reflect.Field;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int fragment = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        Log.d("TAG", getLollipopRecentTask(this));
        navView.setItemIconTintList(null);
        navView.setItemIconSize(200);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        try {
            fragment = getIntent().getIntExtra("fragment", 0);
            Log.d("TAG", String.valueOf(fragment));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment == 2) {
            Log.d("TAG", "cal");
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

    public static String getLollipopRecentTask(Context context) {
        final int PROCESS_STATE_TOP = 2;
        try {
            //通过反射获取私有成员变量processState，稍后需要判断该变量的值
            Field processStateField = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
            List<ActivityManager.RunningAppProcessInfo> processes = ((ActivityManager) context.getSystemService(
                    Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo process : processes) {
                //判断进程是否为前台进程
                if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    int state = processStateField.getInt(process);
                    //processState值为2
                    if (state == PROCESS_STATE_TOP) {
                        String[] packname = process.pkgList;
                        return packname[0];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}

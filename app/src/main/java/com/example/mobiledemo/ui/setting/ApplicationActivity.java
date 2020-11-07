package com.example.mobiledemo.ui.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.notifications.Decoration;
import com.example.mobiledemo.ui.notifications.TodoEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ApplicationActivity extends AppCompatActivity {
    private ApplicationViewModel applicationViewModel;
    public RecyclerView mRecyclerView;
    public View root;
    private ApplicationAdapter mAdapter;
    private List<AppEntity> mDatas;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_list);
        applicationViewModel = ViewModelProviders.of(this).get(ApplicationViewModel.class);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_application_list);
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.todo_edit_container, new TodoEditFragment())
//                .commit();
        final Button backButton = findViewById(R.id.application_list_back);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initListData();
        mAdapter = new ApplicationAdapter(mDatas, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new Decoration());
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplicationActivity.this, MonitorSettingActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initListData() {
//        mDatas = new ArrayList<HotListDataBean>(); //测试无数据情况
        mDatas = new ArrayList<AppEntity>();
        SharedPreferences preferences = getSharedPreferences("appList", MODE_PRIVATE);
        String json = preferences.getString("appListJson", null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<AppEntity>>() {
            }.getType();
            mDatas = gson.fromJson(json, type);
        }
//        mDatas = new ArrayList<String>(10);
//        PackageManager packageManager = this.getPackageManager();
//        List<PackageInfo> packageInfoList = packageManager .getInstalledPackages(0);
//        for (int i = 0; i < packageInfoList.size(); i++) {
//            PackageInfo pak = (PackageInfo)packageInfoList.get(i);
//            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
//                mDatas.add(pak.packageName);
//            } else
//            {
//                //系统应用
//            }
//        }
    }
}

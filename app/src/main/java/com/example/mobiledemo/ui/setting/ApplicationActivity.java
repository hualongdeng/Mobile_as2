package com.example.mobiledemo.ui.setting;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class ApplicationActivity extends AppCompatActivity {
    private ApplicationViewModel applicationViewModel;
    public RecyclerView mRecyclerView;
    public View root;
    private ApplicationAdapter mAdapter;
    private List<String> mDatas;
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

        /*1,设置管理器*/
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*2,设置适配器*/
        initListData();
        mAdapter = new ApplicationAdapter(mDatas);
        mRecyclerView.setAdapter(mAdapter);
        /*3,添加item的添加和移除动画, 这里我们使用系统默认的动画*/
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        /*4,添加分割线，自定义分割线，分割线必须要自己定义，系统没有默认分割线*/
        mRecyclerView.addItemDecoration(new Decoration());
        /*设置条目点击事件*/
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
        mDatas = new ArrayList<String>(10);
        PackageManager packageManager = this.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager .getInstalledPackages(0);
        for (int i = 0; i < packageInfoList.size(); i++) {
            PackageInfo pak = (PackageInfo)packageInfoList.get(i);
            //判断是否为系统预装的应用
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // 第三方应用
                // apps.add(pak);
                mDatas.add(pak.packageName);
            } else
            {
                //系统应用
            }
        }
    }
}

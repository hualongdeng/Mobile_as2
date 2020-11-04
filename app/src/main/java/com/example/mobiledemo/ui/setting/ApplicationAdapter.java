package com.example.mobiledemo.ui.setting;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiledemo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.mobiledemo.MainActivity.getRecentTask;

public class ApplicationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AppEntity> mDatas;
    private LayoutInflater mInflater;
    private ApplicationAdapter.OnItemClickListener onItemClickListener;
    private final int NO_DATA = 0, TEXT_VIEW = 2;
    Context activity_content;

    public ApplicationAdapter(List<AppEntity> mDatas, Context context) {
        this.mDatas = mDatas;
        activity_content = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(mDatas.size() <= 0){ //无数据情况处理
            return NO_DATA;
        }
        return TEXT_VIEW;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder mHolder = null;
        switch (viewType){
            case NO_DATA:
                mHolder = new ApplicationAdapter.TextViewHolder(mInflater.inflate(R.layout.application_list_cardview, parent, false));
                break;
            case TEXT_VIEW:
                mHolder = new ApplicationAdapter.TextViewHolder(mInflater.inflate(R.layout.application_list_cardview, parent, false));
                break;
        }
        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(mDatas.size() <= 0){ //无数据的情况
            return;
        }
        ApplicationAdapter.TextViewHolder textViewHolder = (ApplicationAdapter.TextViewHolder) holder;
        textViewHolder.application_name.setText(mDatas.get(position).getAppName());
        if (mDatas.get(position).getAppBlocked() == 0) {
            textViewHolder.application_switch.setChecked(false);
        } else {
            textViewHolder.application_switch.setChecked(true);
        }
        textViewHolder.application_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int thread_id = activity_content.getSharedPreferences("thread_killer", MODE_PRIVATE).getInt("thread_killer", Context.MODE_PRIVATE);
                if (isChecked){
                    mDatas.get(position).setBlocked(1);
                    SharedPreferences.Editor editor = activity_content.getSharedPreferences("appList", MODE_PRIVATE).edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(mDatas);
                    Log.d("TAG", "saved json is "+ json);
                    editor.putString("appListJson", json);
                    editor.commit();

                    SharedPreferences sharedPref = activity_content.getSharedPreferences("thread_killer", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPref.edit();
                    editor1.putInt("thread_killer", thread_id + 1);
                    editor1.commit();

                    Log.d("TAG", String.valueOf(activity_content.getSharedPreferences("thread_killer", MODE_PRIVATE).getInt("thread_killer", Context.MODE_PRIVATE)));
                    final int new_thread = activity_content.getSharedPreferences("thread_killer", MODE_PRIVATE).getInt("thread_killer", Context.MODE_PRIVATE);

                    Thread thread2 = new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            while ((activity_content.getSharedPreferences("thread_killer", MODE_PRIVATE).getInt("thread_killer", Context.MODE_PRIVATE)) == new_thread) {
                                try {
                                    String currentapp = getRecentTask(activity_content);
                                    Thread.sleep(1000);
                                    SharedPreferences preferences = activity_content.getSharedPreferences("appList", MODE_PRIVATE);
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
                                                NotificationManager manager = (NotificationManager) activity_content.getSystemService(NOTIFICATION_SERVICE);
                                                Notification notification = new NotificationCompat.Builder(activity_content, "alert")
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
                    //选中状态 可以做一些操作

                }else {
                    mDatas.get(position).setBlocked(0);
                    SharedPreferences.Editor editor = activity_content.getSharedPreferences("appList", MODE_PRIVATE).edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(mDatas);
                    Log.d("TAG", "saved json is "+ json);
                    editor.putString("appListJson", json);
                    editor.commit();

                    SharedPreferences sharedPref = activity_content.getSharedPreferences("thread_killer", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPref.edit();
                    editor1.putInt("thread_killer", thread_id + 1);
                    editor1.commit();

                    final int new_thread = activity_content.getSharedPreferences("thread_killer", MODE_PRIVATE).getInt("thread_killer", Context.MODE_PRIVATE);

                    Thread thread2 = new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            while ((activity_content.getSharedPreferences("thread_killer", MODE_PRIVATE).getInt("thread_killer", Context.MODE_PRIVATE)) == new_thread) {
                                try {
                                    String currentapp = getRecentTask(activity_content);
                                    Thread.sleep(1000);
                                    SharedPreferences preferences = activity_content.getSharedPreferences("appList", MODE_PRIVATE);
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
                                                NotificationManager manager = (NotificationManager) activity_content.getSystemService(NOTIFICATION_SERVICE);
                                                Notification notification = new NotificationCompat.Builder(activity_content, "alert")
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

                    //未选中状态 可以做一些操作

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size()>0 ? mDatas.size() : 1; //这里在数据为空的情况下返回1，为了显示无数据的布局
    }

    public void setOnItemClickListener(ApplicationAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 文字item的holder
     */
    class TextViewHolder extends RecyclerView.ViewHolder{

        private TextView application_name;
        private Switch application_switch;
        public TextViewHolder(View itemView) {
            super(itemView);
            application_name = (TextView) itemView.findViewById(R.id.application_list_item);
            application_switch = itemView.findViewById(R.id.application_list_switch);
        }
    }

    /**
     * 适配器的点击事件接口
     */
    public interface OnItemClickListener{
        void OnItemClick(View v, int position);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) activity_content.getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }
}

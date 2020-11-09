package com.example.mobiledemo.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceFragmentCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.account.AccountActivity;
import com.example.mobiledemo.ui.login.LoginActivity;
import com.example.mobiledemo.ui.notifications.Decoration;
import com.example.mobiledemo.ui.notifications.TodoEntity;
import com.example.mobiledemo.ui.register.RegisterActivity;
import com.example.mobiledemo.ui.setting.SettingActivity;
import com.example.mobiledemo.ui.todo_new.TodoNewFragment;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    public RecyclerView mRecyclerView;
    public View root;
    private MyAdapter mAdapter;
    private List<TodoEntity> mDatas;
    String start_date;
    String todo_url = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/todo?email=";
    String user_url = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/user?email=";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        final Button accountButton = root.findViewById(R.id.account);
        final Button settingButton = root.findViewById(R.id.setting);
        TextView nicknameView = root.findViewById(R.id.text_home_name);

        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
            }
        });
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view_home);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+2;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        start_date = "&start_time=" + year+"-"+month+"-"+day;
        initListData(start_date, nicknameView);
        mAdapter = new MyAdapter(mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initListData(String start_date, final TextView nicknameView) {
        mDatas = new ArrayList<TodoEntity>(10);
        RequestQueue mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String login_account = getActivity().getSharedPreferences("account", MODE_PRIVATE).getString("account", "");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, todo_url + login_account + start_date, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i=0; i<response.length(); i++){
                                int id = response.getJSONObject(i).getInt("id");
                                String [] start_time_draft = response.getJSONObject(i).getString("start_time").split("[-:T]");
                                String [] end_time_draft = response.getJSONObject(i).getString("end_time").split("[-:T]");
                                int start_year = Integer.parseInt(start_time_draft[0]);
                                int start_mon = Integer.parseInt(start_time_draft[1]);
                                int start_day = Integer.parseInt(start_time_draft[2]);
                                int start_hour = Integer.parseInt(start_time_draft[3]);
                                int start_minute = Integer.parseInt(start_time_draft[4]);
                                int end_year = Integer.parseInt(end_time_draft[0]);
                                int end_mon = Integer.parseInt(end_time_draft[1]);
                                int end_day = Integer.parseInt(end_time_draft[2]);
                                int end_hour = Integer.parseInt(end_time_draft[3]);
                                int end_minute = Integer.parseInt(end_time_draft[4]);
                                LocalDateTime start_time = LocalDateTime.of(start_year, start_mon, start_day, start_hour, start_minute);
                                LocalDateTime end_time = LocalDateTime.of(end_year, end_mon, end_day, end_hour, end_minute);
                                String title = response.getJSONObject(i).getString("title");
                                String email = response.getJSONObject(i).getString("email");
                                String place = response.getJSONObject(i).getString("location");
                                int remind = response.getJSONObject(i).getInt("remind");
                                int repeat = response.getJSONObject(i).getInt("repeat");
                                TodoEntity dataBean = new TodoEntity(id, start_time, end_time, title, email, place,remind, repeat);
                                mDatas.add(dataBean);
                            }
                            mAdapter.notifyDataSetChanged();
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
        JsonArrayRequest jsonArrayRequest_nickname = new JsonArrayRequest(Request.Method.GET, user_url + login_account + start_date, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i=0; i<response.length(); i++){
                                nicknameView.setText("Hi, " + response.getJSONObject(i).getString("nickname"));
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
        mQueue.add(jsonArrayRequest_nickname);
    }
}
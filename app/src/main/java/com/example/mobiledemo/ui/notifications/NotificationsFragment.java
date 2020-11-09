package com.example.mobiledemo.ui.notifications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.todo.TodoEditActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    public RecyclerView mRecyclerView;
    public View root;
    private MyAdapter mAdapter;
    private List<TodoEntity> mDatas;
    String start_date;
    String url = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/todo?email=";
//    String url = "http://10.0.2.2:5000/";


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final Button todo_edit = root.findViewById(R.id.todo_edit);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        start_date = "&start_time=" + year+"-"+month+"-"+day;
        initListData(start_date);
        CalendarView myCalendar = (CalendarView) root.findViewById(R.id.calendarview);
        CalendarView.OnDateChangeListener myCalendarListener = new CalendarView.OnDateChangeListener(){
            public void onSelectedDayChange(CalendarView view, int year, int month, int day){
                start_date = "&start_time=" + year+"-"+ (month+1) +"-"+day;
                Log.d("NEW_DATE", start_date);
                initListData(start_date);
                mAdapter = new MyAdapter(mDatas);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        };
        myCalendar.setOnDateChangeListener(myCalendarListener);
        mAdapter = new MyAdapter(mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new Decoration());



//        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
//            @Override
//            public void OnItemClick(View v, int position) {
//                Toast.makeText(getContext(), mDatas.get(position).getTitle(), Toast.LENGTH_SHORT).show();
//                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
//                builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                AlertDialog dialog2 = builder2.create();
//                dialog2.setTitle("Do you want to cancel?");
//                dialog2.show();
//            }
//        });
        todo_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TodoEditActivity.class);
                intent.putExtra("date", start_date);
                startActivity(intent);
            }
        });
        return root;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initListData(String start_date) {
//        mDatas = new ArrayList<HotListDataBean>(); //测试无数据情况
        mDatas = new ArrayList<TodoEntity>(10);
        RequestQueue mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String login_account = getActivity().getSharedPreferences("account", MODE_PRIVATE).getString("account", "");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + login_account + start_date, null,
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

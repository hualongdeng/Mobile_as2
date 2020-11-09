package com.example.mobiledemo.ui.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.notifications.Decoration;
import com.example.mobiledemo.ui.notifications.TodoEntity;
import com.example.mobiledemo.ui.todo_new.TodoNewActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TodoEditActivity extends AppCompatActivity {

    private TodoEditViewModel todoEditViewModel;
    public RecyclerView mRecyclerView;
    public View root;
    private MyAdapter mAdapter;
    private List<TodoEntity> mDatas;
    String url = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/todo?email=";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_edit);
        todoEditViewModel = ViewModelProviders.of(this).get(TodoEditViewModel.class);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_todo_edit);
        final Button backButton = findViewById(R.id.todo_edit_back);
        final Button addNewButton = findViewById(R.id.todo_add_new);

        final String start_date = getIntent().getStringExtra("date");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initListData(start_date);
        mAdapter = new MyAdapter(mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new Decoration());
        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Intent intent = new Intent(TodoEditActivity.this, TodoNewActivity.class);
                Bundle bundle = new Bundle();
                List<TodoEntity> item = new ArrayList<TodoEntity>();
                item.add(mDatas.get(position));
                bundle.putSerializable("data", (Serializable) item);
                intent.putExtras(bundle);
                intent.putExtra("date", start_date);
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoEditActivity.this, MainActivity.class);
                intent.putExtra("fragment", 3);
                startActivity(intent);
            }
        });
        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoEditActivity.this, TodoNewActivity.class);
                intent.putExtra("date", start_date);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initListData(String start_date) {
        mDatas = new ArrayList<TodoEntity>(10);
        RequestQueue mQueue = Volley.newRequestQueue(this.getApplicationContext());
        String login_account = getSharedPreferences("account", MODE_PRIVATE).getString("account", "");
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

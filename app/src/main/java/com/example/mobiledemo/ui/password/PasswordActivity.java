package com.example.mobiledemo.ui.password;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.Gravity;
import android.widget.Toast;
import android.widget.Toast;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.account.AccountActivity;
import com.example.mobiledemo.ui.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class PasswordActivity extends AppCompatActivity {

    private PasswordViewModel passwordViewModel;
    private String url = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/user?email=";
    private String updateurl = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/user_update";
    private String oldpw_me;
    private String gender_me;
    private String email_me;
    private String avatar_me;
    private String birthday_me;
    private String nickname_me;
    private String location_me;
    private String phone_me;
    private String inputNewpw;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        passwordViewModel = ViewModelProviders.of(this).get(PasswordViewModel.class);
        final Button backButton = findViewById(R.id.password_back);
        final Button saveButton = findViewById(R.id.password_save);
        initinf();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePw();
            }
        });
    }

    private void initinf() {
        String login_account = this.getSharedPreferences("account", MODE_PRIVATE).getString("account", "");
        RequestQueue mQueue = Volley.newRequestQueue(this.getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + login_account, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            avatar_me = response.getJSONObject(0).getString("avatar");
                            birthday_me = response.getJSONObject(0).getString("birthday");
                            email_me = response.getJSONObject(0).getString("email");
                            gender_me = response.getJSONObject(0).getString("gender");
                            location_me = response.getJSONObject(0).getString("location");
                            nickname_me = response.getJSONObject(0).getString("nickname");
                            oldpw_me = response.getJSONObject(0).getString("password");
                            phone_me = response.getJSONObject(0).getString("phone");
                            // String place = response.getJSONObject(i).getString("location");
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "catch exception", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Network error, please check.", Toast.LENGTH_SHORT).show();
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

    private void ChangePw()  {
        final EditText oldPwtext = (EditText) findViewById(R.id.oldpw);
        final EditText newPwtext = (EditText) findViewById(R.id.newpw);
        final EditText confirmPwtext = (EditText) findViewById(R.id.comfpw);
        String inputOldpw = oldPwtext.getText().toString();
        inputNewpw = newPwtext.getText().toString();
        String inputConfpw = confirmPwtext.getText().toString();

        Log.d("TAG-old", oldpw_me);
        Log.d("TAG-input", inputOldpw);
        if (oldpw_me.equals(inputOldpw)){
            if (inputNewpw.equals(inputConfpw)){
                RequestQueue updateQueue = Volley.newRequestQueue(this.getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, updateurl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG-target", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG-err", error.getMessage(), error);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("avatar", avatar_me);
                        map.put("birthday", birthday_me);
                        map.put("email", email_me);
                        map.put("gender", gender_me);
                        map.put("location", location_me);
                        map.put("nickname", nickname_me);
                        map.put("old_password", oldpw_me);
                        map.put("phone", phone_me);
                        map.put("new_password", inputNewpw);
                        map.put("username", "admin");
                        return map;
                    }
                };
                updateQueue.add(stringRequest);
                Toast.makeText(getApplicationContext(), "Successfully change password", Toast.LENGTH_SHORT).show();
                initinf();
                Intent intent = new Intent(PasswordActivity.this, AccountActivity.class);
                startActivity(intent);
            }else{
                Toast t = Toast.makeText(getApplicationContext(),"Confirm password should as same as the new one",Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER,0,0);
                t.show();
            }

        }else{
            Toast t3 = Toast.makeText(getApplicationContext(),"Wrong password, please check",Toast.LENGTH_LONG);
            t3.setGravity(Gravity.CENTER,0,0);
            t3.show();
        }
    }
}
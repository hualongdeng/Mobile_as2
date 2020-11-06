package com.example.mobiledemo.ui.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobiledemo.MainActivity;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.login.LoginActivity;
import com.example.mobiledemo.ui.login.LoginViewModel;
import com.example.mobiledemo.ui.login.LoginViewModelFactory;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;
    String url = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/user";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        final Button registerButton = findViewById(R.id.register_register);
        final Button cancelButton = findViewById(R.id.register_cancel);
        final EditText usernameEditText = findViewById(R.id.register_username);
        final EditText passwordEditText = findViewById(R.id.register_password);
        final EditText birthdayEditText = findViewById(R.id.register_birthday);
        final EditText phoneEditText = findViewById(R.id.register_phone);
        final CheckBox check = findViewById(R.id.register_checkBox);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check.isChecked() == true){
                    RequestQueue mQueue = Volley.newRequestQueue(RegisterActivity.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("TAG", response);
                            if (response.equals("True")) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterFailed("Create successfully");
                                startActivity(intent);
                                setResult(Activity.RESULT_OK);
                                finish();
                            } else {
                                RegisterFailed(response);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", error.getMessage(), error);
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("email", usernameEditText.getText().toString());
                            map.put("password", passwordEditText.getText().toString());
                            map.put("birthday", birthdayEditText.getText().toString());
                            map.put("phone", phoneEditText.getText().toString());
                            map.put("avatar", "1");
                            map.put("nickname", "default");
                            map.put("gender", "default");
                            map.put("location", "default");
                            return map;
                        }
                    };
                    mQueue.add(stringRequest);
                } else {
                    RegisterFailed("You need to accept the policy.");
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void RegisterFailed(String response) {
        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
    }
}
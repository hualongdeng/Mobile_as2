package com.example.mobiledemo.ui.todo_new;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.notifications.TodoEntity;
import com.example.mobiledemo.ui.todo.TodoEditActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class TodoNewFragment extends PreferenceFragmentCompat {
    List<TodoEntity> todo;
    EditTextPreference titleView, placeView;
    Preference startTimeView, endTimeView;
    ListPreference remindView, repeatView;
    String title = "Click to edit";
    String email = "";
    String place = "Click to edit";
    int id = 0;
    int repeat = 0;
    int remind = 0;
    int start_year = 0;
    int start_month = 0;
    int start_day = 0;
    int start_hour = 0;
    int start_min = 0;
    int end_year = 0;
    int end_month = 0;
    int end_day = 0;
    int end_hour = 0;
    int end_min = 0;
    int new_or_not = 0;
    String get_url = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/todo";
//    String update_url = "http://10.0.2.2:5000/todo_update";
    String update_url = "http://flask-env.eba-kdpr8bpk.us-east-1.elasticbeanstalk.com/todo_update";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.todo_new, rootKey);
        titleView = (EditTextPreference) findPreference("todo_new_title");
        startTimeView = findPreference("todo_new_start");
        endTimeView = findPreference("todo_new_end");
        placeView = findPreference("todo_new_place");
        remindView = (ListPreference) findPreference("todo_new_remind");
        repeatView = (ListPreference) findPreference("todo_new_repeat");
        titleView.setSummaryProvider(new Preference.SummaryProvider<EditTextPreference>() {
            @Override
            public CharSequence provideSummary(EditTextPreference preference) {
                String text = titleView.getText();
                return text;
            }
        });
        placeView.setSummaryProvider(new Preference.SummaryProvider<EditTextPreference>() {
            @Override
            public CharSequence provideSummary(EditTextPreference preference) {
                String text = placeView.getText();
                return text;
            }
        });
        remindView.setSummaryProvider(new Preference.SummaryProvider<ListPreference>() {
            @Override
            public CharSequence provideSummary(ListPreference preference) {
                String text = String.valueOf(remindView.getEntries()[Integer.parseInt(remindView.getValue())]);
                return text;
            }
        });
        repeatView.setSummaryProvider(new Preference.SummaryProvider<ListPreference>() {
            @Override
            public CharSequence provideSummary(ListPreference preference) {
                String text = String.valueOf(repeatView.getEntries()[Integer.parseInt(repeatView.getValue())]);
                return text;
            }
        });
        try {
            todo =  (List<TodoEntity>) getActivity().getIntent().getSerializableExtra("data");
            id = todo.get(0).getId();
            title = todo.get(0).getTitle();
            email = todo.get(0).getEmail();
            start_year = todo.get(0).getStartYear();
            start_month = todo.get(0).getStartMonth();
            start_day = todo.get(0).getStartDay();
            start_hour = todo.get(0).getStartHour();
            start_min = todo.get(0).getStartMin();
            end_year = todo.get(0).getEndYear();
            end_month = todo.get(0).getEndMonth();
            end_day = todo.get(0).getEndDay();
            end_hour = todo.get(0).getEndHour();
            end_min = todo.get(0).getEndMin();
            place = todo.get(0).getPlace();
            repeat = todo.get(0).getRepeat();
            remind = todo.get(0).getRemind();

            titleView.setText(title);
            startTimeView.setSummary(getTimeString(start_year, start_month, start_day, start_hour, start_min));
            endTimeView.setSummary(getTimeString(end_year, end_month, end_day, end_hour, end_min));
            placeView.setText(place);
            remindView.setValueIndex(remind);
            repeatView.setValueIndex(repeat);

        } catch (Exception e) {
            titleView.setText("Click to edit");
            placeView.setText("Click to edit");
            remindView.setValueIndex(0);
            repeatView.setValueIndex(0);
            email = getActivity().getSharedPreferences("account", MODE_PRIVATE).getString("account", "");
            Calendar calendar = Calendar.getInstance();
            start_year = calendar.get(Calendar.YEAR);
            start_month = calendar.get(Calendar.MONTH)+1;
            start_day = calendar.get(Calendar.DAY_OF_MONTH);
            start_hour = calendar.get(Calendar.HOUR_OF_DAY);
            start_min = calendar.get(Calendar.MINUTE);
            end_year = calendar.get(Calendar.YEAR);
            end_month = calendar.get(Calendar.MONTH)+1;
            end_day = calendar.get(Calendar.DAY_OF_MONTH);
            end_hour = calendar.get(Calendar.HOUR_OF_DAY);
            end_min = calendar.get(Calendar.MINUTE);

            new_or_not = 1;
        }

        Preference save_button = findPreference("todo_save_new");
        Preference start_button = findPreference("todo_new_start");
        Preference end_button = findPreference("todo_new_end");
        Preference delete_button = findPreference("todo_delete");
        if (new_or_not == 1) {
            delete_button.setVisible(false);
        }
        if (delete_button != null) {
            delete_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    RequestQueue mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    StringRequest deleteRequest = new StringRequest(Request.Method.GET, update_url + "?id=" + id, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("TAG", response);
                            String start_date = getActivity().getIntent().getStringExtra("date");
                            Intent intent = new Intent(getActivity(), TodoEditActivity.class);
                            intent.putExtra("date", start_date);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("TAG", error.getMessage(), error);
                        }
                    });
                    mQueue.add(deleteRequest);
                    return true;
                }
            });
        }
        if (save_button != null) {
            save_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    RequestQueue mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    String url;
                    final String login_account = getActivity().getSharedPreferences("account", MODE_PRIVATE).getString("account", "");
                    if (new_or_not == 1) {
                        url = get_url;
                    } else {
                        url = update_url;
                    }
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("TAG", response);
                            String start_date = getActivity().getIntent().getStringExtra("date");
                            Intent intent = new Intent(getActivity(), TodoEditActivity.class);
                            intent.putExtra("date", start_date);
                            startActivity(intent);
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
                            if (new_or_not == 1) {
                                map.put("title", titleView.getText());
                                map.put("email", login_account);
                                map.put("start_time", getTimeString(start_year, start_month, start_day, start_hour, start_min));
                                map.put("end_time", getTimeString(end_year, end_month, end_day, end_hour, end_min));
                                map.put("location", placeView.getText());
                                map.put("remind", String.valueOf(remindView.findIndexOfValue(remindView.getValue())));
                                map.put("repeat", String.valueOf(repeatView.findIndexOfValue(repeatView.getValue())));
                            } else {
                                map.put("id", String.valueOf(id));
                                map.put("title", titleView.getText());
                                map.put("start_time", getTimeString(start_year, start_month, start_day, start_hour, start_min));
                                Log.d("TAG", start_month + "zxc");
                                map.put("end_time", getTimeString(end_year, end_month, end_day, end_hour, end_min));
                                Log.d("TAG", end_month + "zxc");
                                map.put("location", placeView.getText());
                                map.put("remind", String.valueOf(remindView.findIndexOfValue(remindView.getValue())));
                                map.put("repeat", String.valueOf(repeatView.findIndexOfValue(repeatView.getValue())));
                            }
                            return map;
                        }
                    };
                    mQueue.add(stringRequest);
                    return true;
                }
            });
        }
        if (start_button != null) {
            start_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                int new_start_year = 0;
                int new_start_month = 0;
                int new_start_day = 0;
                int new_start_hour = 0;
                int new_start_min = 0;
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            start_year = new_start_year;
                            start_month = new_start_month + 1;
                            start_day = new_start_day;
                            start_hour = new_start_hour;
                            start_min = new_start_min;
                            startTimeView.setSummary(getTimeString(start_year, start_month, start_day, start_hour, start_min));
                            dialog.dismiss();
                        }
                    });
                    builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog1 = builder1.create();
                    View dialogView1 = View.inflate(getActivity(), R.layout.start_timepicker, null);
                    DatePicker datepicker = dialogView1.findViewById(R.id.datePicker);
                    TimePicker timePicker = (TimePicker) dialogView1.findViewById(R.id.timePicker);
                    timePicker.setIs24HourView(true);
                    if (new_or_not == 0) {
                        timePicker.setHour(start_hour);
                        timePicker.setMinute(start_min);
                        datepicker.updateDate(start_year, start_month -1, start_day);
                    } else {
                        timePicker.setHour(start_hour);
                        timePicker.setMinute(start_min);
                        datepicker.updateDate(start_year, start_month -1, start_day);
                    }
                    new_start_year = datepicker.getYear();
                    new_start_month = datepicker.getMonth();
                    new_start_day = datepicker.getDayOfMonth();
                    new_start_hour = timePicker.getHour();
                    new_start_min = timePicker.getMinute();
                    datepicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                            new_start_year = year;
                            new_start_month = month;
                            new_start_day = day;
                        }
                    });
                    timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            new_start_hour = hourOfDay;
                            new_start_min = minute;
                        }
                    });
                    dialog1.setTitle("Time setting");
                    dialog1.setView(dialogView1);
                    dialog1.show();
                    return true;
                }
            });
        }
        if (end_button != null) {
            end_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                int new_end_year = 0;
                int new_end_month = 0;
                int new_end_day = 0;
                int new_end_hour = 0;
                int new_end_min = 0;
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                    builder2.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            end_year = new_end_year;
                            end_month = new_end_month + 1;
                            end_day = new_end_day;
                            end_hour = new_end_hour;
                            end_min = new_end_min;
                            endTimeView.setSummary(getTimeString(end_year, end_month, end_day, end_hour, end_min));
                            dialog.dismiss();
                        }
                    });
                    builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog2 = builder2.create();
                    View dialogView2 = View.inflate(getActivity(), R.layout.start_timepicker, null);
                    DatePicker datepicker = dialogView2.findViewById(R.id.datePicker);
                    TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.timePicker);
                    timePicker.setIs24HourView(true);
                    if (new_or_not == 0) {
                        timePicker.setHour(end_hour);
                        timePicker.setMinute(end_min);
                        datepicker.updateDate(end_year, end_month -1, end_day);
                    } else {
                        timePicker.setHour(start_hour);
                        timePicker.setMinute(start_min);
                        datepicker.updateDate(start_year, start_month -1, start_day);
                    }
                    new_end_year = datepicker.getYear();
                    new_end_month = datepicker.getMonth();
                    new_end_day = datepicker.getDayOfMonth();
                    new_end_hour = timePicker.getHour();
                    new_end_min = timePicker.getMinute();
                    datepicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                            new_end_year = year;
                            new_end_month = month;
                            new_end_day = day;
                        }
                    });
                    timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            new_end_hour = hourOfDay;
                            new_end_min = minute;
                        }
                    });
                    dialog2.setTitle("Time setting");
                    dialog2.setView(dialogView2);
                    dialog2.show();
                    return true;
                }
            });
        }
    }

    public String getTimeString(int year, int month, int day, int hour, int min) {
        String mon;
        String day1;
        String hour1;
        String min1;
        if (month < 9) {
            mon = "0" + (month);
        } else {
            mon = String.valueOf(month);
        }
        if (day < 10) {
            day1 = "0" + day;
        } else {
            day1 = String.valueOf(day);
        }
        if (hour < 10) {
            hour1 = "0" + hour;
        } else {
            hour1 = String.valueOf(hour);
        }
        if (min < 10) {
            min1 = "0" + min;
        } else {
            min1 = String.valueOf(min);
        }
        return year + "-" + mon + "-" + day1 + " " + hour1 + ":" + min1 + ":00";
    }
}

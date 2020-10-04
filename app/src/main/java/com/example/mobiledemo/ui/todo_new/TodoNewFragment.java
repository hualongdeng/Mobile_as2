package com.example.mobiledemo.ui.todo_new;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.report.ReportActivity;
import com.example.mobiledemo.ui.todo.TodoEditActivity;

public class TodoNewFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.todo_new, rootKey);
        Preference monitor_button = findPreference("todo_save_new");
        Preference start_button = findPreference("todo_new_start");
        Preference end_button = findPreference("todo_new_end");
        if (monitor_button != null) {
            monitor_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    Intent intent = new Intent(getActivity(), TodoEditActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }
        if (start_button != null) {
            start_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                    builder2.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
                    TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.start_timePicker);
                    timePicker.setIs24HourView(true);
                    dialog2.setTitle("Time setting");
                    dialog2.setView(dialogView2);
                    dialog2.show();
                    return true;
                }
            });
        }
        if (end_button != null) {
            end_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                    builder2.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
                    TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.start_timePicker);
                    timePicker.setIs24HourView(true);
                    dialog2.setTitle("Time setting");
                    dialog2.setView(dialogView2);
                    dialog2.show();
                    return true;
                }
            });
        }
    }
}

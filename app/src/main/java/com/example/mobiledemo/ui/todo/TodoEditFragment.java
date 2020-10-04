package com.example.mobiledemo.ui.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.report.ReportActivity;
import com.example.mobiledemo.ui.todo_new.TodoNewActivity;

public class TodoEditFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.todo_edit, rootKey);
        Preference monitor_button = findPreference("todo_add_new");
        Preference event_button = findPreference("todo_event1");
        Preference task_button = findPreference("todo_task1");
        if (monitor_button != null) {
            monitor_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    Intent intent = new Intent(getActivity(), TodoNewActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }
        if (event_button != null) {
            event_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                    builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                    dialog2.setTitle("Do you want to cancel?");
                    dialog2.show();
                    return true;
                }
            });
        }
        if (task_button != null) {
            task_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                    builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                    dialog2.setTitle("Do you want to cancel?");
                    dialog2.show();
                    return true;
                }
            });
        }
    }
}

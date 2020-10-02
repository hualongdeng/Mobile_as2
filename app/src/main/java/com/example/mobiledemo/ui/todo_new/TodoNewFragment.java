package com.example.mobiledemo.ui.todo_new;

import android.content.Intent;
import android.os.Bundle;

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
    }
}

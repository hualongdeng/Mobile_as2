package com.example.mobiledemo.ui.setting;

import android.content.Intent;
import android.os.Bundle;


import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.report.ReportActivity;

public class MonitorSettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.monitor_setting, rootKey);
        Preference monitor_button = findPreference("setting_application");
        if (monitor_button != null) {
            monitor_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    Intent intent = new Intent(getActivity(), ReportActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }
    }
}

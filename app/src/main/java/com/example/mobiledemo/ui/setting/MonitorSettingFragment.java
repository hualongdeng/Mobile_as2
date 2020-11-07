package com.example.mobiledemo.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;


import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.mobiledemo.R;


import static android.content.Context.MODE_PRIVATE;

public class MonitorSettingFragment extends PreferenceFragmentCompat {

    Object mLock;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.monitor_setting, rootKey);
        Preference monitor_button = findPreference("setting_application");
        SwitchPreference volume_monitor = findPreference("switch_volume");
        if ((getActivity().getSharedPreferences("monitor", MODE_PRIVATE).getInt("volume_monitor", Context.MODE_PRIVATE)) == -1) {
            volume_monitor.setChecked(false);
        } else {
            volume_monitor.setChecked(true);
        }
        volume_monitor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean switched = ((SwitchPreference) preference).isChecked();
                if (switched == false) {
                    SharedPreferences sharedPref = getActivity().getSharedPreferences("monitor", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("volume_monitor", 0);
                    editor.commit();
                    Log.e("123",String.valueOf(getActivity().getSharedPreferences("monitor", MODE_PRIVATE).getInt("volume_monitor", Context.MODE_PRIVATE)));
                }
                else {
                    SharedPreferences sharedPref = getActivity().getSharedPreferences("monitor", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("volume_monitor", -1);
                    editor.commit();
                    Log.e("123",String.valueOf(getActivity().getSharedPreferences("monitor", MODE_PRIVATE).getInt("volume_monitor", Context.MODE_PRIVATE)));
                }
                return true;
            }
        });
        if (monitor_button != null) {
            monitor_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    Intent intent = new Intent(getActivity(), ApplicationActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }
    }
}

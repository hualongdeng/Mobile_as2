package com.example.mobiledemo.ui.setting;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;


import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

import com.example.mobiledemo.R;
import com.example.mobiledemo.ui.report.ReportActivity;

import static android.content.Context.AUDIO_SERVICE;

public class SettingFragment extends PreferenceFragmentCompat {
    AudioManager am;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting, rootKey);
        Preference monitor_button = findPreference("setting_monitor");
        SeekBarPreference volume_bar = findPreference("volume");
        if (monitor_button != null) {
            monitor_button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    Intent intent = new Intent(getActivity(), MonitorSettingActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }
        am = (AudioManager)getActivity().getSystemService(AUDIO_SERVICE);
//获取系统最大音量
        int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM );
        volume_bar.setMax(maxVolume);
//获取当前音量
        int currentVolume = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
        Log.i("init", String.valueOf(currentVolume));
        volume_bar.setValue(currentVolume);
    }
}
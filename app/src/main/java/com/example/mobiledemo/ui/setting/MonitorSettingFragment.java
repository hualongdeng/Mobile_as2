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

    AudioRecord mAudioRecorder;
    static final int SAMPLE_RATE_IN_HZ = 8000;
    static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(
            SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
            AudioFormat.ENCODING_PCM_16BIT);
    Object mLock;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.monitor_setting, rootKey);
        Preference monitor_button = findPreference("setting_application");
        SwitchPreference volume_monitor = findPreference("switch_volume");
        volume_monitor.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean switched = ((SwitchPreference) preference).isChecked();
                if (switched == false) {
                    SharedPreferences sharedPref = getActivity().getSharedPreferences("volume_monitor", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("volume_monitor", 1);
                    editor.commit();
                    mLock = new Object();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mAudioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                                    SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
                                    AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
                            mAudioRecorder.startRecording();
                            while ((getActivity().getSharedPreferences("volume_monitor", MODE_PRIVATE).getInt("volume_monitor", Context.MODE_PRIVATE)) == 1) {
                                short[] buffer = new short[BUFFER_SIZE];
                                double volume = getVolume(buffer);
                                Log.e("TAG", "分贝值:" + volume);
                                synchronized (mLock) {
                                    try {
                                        mLock.wait(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            mAudioRecorder.stop();
                            mAudioRecorder.release();
                            mAudioRecorder = null;
                        }
                    });
                    thread.start();
                }
                else {
                    SharedPreferences sharedPref = getActivity().getSharedPreferences("volume_monitor", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("volume_monitor", 0);
                    editor.commit();
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
    private double getVolume(short[] buffer) {
        int r = mAudioRecorder.read(buffer, 0, BUFFER_SIZE);
        long v = 0;
        for (int i = 0; i < buffer.length; i++) {
            Log.e("voice", "data：" + buffer[i]);
            v += buffer[i] * buffer[i];
        }
        double mean = v / (double) r;
        double volume = 10 * Math.log10(mean);
        return volume;
    }
}

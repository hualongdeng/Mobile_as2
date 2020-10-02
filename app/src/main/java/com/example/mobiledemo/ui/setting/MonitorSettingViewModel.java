package com.example.mobiledemo.ui.setting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MonitorSettingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MonitorSettingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is account fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

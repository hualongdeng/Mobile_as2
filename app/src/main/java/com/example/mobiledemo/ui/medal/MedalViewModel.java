package com.example.mobiledemo.ui.medal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MedalViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MedalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is medal fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
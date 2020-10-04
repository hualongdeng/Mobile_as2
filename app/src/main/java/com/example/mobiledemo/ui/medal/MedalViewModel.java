package com.example.mobiledemo.ui.medal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MedalViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MedalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("My Medal");

    }

    public LiveData<String> getText() {
        return mText;
    }
}
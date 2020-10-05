package com.example.mobiledemo.ui.type2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Type2ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Type2ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is account fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

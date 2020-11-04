package com.example.mobiledemo.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfilePhotoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfilePhotoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ProfilePhotoViewModel");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

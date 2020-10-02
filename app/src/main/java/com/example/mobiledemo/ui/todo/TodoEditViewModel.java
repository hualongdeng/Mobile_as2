package com.example.mobiledemo.ui.todo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TodoEditViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TodoEditViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is account fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

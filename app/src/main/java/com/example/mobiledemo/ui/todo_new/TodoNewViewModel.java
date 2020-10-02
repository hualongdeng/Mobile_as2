package com.example.mobiledemo.ui.todo_new;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TodoNewViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TodoNewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is account fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

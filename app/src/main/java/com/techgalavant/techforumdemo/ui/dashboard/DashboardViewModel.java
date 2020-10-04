package com.techgalavant.techforumdemo.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the dashboard fragment. It will be used to show the Office Trivia.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
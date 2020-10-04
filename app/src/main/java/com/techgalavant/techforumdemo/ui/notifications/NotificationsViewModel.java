package com.techgalavant.techforumdemo.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the notifications fragment. It will be used to show Today's Sponsor.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
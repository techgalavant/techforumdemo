package com.techgalavant.techforumdemo.ui.riddle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RiddleViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RiddleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the fragment to show Today's Riddle.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
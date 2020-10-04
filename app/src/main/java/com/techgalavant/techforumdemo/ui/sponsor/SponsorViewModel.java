package com.techgalavant.techforumdemo.ui.sponsor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SponsorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SponsorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the sponsor fragment. It will be used to show Today's Sponsor.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
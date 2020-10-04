package com.techgalavant.techforumdemo.ui.trivia;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TriviaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TriviaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the trivia fragment. It will be used to show the Office Trivia.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
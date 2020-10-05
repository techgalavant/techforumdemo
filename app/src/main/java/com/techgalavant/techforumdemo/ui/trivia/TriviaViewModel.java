package com.techgalavant.techforumdemo.ui.trivia;

import com.techgalavant.techforumdemo.R;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TriviaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TriviaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Office Trivia");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
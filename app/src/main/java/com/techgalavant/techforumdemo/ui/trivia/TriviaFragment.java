package com.techgalavant.techforumdemo.ui.trivia;

// This fragment will be used to show the Office Trivia from the Google Firebase Realtime Database

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.techgalavant.techforumdemo.R;

public class TriviaFragment extends Fragment {

    // used for logging events in Google Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
    }
    // TODO Setup logEvent() to log Office Trivia actions into Google Firebase Analytics.

    private static final String TAG = TriviaFragment.class.getSimpleName();

    private TriviaViewModel triviaViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        triviaViewModel =
                ViewModelProviders.of(this).get(TriviaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_trivia, container, false);
        final TextView textView = root.findViewById(R.id.title_trivia);  // TODO Add in the other TextViews
        triviaViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
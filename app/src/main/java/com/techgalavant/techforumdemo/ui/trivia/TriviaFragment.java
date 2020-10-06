package com.techgalavant.techforumdemo.ui.trivia;

// This fragment demonstrates Crashlytics. When the user presses a button, it will force a crash and log events in Firebase.

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
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

        //Demo Crashlytics - when the user presses the button, crash the app and log an event in Firebase

        final Button button = root.findViewById(R.id.next_q);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Intent nextIntent = new Intent();
                //startActivity(nextIntent);
                // See: https://firebase.google.com/docs/crashlytics/customize-crash-reports?platform=android for different ways to add keys
                FirebaseCrashlytics.getInstance().setCustomKey("str_key", "hello");
                FirebaseCrashlytics.getInstance().log("User hit NEXT button - to test a forced crash");
                throw new RuntimeException("Trivia Test Crash"); // force a crash for crashlytics when user presses Next button
                //Log.e(TAG,"User selected button");

            }

        });
                return root;
    }
}
package com.techgalavant.techforumdemo.ui.riddle;

// This fragment will be used to show a riddle using In-App Messaging.
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


public class RiddleFragment extends Fragment {

    // used for logging events in Google Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
    }

    // TODO Setup logEvent() to log Riddle actions into Google Firebase Analytics.

    private static final String TAG = RiddleFragment.class.getSimpleName();

    private RiddleViewModel riddleViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        riddleViewModel =
                ViewModelProviders.of(this).get(RiddleViewModel.class);
        View root = inflater.inflate(R.layout.fragment_riddle, container, false);
        final TextView textView = root.findViewById(R.id.text_riddle);
        riddleViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;

    }

}
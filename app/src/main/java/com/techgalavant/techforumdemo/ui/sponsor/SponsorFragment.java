package com.techgalavant.techforumdemo.ui.sponsor;

// This fragment will be used to show the Sponsor using Google Firebase Remote Configuration.

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

public class SponsorFragment extends Fragment {

    // used for logging events in Google Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
    }

    // TODO Setup logEvent() to log Sponsor actions into Google Firebase Analytics.

    private static final String TAG = SponsorFragment.class.getSimpleName();

    private SponsorViewModel sponsorViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sponsorViewModel =
                ViewModelProviders.of(this).get(SponsorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sponsor, container, false);
        final TextView textView = root.findViewById(R.id.text_sponsor);
        sponsorViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
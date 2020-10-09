package com.techgalavant.techforumdemo.ui.trivia;

/**
 * Created by Mike Fallon in October 2020 for the Tech Forum demo of Google Firebase.
 *
 * This fragment demonstrates Crashlytics. When the user presses a button, it will force a crash and log events in Firebase.
 * It also demonstrates logging values as special paramters in Firebase Analytics.
 *
 * TODO read Office Trivia questions from Firebase Database. See example here - https://github.com/prakashpun/IsItTrue/blob/master/app/src/main/java/com/thinkinghats/isittrue/activity/GameActivity.java
 *
 */


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.techgalavant.techforumdemo.R;
import com.techgalavant.techforumdemo.ui.riddle.RiddleFragment;

public class TriviaFragment extends Fragment {

    // used for logging events in Google Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    int count;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
    }


    private static final String TAG = TriviaFragment.class.getSimpleName();

    private TriviaViewModel triviaViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        triviaViewModel =
                ViewModelProviders.of(this).get(TriviaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_trivia, container, false);
        final TextView textView = root.findViewById(R.id.title_trivia);
        triviaViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }

        });

        //Demo Crashlytics - when the user presses the NEXT button, crash the app and log an event in Firebase

        final Button nextbtn = root.findViewById(R.id.next_q);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // See: https://firebase.google.com/docs/crashlytics/customize-crash-reports?platform=android for different ways to add keys
                FirebaseCrashlytics.getInstance().setCustomKey("str_key", "crash button");
                FirebaseCrashlytics.getInstance().log("User hit NEXT button - to test a forced crash");
                Log.e(TAG,"User selected Next button");
                throw new RuntimeException("Trivia Test Crash");

            }

        });

        // Demo Firebase Analytics - count each time the person presses the LAST button

        final Button lastbutton = root.findViewById(R.id.last_q);

        lastbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = getResources().getString(R.string.fragtitle_trivia);
                String id = getResources().getString(R.string.title_trivia);
                Intent lastIntent = new Intent(TriviaFragment.this.getActivity(), RiddleFragment.class);

                Log.e(TAG, "User selected LAST button");


                if(count < 1)
                {
                    count = 1;
                    //first time clicked to do this
                    Toast.makeText(getActivity(),"LAST button clicked for 1st time", Toast.LENGTH_LONG).show();
                }
                else
                {
                    count = count+1;
                    //check how many times clicked and so on
                    Toast.makeText(getActivity(),"LAST button click count = "+count, Toast.LENGTH_LONG).show();
                }
                Log.e(TAG, String.valueOf(count));

                // Log some information in the Firebase Analytics on this fragment
                Bundle params = new Bundle();
                params.putString("title_frag", id);
                params.putString("frag_title", name);
                params.putString("last_btn_count", String.valueOf(count));
                params.putInt("btn_last_count2", count);
                mFirebaseAnalytics.logEvent("frag_info", params);
                // END of logging


            }

        });


                return root;
    }
}
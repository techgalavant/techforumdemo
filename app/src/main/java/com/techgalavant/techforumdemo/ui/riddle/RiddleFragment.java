package com.techgalavant.techforumdemo.ui.riddle;

/**
 * Created by Mike Fallon in October 2020 for the Tech Forum demo of Google Firebase.
 *
 * This fragment will be used to demonstrate reading from the Google Firebase Realtime database
 * using a riddle. Users will be able to shake their device to suggest a new riddle. If the user
 * identifies as the 'hermosa' string value, then it will display that updated riddle on the screen.
 *
 * This fragment uses the Words.java constructor and the MyFirebaseUtil.java for accessing the Firebase DB.
 *
 */


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.techgalavant.techforumdemo.MyFirebaseUtil;
import com.techgalavant.techforumdemo.R;

import org.w3c.dom.Text;


public class RiddleFragment extends Fragment {

    // used for logging events in Google Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
    }

    private static final String TAG = RiddleFragment.class.getSimpleName();

    private TextView txtTitle, txtRiddle, tvDate, txtAns1, txtAns2;
    private EditText txtResponse;

    private String hermosa; // used to identify who can display their riddle on the fragment

    public RiddleFragment() {
        // Required empty public constructor
    }

    private RiddleViewModel riddleViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        riddleViewModel =
                ViewModelProviders.of(this).get(RiddleViewModel.class);
        View root = inflater.inflate(R.layout.fragment_riddle, container, false);

        // Show a friendly message about the screen's purpose
        txtTitle = (TextView) root.findViewById(R.id.title_riddle);

        // dupe??
        final TextView textView = root.findViewById(R.id.title_riddle);
        riddleViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // Used to retrieve the current riddle in the Firebase DB if they provide the 'hermosa' string as their name
        hermosa = getResources().getString(R.string.hermosa);
        DatabaseReference myRef = MyFirebaseUtil.getDatabase().getReference(hermosa); // see MyFirebaseUtil.class
        final DatabaseReference myRiddle = myRef.child("Riddle");
        DatabaseReference myAnswer1 = myRef.child("Answer1");
        DatabaseReference myAnswer2 = myRef.child("Answer2");
        DatabaseReference corrAns = myRef.child("Correct");

        // Setup the Riddle and potential answers
        // TODO setup the answers as buttons which the user can select.
        txtRiddle = (TextView) root.findViewById(R.id.riddle);
        txtAns1 = (TextView) root.findViewById(R.id.ans1);
        txtAns2 = (TextView) root.findViewById(R.id.ans2);
        txtResponse = (EditText) root.findViewById(R.id.riddle_response);

        // Read from the Firebase database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = getResources().getString(R.string.fragtitle_riddle);
                String id = getResources().getString(R.string.title_riddle);

                // This method will read from the Firebase DB and present it as a riddle
                // whenever data changes.
                String updateriddle = dataSnapshot.child("Riddle").getValue(String.class);
                String updateanswer1 = "Answer 1 = "+dataSnapshot.child("Answer1").getValue(String.class);
                String updateanswer2 = "Answer 2 = "+dataSnapshot.child("Answer2").getValue(String.class);
                String updatecorrectans = dataSnapshot.child("Correct").getValue(String.class);
                String updatesender = dataSnapshot.child("Sender").getValue(String.class);

                txtRiddle.setText(updateriddle); // provide the current riddle
                txtAns1.setText(updateanswer1); // provide answer 1
                txtAns2.setText(updateanswer2); // provide answer 2

                Log.e(TAG, "Sender was " + updatesender);

                // Log some information in the Firebase Analytics on this fragment
                Bundle params = new Bundle();
                params.putString("title_frag", id);
                params.putString("frag_title", name);
                params.putString("riddle_question", updateriddle);
                params.putString("riddle_answer1", updateanswer1);
                params.putString("riddle_answer2", updateanswer2);
                params.putString("correct_answer", updatecorrectans);
                mFirebaseAnalytics.logEvent("frag_info", params);
                // END of logging

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });


        return root;

    }

}
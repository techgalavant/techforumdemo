package com.techgalavant.techforumdemo.ui.riddle;

// This fragment will be used to show a riddle using In-App Messaging.
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

    // TODO Setup logEvent() to log Riddle actions into Google Firebase Analytics.

    private static final String TAG = RiddleFragment.class.getSimpleName();

    private TextView txtTitle, txtRiddle, tvDate, txtAns1, txtAns2;
    private EditText txtResponse;

    private String hermosa; // used to display messages on WelcomeFragment

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

        // Show a friendly date and time to the user
        // tvDate = (TextView) root.findViewById(R.id.appdate); // Shows the time for the most recent message
        //final TextView txtRiddle = (TextView) root.findViewById(R.id.riddle); // Show the current riddle
        //final  TextView txtResponse = (EditText) root.findViewById(R.id.riddle_response);
        final TextView textView = root.findViewById(R.id.title_riddle);
        riddleViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // Used to retrieve welcome messages in the Firebase DB
        hermosa = getResources().getString(R.string.hermosa);
        DatabaseReference myRef = MyFirebaseUtil.getDatabase().getReference(hermosa); // see MyFirebaseUtil.class
        DatabaseReference myRiddle = myRef.child("Riddle");
        DatabaseReference myAnswer1 = myRef.child("Answer1");
        DatabaseReference myAnswer2 = myRef.child("Answer2");
        DatabaseReference mesgTime = myRef.child("Contact");

        // Show a friendly message about the screen's purpose
        //txtTitle = (TextView) root.findViewById(R.id.title_riddle);

        // Show a friendly date and time to the user
        //tvDate = (TextView) root.findViewById(R.id.appdate); // Shows the time for the most recent message

        txtRiddle = (TextView) root.findViewById(R.id.riddle); // Show the current riddle
        txtAns1 = (TextView) root.findViewById(R.id.ans1);
        txtAns2 = (TextView) root.findViewById(R.id.ans2);
        txtResponse = (EditText) root.findViewById(R.id.riddle_response);

        // Read from the Firebase database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // This method will read from the Firebase DB and present it as a riddle
                // whenever data changes.
                String updateriddle = dataSnapshot.child("Riddle").getValue(String.class);
                String updateanswer1 = "Answer 1 = "+dataSnapshot.child("Answer1").getValue(String.class);
                String updateanswer2 = "Answer 2 = "+dataSnapshot.child("Answer2").getValue(String.class);
                String updatetime = dataSnapshot.child("Contact").getValue(String.class);  // "Contact" is actually the time that the update was provided - see SendFeedback if userName equals...
                String updatemesg = dataSnapshot.child("Feedback").getValue(String.class); // "Feedback" is converted to message if userName.equals ...

                //tvDate.setText(updatetime); // provide the update time
                txtRiddle.setText(updateriddle); // provide the current riddle
                txtAns1.setText(updateanswer1); // provide answer 1
                txtAns2.setText(updateanswer2); // provide answer 2

                Log.e(TAG, "Update time = " + updatetime + " and Update Mesg = " + updatemesg);

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
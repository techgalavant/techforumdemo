package com.techgalavant.techforumdemo;

/** Created by Mike Fallon and updated in October 2020 for use in the Tech Forum demo.
 *
 * The purpose of SendFeedback is to allow a user to submit a new riddle which will
 * get stored in the Firebase DB. If the user identifies themselves with the 'hermosa' value,
 * then that will be the key used for displaying the riddle on RiddleFragment.
 *
 * This also demonstrates Firebase Analytics, storing the sender's name and time submitted.
 *
 * Credits to:
 * - Firebase DB tutorial - https://www.simplifiedcoding.net/firebase-realtime-database-example-android-application/
 *
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

import static com.techgalavant.techforumdemo.R.id.word1;
import static com.techgalavant.techforumdemo.R.id.word2;
import static com.techgalavant.techforumdemo.R.id.word3;
import static com.techgalavant.techforumdemo.R.id.word4;


public class SendFeedback extends AppCompatActivity {

    // used for logging events in Google Analytics - send the submitter's name and time submitted for deeper analysis
    private FirebaseAnalytics mFirebaseAnalytics;
    int count;

    private static final String TAG = SendFeedback.class.getSimpleName();

    private Button mashIt_btn, cancelIt_btn, clearIt_btn;
    private EditText inWord1, inWord2, inWord3, inWord4;
    private TextView txtDetails;
    private DatabaseReference myRiddle;
    private String togglebtn;
    public String Correct; //used to identify the correct answer by the user
    public Integer choice=0; // just used for TAGging
    String name = "Feedback";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_hints);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        txtDetails = (TextView) findViewById(R.id.textView2);

        inWord1 = (EditText) findViewById(word1); // Sender's name
        inWord3 = (EditText) findViewById(word3); // Riddle
        inWord2 = (EditText) findViewById(word2); // Answer 1
        inWord4 = (EditText) findViewById(word4); // Answer 2

        Spinner spinner = (Spinner) findViewById(R.id.answer_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.answer_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                Correct = parent.getItemAtPosition(pos).toString();
                // uses string array "answer_array"

                if(pos==1){
                    Log.e(TAG, "User selected " + Correct);
                }

                if(pos==2){
                    Log.e(TAG, "User selected " + Correct);

                }

                if (pos==0) {
                    Correct = "";
                    Log.e(TAG, "User selected " + Correct);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                choice = 0;

            }
        });

        mashIt_btn = (Button) findViewById(R.id.mashIt);
        clearIt_btn = (Button) findViewById(R.id.clearIt);
        cancelIt_btn = (Button) findViewById(R.id.cxlIt);

        // Store the app feedback in Firebase
        mashIt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Sender = inWord1.getText().toString();
                String Riddle = inWord3.getText().toString();
                String Answer1 = inWord2.getText().toString();
                String Answer2 = inWord4.getText().toString();
                myRiddle = MyFirebaseUtil.getDatabase().getReference(Sender);

                Log.e(TAG, "Firebase reference is set to " + myRiddle);

                // Create a new list of items to be stored.
                // FUTURE - allow the user to update their feedback.
                if (TextUtils.isEmpty(togglebtn)) {
                    createList(Sender, Correct, Riddle, Answer1, Answer2);
                    Log.e(TAG, "No words found so created a new list.");
                }
                // TODO check to make sure the user has selected a correct answer
                // if (!TextUtils.isEmpty(Correct)) {
                //    Toast.makeText(getApplicationContext(), "Please pick a correct answer!", Toast.LENGTH_LONG).show();
                // }
                else {
                    updateList(Sender, Correct, Riddle, Answer1, Answer2);
                    Log.e(TAG, "Updated the list of words.");
                }

                // Launch an AlertDialog box to post a confirmation message
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SendFeedback.this);

                // Set Dialog box title
                alertDialog.setTitle("Thank You!");

                // Set Dialog message
                alertDialog.setMessage("Need to verify this answer before it gets used.");

                // Sets icon in the AlertDialog window
                alertDialog.setIcon(R.drawable.ic_mesg);

                // Sets operation for when "Close" button is selected
                alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the AlertDialog box and return to MainActivity
                        Log.e(TAG, "User selected CLOSE button on AlertDialog in SendFeedback.java");
                        // dialog.cancel();
                        Intent intent = new Intent(SendFeedback.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

                // Show AlertDialog box
                alertDialog.show();

            }

        });

        // Clear the feedback form
        clearIt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inWord1.setText("");
                inWord2.setText("");
                inWord3.setText("");
                inWord4.setText("");
                togglebtn = "";
                Log.e(TAG, "clearIt_btn was used to reset entries");
            }
        });

        // Cancel this and go back to MainActivity
        cancelIt_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Log.e(TAG, "User elected to cancel.");
                if(count < 1)
                {
                    count = 1;

                }
                else
                {
                    count = count+1;

                }
                Log.e(TAG, String.valueOf(count));

                // If the user cancelled submitting a new riddle, log this event in Firebase Analytics
                Bundle params = new Bundle();
                params.putString("dialog_screen", name);
                params.putString("cancel_send_counter", String.valueOf(count));
                mFirebaseAnalytics.logEvent("screen_info", params);
                // END of logging

                Intent intent = new Intent(SendFeedback.this, MainActivity.class);
                startActivity(intent);
            }
        });

        toggleButton();

    }

    // Changes button text of mashIt_btn
    // For use in FUTURE state

    private void toggleButton() {
        if (TextUtils.isEmpty(togglebtn)) {
            mashIt_btn.getResources().getString(R.string.send);
        } else {
            mashIt_btn.getResources().getString(R.string.update);
        }
    }


    // Creates a new list of words consisting of the riddle and potential answers back into the Firebase Database
    private void createList(String Sender, String Correct, String Riddle, String Answer1, String Answer2) {

        Words words = new Words(Sender, Correct, Riddle, Answer1, Answer2);
        togglebtn = "On";

        myRiddle.setValue(words);

        // Logging some Firebase Analytics
        Bundle params = new Bundle();
        params.putString("riddle_submitter", Sender);
        String currentTimeString = DateFormat.getDateTimeInstance().format(new Date());
        params.putString("riddle_submit_time", currentTimeString);
        mFirebaseAnalytics.logEvent("submitter_info", params);
        // END of logging

        addWordChangeListener();
    }

    // Updates the list of words in the Firebase Database
    private void updateList(String Sender, String Correct, String Riddle, String Answer1, String Answer2) {

        if (!TextUtils.isEmpty(Sender))
            myRiddle.child("Sender").setValue(Sender);
        if (!TextUtils.isEmpty(Correct))
            myRiddle.child("Correct").setValue(Correct);
        if (!TextUtils.isEmpty(Riddle))
            myRiddle.child("Riddle").setValue(Riddle);
        if (!TextUtils.isEmpty(Answer1))
            myRiddle.child("Answer1").setValue(Answer1);
        if (!TextUtils.isEmpty(Answer2))
            myRiddle.child("Answer2").setValue(Answer2);

    }

    private void addWordChangeListener() {
        // User data change listener
        myRiddle.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Words words = dataSnapshot.getValue(Words.class);

                // Check for null
                if (words == null) {
                    Log.e(TAG, "No riddle was found.");
                    togglebtn = "";
                    return;
                } else {

                    Log.e(TAG, "Riddle got added.");

                    // Change the subtitle if the user has already provided feedback
                    txtDetails.getResources().getString(R.string.feedback_chg);

                    toggleButton();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Database error. See onCancelled", error.toException());
            }
        });
    }

}
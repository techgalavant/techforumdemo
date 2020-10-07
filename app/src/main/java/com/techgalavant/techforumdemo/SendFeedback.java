package com.techgalavant.techforumdemo;

/** Created by Mike Fallon.
 *
 * The purpose of SendFeedback is to generate a riddle manually, store it in Firebase and
 * retrieve the response if the hermosa value matches the sender.
 * The riddle will then be displayed on the RiddleFragment.
 *
 * Credits to:
 * - Firebase DB tutorial - https://www.simplifiedcoding.net/firebase-realtime-database-example-android-application/
 * - Crazy Madlibs App on GitHub (my other app!) - https://github.com/techgalavant/madlibs
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    private static final String TAG = SendFeedback.class.getSimpleName();

    private Button mashIt_btn, cancelIt_btn, clearIt_btn;
    private EditText inWord1, inWord2, inWord3, inWord4;
    private TextView txtDetails;
    private DatabaseReference myStory;
    private FirebaseDatabase myFBInstance;
    private String storyId;
    private String togglebtn;

    private String hermosa; // used to display current riddle on RiddleFragment
    private String Contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_hints);

        txtDetails = (TextView) findViewById(R.id.textView2);

        // EditTexts in feedback_hints.xml use TextInputLayout to animate the hint texts.
        // The TextInputLayout wraps around each EditText in the layout file.
        inWord1 = (EditText) findViewById(word1); // Sender's name
        inWord3 = (EditText) findViewById(word3); // Riddle
        inWord2 = (EditText) findViewById(word2); // Answer 1
        inWord4 = (EditText) findViewById(word4); // Answer 2
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
                hermosa = getResources().getString(R.string.hermosa);

                // If Digital Hermosa posts a message, it should be displayed in the RiddleFragment.
                // The purpose of this was to allow an admin to update the riddle manually.
                if (Sender.equals(hermosa)){
                    String currentTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    Contact = "UPDATE: " + currentTimeString; // Instead of contact info, set contact field to time entered when userName equals Sender
                    // myStory = MyFirebaseUtil.getDatabase().getReference(Sender).child(Contact); // create separate message based on time entered?
                    myStory = MyFirebaseUtil.getDatabase().getReference(Sender);
                } else {
                    Contact = inWord2.getText().toString();
                    myStory = MyFirebaseUtil.getDatabase().getReference(Sender);
                }

                Log.e(TAG, "Firebase reference is set to " + myStory);

                // Create a new list of items to be stored.
                // FUTURE - allow the user to update their feedback.
                if (TextUtils.isEmpty(togglebtn)) {
                    createList(Sender, Contact, Riddle, Answer1, Answer2);
                    Log.e(TAG, "There were no words in " + storyId + ", so created a new list.");
                } else {
                    updateList(Sender, Contact, Riddle, Answer1, Answer2);
                    Log.e(TAG, "Updated the word list for " + storyId + ".");
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
    private void createList(String Sender, String Contact, String Riddle, String Answer1, String Answer2) {

        Words words = new Words(Sender, Contact, Riddle, Answer1, Answer2);
        togglebtn = "On";

        myStory.setValue(words);

        addWordChangeListener();
    }

    // Updates the list of words in the Firebase Database
    private void updateList(String Sender, String Contact, String Riddle, String Answer1, String Answer2) {

        if (!TextUtils.isEmpty(Sender))
            myStory.child("Sender").setValue(Sender);
        if (!TextUtils.isEmpty(Contact))
            myStory.child("Contact").setValue(Contact);
        if (!TextUtils.isEmpty(Riddle))
            myStory.child("Riddle").setValue(Riddle);
        if (!TextUtils.isEmpty(Answer1))
            myStory.child("Answer1").setValue(Answer1);
        if (!TextUtils.isEmpty(Answer2))
            myStory.child("Answer2").setValue(Answer2);

    }

    private void addWordChangeListener() {
        // User data change listener
        myStory.addValueEventListener(new ValueEventListener() {
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
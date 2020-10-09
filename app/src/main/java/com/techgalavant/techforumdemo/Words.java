package com.techgalavant.techforumdemo;

/**
 * Created by Mike Fallon on 10/6/2020.
 *
 * This is a constructor used for SendFeedback.
 * The Words are stored in the Firebase DB.
 */

public class Words {
    public String Sender;
    public String Correct;
    public String Riddle;
    public String Answer1;
    public String Answer2;


    // This is the default constructor for DataSnapshot.getValue(Words.class)
    public Words(){

    }

    public Words(String Sender, String Correct, String Riddle, String Answer1, String Answer2) {
        this.Sender = Sender;
        this.Correct = Correct;
        this.Riddle = Riddle;
        this.Answer1 = Answer1;
        this.Answer2 = Answer2;

    }

}

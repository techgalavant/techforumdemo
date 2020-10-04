package com.techgalavant.techforumdemo;

/**
 * Created by Mike Fallon
 *
 * This is a utility for configuration of the Firebase features - Database and Remote Config.
 *
 */

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class MyFirebaseUtil {

    private static FirebaseDatabase mDatabase;

    private FirebaseRemoteConfig mRemoteConfig;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance(); // get instance of Firebase
            mDatabase.setPersistenceEnabled(true); // set to true so that it will store data if user is not connected online
        }

        return mDatabase;
    }

}
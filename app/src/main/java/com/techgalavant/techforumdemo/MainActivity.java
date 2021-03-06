package com.techgalavant.techforumdemo;


 /**
 * Created by Mike Fallon in October 2020 for the Homesite Tech Forum demo of Google Firebase.
 *
 * The app demonstrates various Google Firebase features including Remote Config, Firebase Realtime Database,
 * Firebase Analytics, Crashlytics, In-App messaging, Push Notification (Cloud Messaging) and Storage (accessing
 * the image file and displaying it with Picasso).
 *
 * Other cool features - users will be able to shake their device to suggest a new riddle. If the user
 * identifies as the 'hermosa' string value, then it will display that updated riddle on the screen.
  *
  * MyFirebaseMessagingService.java is for the push notifications, to obtain the device token. It's probably optional.
 *
  * Credits and inspiration to:
  *  Device Shake Listener credit to http://jasonmcreynolds.com/?p=388
  *
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;  // used for logging events in Google Analytics
    String name = "Main Activity";

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_riddle, R.id.navigation_trivia, R.id.navigation_sponsor)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MainActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);

            }
        });

        // The user can also shake the device to send a message
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {

                // Launch an AlertDialog box so that users can post a message

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                Log.e(TAG, "ShakeListener detected movement so it displayed the alert dialog box.");

                // Set Dialog box title
                alertDialog.setTitle("New Riddle");

                // Set Dialog message
                alertDialog.setMessage("Submit a new riddle?");

                // Sets icon in the AlertDialog window
                alertDialog.setIcon(R.drawable.ic_mesg);

                // Set operation for when user selects "YES" on AlertDialog
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Launch riddle form - SendFeedback.class
                        final Intent intent = new Intent(MainActivity.this, SendFeedback.class);
                        startActivity(intent);
                        Log.e(TAG, "User selected YES on SendFeedback AlertDialog");
                    }
                });

                // Sets operation for when "NO" button is selected
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the AlertDialog box
                        Log.e(TAG, "User selected NO on SendFeedback AlertDialog");
                        String cancel = "Yes";

                        // If the user accidentally shook their device and hit cancel, log this event in Firebase Analytics.
                        Bundle params = new Bundle();
                        params.putString("dialog_screen", name);
                        params.putString("shake_cancel_action", cancel);
                        mFirebaseAnalytics.logEvent("activity_info", params);
                        // End logging in Firebase

                        dialog.cancel();
                    }
                });

                // Show AlertDialog box
                alertDialog.show();

            }
        });
    }

    // Used for ShakerListener
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    // Used for ShakerListener
    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

}
package com.techgalavant.techforumdemo.ui.sponsor;
/**
 * Created by Mike Fallon in October 2020 for the Tech Forum demo of Google Firebase.
 *
 * This fragment will be used to show the Sponsor using Google Firebase Remote Configuration.
 *
 */


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.squareup.picasso.Picasso;
import com.techgalavant.techforumdemo.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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

    private TextView txtTitle, txtSponsor;
    private ImageView imgSponsor;

    // The following are related to the Firebase Remote Config
    private FirebaseRemoteConfig mRemoteConfig;

    private SponsorViewModel sponsorViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // See remote config example here: https://github.com/firebase/quickstart-android/blob/adb735b2abbd00e61ed387751e2d91cd7f22a3c5/config/app/src/main/java/com/google/samples/quickstart/config/java/MainActivity.java#L76-L79

        mRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(10)
                .build();
        mRemoteConfig.setConfigSettingsAsync(configSettings);
        // use defaults if you can't reach service
        mRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        sponsorViewModel =
                ViewModelProviders.of(this).get(SponsorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sponsor, container, false);
        final TextView textView = root.findViewById(R.id.title_sponsor);
        sponsorViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        txtSponsor = (TextView) root.findViewById(R.id.sponsor_caption); // Shows the thank you message under the sponsor logo
        imgSponsor = (ImageView) root.findViewById(R.id.sponsor_photo); // Show the current sponsor

        fetchSponsor();

        return root;
    }

    /**
     * Fetch a URL from the Remote Config service, and then activate it.
     */
    private void fetchSponsor() {

        // [START fetching remote config and populate the imageView]
        mRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.d(TAG, "Config params updated: " + updated);

                            String imgUrl = mRemoteConfig.getString("new_sponsor");
                            Log.i(TAG,"Remote Config imgUrl is "+imgUrl);
                            Toast.makeText(getActivity(), "Successfully fetched remote config "+imgUrl,
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e(TAG,"fetchRemoteConfigs UNSUCCESSFUL");
                            Toast.makeText(getActivity(), "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // get the new_sponsor from Remote Config service and insert it into the ImageView
                        String imgUrl = mRemoteConfig.getString("new_sponsor");

                        // this is a check to ensure that the new_sponsor value was not empty
                        if (imgUrl.isEmpty()){
                            Toast.makeText(getActivity(), "new_sponsor value was empty",
                                    Toast.LENGTH_SHORT).show();
                            imgSponsor.setImageResource(R.drawable.tg_logo2);
                        } else {
                            Toast.makeText(getActivity(), "new_sponsor value was found",
                                    Toast.LENGTH_SHORT).show();
                            Picasso.get().load(imgUrl).into(imgSponsor);  // Resource info here https://square.github.io/picasso/
                            // TODO set resize image to fit correctly into the imageView on SponsorFragment
                        }

                    }
                });  // [END fetchAndActivate]

    }


}
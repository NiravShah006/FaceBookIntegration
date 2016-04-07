package com.test.indianic.facebookintegration.view;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.test.indianic.facebookintegration.R;
import com.test.indianic.facebookintegration.fragment.LoginFragment;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "FTrbkAqNsPLw7BeCxf24A3vJ5";
    private static final String TWITTER_SECRET = "fMy4wlHRe3PxA7RhFtkZL1eEfvh2zupsqjXcpExBexCoOWYgZq";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());


        getSupportFragmentManager().beginTransaction().add(R.id.container, new LoginFragment(), LoginFragment.class.getSimpleName()).commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the fragment, which will then pass the result to the login
        // button.
        android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName());
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);

        }
    }


}



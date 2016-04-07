package com.test.indianic.facebookintegration.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.test.indianic.facebookintegration.R;
import com.test.indianic.facebookintegration.view.MainActivity;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * Created by indianic on 13/02/16.
 */
public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;
    private TextView tvUserName;
    private ProfileTracker profileTracker;
    private SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private Button signOut;
    private TwitterLoginButton twitterLoginButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        callbackManager = CallbackManager.Factory.create();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.

                // Log.e("Accesstoken","oncreate"+currentAccessToken.getUserId().toString());

                if (currentAccessToken == null) {
                    tvUserName.setText("");
                }

            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();


        if (accessToken != null) {
            getFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment(), HomeFragment.class.getSimpleName()).commit();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, null);

        //fb button
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        tvUserName = (TextView) view.findViewById(R.id.tv_username);

        //g+ button
        signInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signOut = (Button) view.findViewById(R.id.sign_out_button);

        loginButton.setReadPermissions("user_friends");
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                if (loginResult.getAccessToken() != null) {

                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                            if(currentProfile != null)
                                tvUserName.setText(currentProfile.getName());
                        }
                    };

                    //redirect to home page

                    getFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment(), HomeFragment.class.getSimpleName()).commit();

                } else {

//                      Profile  profile=Profile.getCurrentProfile();
//                      tvUserName.setText(profile.getName());
                    }


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        //Google  ++++++ code

        //sign in

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Toast.makeText(getActivity(),"G+",Toast.LENGTH_SHORT).show();
                signIn();

            }
        });


        //sign out


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOut();

            }
        });


        //twitter code..........

        twitterLoginButton = (TwitterLoginButton) view.findViewById(R.id.twitter_login_button);

        if (isAppInstalled(getActivity(), "com.twitter.android")) {

            twiterLogin();

        } else {

            twiterLogin();

        }

        return view;
    }


    //twitter login function


    public void twiterLogin() {

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("https://twitter.com/login/error?redirect_after_login=%2F"));
                startActivity(intent);

            }
        });


    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    //
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);


        //twitter
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);


        //g+++

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleSignInResult(GoogleSignInResult result) {
        // Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            tvUserName.setText(acct.getEmail());
            mGoogleApiClient.connect();
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }


    private void updateUI(boolean signedIn) {
        if (signedIn) {

            tvUserName.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.GONE);
            signOut.setVisibility(View.VISIBLE);


        } else {

            signOut.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
            tvUserName.setVisibility(View.GONE);

        }
    }


    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}

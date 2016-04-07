package com.test.indianic.facebookintegration.fragment;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.test.indianic.facebookintegration.R;


public class HomeFragment extends android.support.v4.app.Fragment {

    private Button      logOutbtn;
    private ShareButton sharebtn;
    private EditText   editText;
    private ProfileTracker profileTracker;
    private String profilename;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


         profileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                Log.e("profile name",""+oldProfile.getName());
                profilename=oldProfile.getName();

            }
        };

        profileTracker.startTracking();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        logOutbtn = (Button) view.findViewById(R.id.fragment_home_logout_btn);
        sharebtn = (ShareButton) view.findViewById(R.id.fragment_home_sharebtn);
        editText= (EditText) view.findViewById(R.id.fragment_home_edt);

               ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("Hello Facebook")
                .setContentDescription(
                        "The 'Hello Facebook' sample  showcases simple Facebook integration")
                .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                .build();

        sharebtn.setShareContent(linkContent);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText.setText(profilename);
        logOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logOut();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment(), LoginFragment.class.getSimpleName()).commit();

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        profileTracker.stopTracking();
    }
}

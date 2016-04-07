package com.test.indianic.facebookintegration.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.test.indianic.facebookintegration.R;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends android.support.v4.app.Fragment {

    private Button      logOutbtn;
    private ShareButton sharebtn;
    private EditText   editText;
    private ProfileTracker profileTracker;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e("HomeFragment", object.toString());

                        // Application code
                        try {
                            String email = object.getString("email");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();



    }


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

    }
}

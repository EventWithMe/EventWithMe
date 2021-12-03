package com.example.eventwithus.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventwithus.EditProfileActivity;
import com.example.eventwithus.LoginActivity;
import com.example.eventwithus.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

// TODO: 12/2/2021 add image functionality

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment"; // tag for logging
    public static final String EMAIL_KEY= "email";
    public static final String FIRSTNAME_KEY= "firstname";
    public static final String LASTNAME_KEY= "lastname";
    public static final String IMAGE_KEY= "image";

    ImageView ivPfp;
    TextView tvFirstNameP;
    TextView tvLastNameP;
    TextView tvEmailP;
    Button btnEditProfile;
    Button btnLogout;


    private ParseUser currentUser;
    Context context;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUser = ParseUser.getCurrentUser(); // initializes the Parseuser
        context = getContext();
        ivPfp = view.findViewById(R.id.ivPfp);
        tvFirstNameP = view.findViewById(R.id.tvFirstNameP);
        tvLastNameP = view.findViewById(R.id.tvLastNameP);
        tvEmailP = view.findViewById(R.id.tvEmailP);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);

        currentUser.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                populateProfileData();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Edit Profile Button Clicked");
                Intent intent = new Intent(getActivity().getBaseContext(), EditProfileActivity.class);
                getActivity().startActivity(intent);
                currentUser.fetchInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        populateProfileData();
                    }
                });
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Logout Button Clicked");
                ParseUser.logOut();
                Intent intent = new Intent(getActivity().getBaseContext(), LoginActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void populateProfileData() {
        String firstName = getString(R.string.profile_first) + "   " + currentUser.getString(FIRSTNAME_KEY);
        String lastName = getString(R.string.profile_last) + "   " + currentUser.getString(LASTNAME_KEY);
        String email = getString(R.string.profile_email) + "   " + currentUser.getString(EMAIL_KEY);

        tvFirstNameP.setText(firstName);
        tvLastNameP.setText(lastName);
        tvEmailP.setText(email);
    }
}
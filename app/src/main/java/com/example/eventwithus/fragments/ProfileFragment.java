package com.example.eventwithus.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventwithus.EditProfileActivity;
import com.example.eventwithus.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment"; // tag for logging
    public static final String EMAIL_KEY= "email";
    public static final String PASSWORD_KEY= "password";
    public static final String FIRSTNAME_KEY= "firstname";
    public static final String LASTNAME_KEY= "lastname";
    public static final String IMAGE_KEY= "image";

    ImageView ivPfp;
    TextView tvFirstNameP;
    TextView tvLastNameP;
    TextView tvEmailP;
    TextView tvPasswordP;
    Button btnEditProfile;

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
        tvPasswordP = view.findViewById(R.id.tvPasswordP);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);

        currentUser.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                populateProfileData();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), EditProfileActivity.class);
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
        tvPasswordP.setText(R.string.profile_password);
    }
}
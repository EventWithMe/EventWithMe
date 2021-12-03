package com.example.eventwithus.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.eventwithus.EditProfileActivity;
import com.example.eventwithus.LoginActivity;
import com.example.eventwithus.PasswordChangeActivity;
import com.example.eventwithus.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment"; // tag for logging
    public static final String EMAIL_KEY = "email";
    public static final String FIRSTNAME_KEY = "firstname";
    public static final String LASTNAME_KEY = "lastname";
    public static final String IMAGE_KEY = "image";
    public static final int EDIT_PROFILE_KEY = 99;

    ImageView ivPfp;
    TextView tvFirstNameP;
    TextView tvLastNameP;
    TextView tvEmailP;
    Button btnEditProfile;
    Button btnLogout;
    Button btnEditPassword;

    private ParseUser currentUser;
    Context context;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Log.d(TAG, "onActivityResult: ");
            if (result.getResultCode() == Activity.RESULT_OK) {
                // There are no request codes
                Intent intent = result.getData();
                String firstName = intent.getStringExtra(FIRSTNAME_KEY);
                String lastName = intent.getStringExtra(LASTNAME_KEY);
                String email = intent.getStringExtra(EMAIL_KEY);
                tvFirstNameP.setText(firstName);
                tvLastNameP.setText(lastName);
                tvEmailP.setText(email);
            } else {
                Log.e(TAG, "result code not OK some error");
            }
        }
    });

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
        btnEditPassword = view.findViewById(R.id.btnEditPassword);

        currentUser.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                populateProfileData();
                loadProfilePic();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Edit Profile Button Clicked");
                Intent intent = new Intent(getActivity().getBaseContext(), EditProfileActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        btnEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Edit Password Button Clicked");
                Intent intent = new Intent(getActivity().getBaseContext(), PasswordChangeActivity.class);
                getActivity().startActivity(intent);
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

    private void loadProfilePic() {
        Log.i(TAG, "Entering loadProfilePic");
        ParseFile file = currentUser.getParseFile(IMAGE_KEY);
        if(file == null) {
            Log.i(TAG, "User has no pfp");
            return;
        }
        Glide.with(context).load(file.getUrl()).transform(new RoundedCorners(50)).into(ivPfp);
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
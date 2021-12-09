package com.example.eventwithus.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.eventwithus.EditProfileActivity;
import com.example.eventwithus.LoginActivity;
import com.example.eventwithus.PasswordChangeActivity;
import com.example.eventwithus.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    public static final String FIRSTNAME_KEY = "firstname";
    public static final String LASTNAME_KEY = "lastname";
    public static final String EMAIL_KEY = "email";
    public static final String CITY_KEY = "city";
    public static final String IMAGE_KEY = "image";
    public static final String BIO_KEY = "bio";

    ImageView ivPfp;
    TextView tvName;
    TextView tvEmail;
    TextView tvCity;
    TextView tvBio;
    ImageButton btnEditProfile;
    Button btnLogout;
    Button btnEditPassword;

    private ParseUser currentUser;
    Context context;

    final ActivityResultLauncher<Intent> editProfileActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "onActivityResult: ");
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent intent = result.getData();
                        if (intent != null) {
                            String firstName = intent.getStringExtra(FIRSTNAME_KEY);
                            String lastName = intent.getStringExtra(LASTNAME_KEY);
                            String email = intent.getStringExtra(EMAIL_KEY);
                            String city = intent.getStringExtra(CITY_KEY);
                            String bio = intent.getStringExtra(BIO_KEY);
                            tvName.setText(String.format("%s %s", firstName, lastName));
                            tvEmail.setText(email);
                            tvCity.setText(city);
                            if (bio.isEmpty())
                                tvBio.setText(R.string.profile_fragment_label_no_bio);
                            else
                                tvBio.setText(bio);
                        }
                    } else {
                        Log.e(TAG, "Bad result code: " + result.getResultCode());
                    }
                }
            });

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity activity = getActivity();

        currentUser = ParseUser.getCurrentUser();
        context = getContext();
        ivPfp = view.findViewById(R.id.imageViewProfilePicture);
        tvName = view.findViewById(R.id.textViewName);
        tvEmail = view.findViewById(R.id.textViewEmail);
        tvCity = view.findViewById(R.id.textViewCity);
        tvBio = view.findViewById(R.id.textViewProfileBio);
        btnEditProfile = view.findViewById(R.id.buttonEditProfile);
        btnLogout = view.findViewById(R.id.buttonLogout);
        btnEditPassword = view.findViewById(R.id.buttonResetPassword);

        currentUser.fetchInBackground((object, e) -> {
            populateProfileData();
            loadProfilePic();
        });

        if (activity == null) {
            Log.e(TAG, "Could not retrieve activity");
        }

        btnEditProfile.setOnClickListener(v -> {
            Log.i(TAG, "Edit Profile Button Clicked");
            Intent intent = new Intent(context, EditProfileActivity.class);
            editProfileActivityResultLauncher.launch(intent);
        });

        btnEditPassword.setOnClickListener(v -> {
            Log.i(TAG, "Edit Password Button Clicked");
            Intent intent = new Intent(context, PasswordChangeActivity.class);
            context.startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Log.i(TAG, "Logout Button Clicked");
            ParseUser.logOut();
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            if (activity != null) {
                activity.finish();
            }
        });
    }

    private void loadProfilePic() {
        Log.i(TAG, "Entering loadProfilePic");
        ParseFile file = currentUser.getParseFile(IMAGE_KEY);
        if(file == null) {
            Log.i(TAG, "User has no profile picture");
            return;
        }
        Glide.with(context)
                .load(file.getUrl())
                .circleCrop()
                .into(ivPfp);
    }

    private void populateProfileData() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            String name = String.format("%s %s",
                    currentUser.getString(FIRSTNAME_KEY),
                    currentUser.getString(LASTNAME_KEY));
            String email = currentUser.getString(EMAIL_KEY);
            String city = currentUser.getString(CITY_KEY);
            String bio = currentUser.getString(BIO_KEY);

            tvName.setText(name);
            tvEmail.setText(email);
            tvCity.setText(city);
            if (bio == null || bio.isEmpty())
                tvBio.setText(R.string.profile_fragment_label_no_bio);
            else
                tvBio.setText(bio);
        }
    }
}
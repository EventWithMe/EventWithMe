package com.example.eventwithus.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.eventwithus.EditProfileActivity;
import com.example.eventwithus.LoginActivity;
import com.example.eventwithus.PasswordChangeActivity;
import com.example.eventwithus.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

@SuppressWarnings("FieldCanBeLocal")
public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment"; // tag for logging
    public static final String FIRSTNAME_KEY = "firstname";
    public static final String LASTNAME_KEY = "lastname";
    public static final String EMAIL_KEY = "email";
    public static final String CITY_KEY = "city";
    public static final String IMAGE_KEY = "image";

    ImageView ivPfp;
    TextView tvFirstNameP;
    TextView tvLastNameP;
    TextView tvEmailP;
    TextView tvCityP;
    Button btnEditProfile;
    Button btnLogout;
    Button btnEditPassword;

    private ParseUser currentUser;
    Context context;

    final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
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
                            tvFirstNameP.setText(firstName);
                            tvLastNameP.setText(lastName);
                            tvEmailP.setText(email);
                            tvCityP.setText(city);
                        }
                    } else {
                        Log.e(TAG, "result code not OK some error: " + result.getResultCode());
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        currentUser = ParseUser.getCurrentUser(); // initializes the ParseUser
        context = getContext();
        ivPfp = view.findViewById(R.id.ivPfp);
        tvFirstNameP = view.findViewById(R.id.tvFirstNameP);
        tvLastNameP = view.findViewById(R.id.tvLastNameP);
        tvEmailP = view.findViewById(R.id.tvEmailP);
        tvCityP = view.findViewById(R.id.tvCityP);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnEditPassword = view.findViewById(R.id.btnEditPassword);

        currentUser.fetchInBackground((object, e) -> {
            populateProfileData();
            loadProfilePic();
        });

        btnEditProfile.setOnClickListener(v -> {
            Log.i(TAG, "Edit Profile Button Clicked");
            Intent intent = new Intent(getActivity().getBaseContext(), EditProfileActivity.class);
            activityResultLauncher.launch(intent);
        });

        btnEditPassword.setOnClickListener(v -> {
            Log.i(TAG, "Edit Password Button Clicked");
            Intent intent = new Intent(getActivity().getBaseContext(), PasswordChangeActivity.class);
            getActivity().startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Log.i(TAG, "Logout Button Clicked");
            ParseUser.logOut();
            Intent intent = new Intent(getActivity().getBaseContext(), LoginActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
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
        FragmentActivity activity = getActivity();
        if (activity != null) {
            String firstName = String.format("%s   %s",
                    activity.getString(R.string.profile_fragment_label_first_name),
                    currentUser.getString(FIRSTNAME_KEY));

            String lastName = String.format("%s   %s",
                    activity.getString(R.string.profile_fragment_label_last_name),
                    currentUser.getString(LASTNAME_KEY));

            String email = String.format("%s   %s",
                    activity.getString(R.string.profile_fragment_label_email),
                    currentUser.getString(EMAIL_KEY));

            String city = String.format("%s   %s",
                    activity.getString(R.string.profile_fragment_label_city),
                    currentUser.getString(CITY_KEY));

            tvFirstNameP.setText(firstName);
            tvLastNameP.setText(lastName);
            tvEmailP.setText(email);
            tvCityP.setText(city);

        }
    }
}
package com.example.eventwithus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.*;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PasswordChangeActivity extends AppCompatActivity {

    public static final String TAG = "EditProfileFragment"; // tag for logging
    public static final String PASSWORD_KEY= "pBackup";

    EditText etCurrentPassPE;
    EditText etNewPasswordPE;
    EditText etConfirmPasswordPE;
    Button btnSubmit;

    private ParseUser currentUser;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        etCurrentPassPE = findViewById(R.id.etCurrentPassPE);
        etNewPasswordPE = findViewById(R.id.etNewPasswordPE);
        etConfirmPasswordPE = findViewById(R.id.etConfirmPasswordPE);
        btnSubmit = findViewById(R.id.btnSavePassword);

        currentUser = ParseUser.getCurrentUser();
        context = getApplicationContext();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordValidator();
            }
        });
    }

    private void passwordValidator() {
        String currentPassword = etCurrentPassPE.getText().toString();
        String newPassword = etNewPasswordPE.getText().toString();
        String confirmPassword = etConfirmPasswordPE.getText().toString();

        String truePassword = currentUser.getString(PASSWORD_KEY);

        if(currentPassword.equals("") || newPassword.equals("") || confirmPassword.equals("")){
            Toast.makeText(context, "please fill out all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!currentPassword.equals(truePassword)) {
            Toast.makeText(context, "Incorrect current password please try again", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!newPassword.equals(confirmPassword)) {
            Toast.makeText(context, "New password does not match confirm password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Regex to check valid password.
        String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[!@#$%^&+=])" + "(?=\\S+$).{8,20}$";

        // Compile the ReGex
        Pattern pattern = Pattern.compile(regex);

        // Pattern class contains matcher() method to find matching between given password and regular expression.
        Matcher matchNew = pattern.matcher(newPassword);
        Matcher matchConfirm = pattern.matcher(newPassword);

        if(!matchNew.matches()) {
            Toast.makeText(context, "New password does not meet the password requirements", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!matchConfirm.matches()) {
            Toast.makeText(context, "Confirm password does not meet the password requirements", Toast.LENGTH_SHORT).show();
            return;
        }

        // save password to the DB
        currentUser.setPassword(newPassword);
        currentUser.put(PASSWORD_KEY, newPassword);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (null == e) {
                    Log.i(TAG, "Password change saved successfully");
                    Toast.makeText(context, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e(TAG, "Password change failed to save");
                }
            }
        });
    }
}
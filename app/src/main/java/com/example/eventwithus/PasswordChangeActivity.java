package com.example.eventwithus;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordChangeActivity extends AppCompatActivity {

    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])" + "(?=\\S+$).{8,20}$";
    public static final String TAG = "EditProfileFragment"; // tag for logging
    public static final String PASSWORD_KEY= "pBackup";

    TextInputLayout layoutCurrentPass;
    TextInputLayout layoutNewPassword;
    TextInputLayout layoutConfirmPassword;
    EditText etCurrentPass;
    EditText etNewPassword;
    EditText etConfirmPassword;
    Button btnSubmit;

    private ParseUser currentUser;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);


        layoutCurrentPass = findViewById(R.id.textLayoutPasswordChangeCurrentPassword);
        layoutNewPassword = findViewById(R.id.textLayoutPasswordChangeNewPassword);
        layoutConfirmPassword = findViewById(R.id.textLayoutPasswordChangeConfirmPassword);
        etCurrentPass = findViewById(R.id.editTextPasswordChangeCurrentPassword);
        etNewPassword = findViewById(R.id.editTextPasswordChangeNewPassword);
        etConfirmPassword = findViewById(R.id.editTextPasswordChangeConfirmPassword);
        btnSubmit = findViewById(R.id.btnSavePassword);

        currentUser = ParseUser.getCurrentUser();
        context = getApplicationContext();

        layoutCurrentPass.setErrorIconDrawable(null);
        layoutNewPassword.setErrorIconDrawable(null);
        layoutConfirmPassword.setErrorIconDrawable(null);

        btnSubmit.setOnClickListener(view -> savePassword());

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                boolean passwordsMatch = etNewPassword.getText().toString()
                        .equals(etConfirmPassword.getText().toString());


                btnSubmit.setEnabled(!TextUtils.isEmpty(etCurrentPass.getText()) &&
                        !TextUtils.isEmpty(etNewPassword.getText()) && passwordsMatch &&
                        isValidPassword(etNewPassword.getText().toString()));

                if(TextUtils.isEmpty(etCurrentPass.getText()))
                    Log.d(TAG, "Current Empty");
                if(TextUtils.isEmpty(etNewPassword.getText()))
                    Log.d(TAG, "New Empty");
                if(passwordsMatch)
                    Log.d(TAG, "Passwords Match");
                if(isValidPassword(etNewPassword.getText().toString()))
                    Log.d(TAG, "Valid");


                if (s == etCurrentPass.getEditableText()) {
                    layoutCurrentPass.setError(null);
                }
                else if (s == etNewPassword.getEditableText()) {
                    if (passwordsMatch)
                        layoutConfirmPassword.setError(null);
                    CharSequence currentError = layoutNewPassword.getError();
                    if (currentError != null && isValidPassword(etNewPassword.getText().toString()))
                        layoutNewPassword.setError(null);
                }
                else if (s == etConfirmPassword.getEditableText()) {
                    if (passwordsMatch)
                        layoutConfirmPassword.setError(null);
                    else
                        layoutConfirmPassword.setError(getString(R.string.password_change_activity_error_password_mismatch));
                }
            }
        };

        View.OnFocusChangeListener onFocusChangeListener = (view, hasFocus) -> {
            if (!hasFocus) {
                int id = view.getId();
                if (id == R.id.editTextPasswordChangeCurrentPassword) {
                    if (TextUtils.isEmpty(etCurrentPass.getText()))
                        layoutCurrentPass.setError(getString(R.string.signup_activity_error_empty_password));
                }
                else if (id == R.id.editTextPasswordChangeNewPassword) {
                    if (TextUtils.isEmpty(etNewPassword.getText()))
                        layoutNewPassword.setError(getString(R.string.signup_activity_error_empty_password));
                    else if (!isValidPassword(etNewPassword.getText().toString()))
                        layoutNewPassword.setError(getString(R.string.password_change_activity_error_password_requirements));
                }
            }
        };

        etCurrentPass.addTextChangedListener(textWatcher);
        etNewPassword.addTextChangedListener(textWatcher);
        etConfirmPassword.addTextChangedListener(textWatcher);

        etCurrentPass.setOnFocusChangeListener(onFocusChangeListener);
        etNewPassword.setOnFocusChangeListener(onFocusChangeListener);
    }

    private void savePassword() {
        String currentPassword = etCurrentPass.getText().toString();

        ParseUser.logInInBackground(ParseUser.getCurrentUser().getUsername(), currentPassword,
                this::loginCallback);
    }

    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void loginCallback(ParseUser user, ParseException e) {
        if (e != null) {
            Log.e(TAG, "Issue during password reset", e);
            if (e.getCode() == 101) {
                layoutNewPassword.setError(getString(R.string.password_change_activity_error_password_incorrect));
            }
            else {
                Toast.makeText(this, getString(R.string.password_change_activity_error_unknown), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            String newPassword = etNewPassword.getText().toString();
            // save password to the DB
            currentUser.setPassword(newPassword);
            currentUser.put(PASSWORD_KEY, newPassword);
            currentUser.saveInBackground(err -> {
                if (err != null) {
                    Log.e(TAG, "Password change failed to save", err);
                    Toast.makeText(context, getString(R.string.password_change_activity_error_unknown), Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.i(TAG, "Password change saved successfully");
                    Toast.makeText(context, getString(R.string.password_change_activity_toast_success), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }
}
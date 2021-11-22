package com.example.eventwithus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    TextInputLayout textLayoutUsername;
    TextInputLayout textLayoutEmail;
    TextInputLayout textLayoutPassword;
    TextInputLayout textLayoutConfirmPassword;
    EditText editTextUsername;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextConfirmPassword;
    Button buttonSignUp;
    Button buttonLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        textLayoutUsername = findViewById(R.id.textLayoutSignupUsername);
        textLayoutEmail = findViewById(R.id.textLayoutSignupEmail);
        textLayoutPassword = findViewById(R.id.textLayoutSignupPassword);
        textLayoutConfirmPassword = findViewById(R.id.textLayoutSignupConfirmPassword);
        editTextUsername = findViewById(R.id.editTextSignupUsername);
        editTextEmail = findViewById(R.id.editTextSignupEmail);
        editTextPassword = findViewById(R.id.editTextSignupPassword);
        editTextConfirmPassword = findViewById(R.id.editTextSignupConfirmPassword);
        buttonSignUp = findViewById(R.id.buttonSignupSignUp);
        buttonLogIn = findViewById(R.id.buttonSignupLogIn);

        buttonSignUp.setEnabled(false);

        buttonSignUp.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            String confirmPassword = editTextConfirmPassword.getText().toString();
            if (password.equals(confirmPassword)) {
                signupUser(username, email, password);
            }
            else {
                Toast.makeText(this, getString(R.string.errorPasswordMismatch), Toast.LENGTH_SHORT).show();
            }
        });

        buttonLogIn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Watches for change in a text box and updates whether Log In button is enabled
        // Also updates displayed errors as necessary
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                boolean passwordsMatch = editTextPassword.getText().toString()
                        .equals(editTextConfirmPassword.getText().toString());
                boolean validEmail = Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText()
                        .toString()).matches();

                buttonSignUp.setEnabled(!TextUtils.isEmpty(editTextUsername.getText()) &&
                        !TextUtils.isEmpty(editTextPassword.getText()) &&
                        passwordsMatch && validEmail);

                if (s == editTextUsername.getEditableText())
                    textLayoutUsername.setError(null);
                else if (s == editTextEmail.getEditableText()) {
                    if (validEmail || TextUtils.equals(textLayoutEmail.getError(),
                            getText(R.string.errorEmptyEmail)))
                        textLayoutEmail.setError(null);
                }
                else if (s == editTextPassword.getEditableText()) {
                    if (!TextUtils.isEmpty(editTextConfirmPassword.getText())) {
                        if (passwordsMatch)
                            textLayoutConfirmPassword.setError(null);
                        else
                            textLayoutConfirmPassword.setError(getString(R.string.errorPasswordMismatch));
                    }
                    textLayoutPassword.setError(null);
                }
                else if (s == editTextConfirmPassword.getEditableText()) {
                    if (passwordsMatch)
                        textLayoutConfirmPassword.setError(null);
                    else
                        textLayoutConfirmPassword.setError(getString(R.string.errorPasswordMismatch));
                }
            }
        };

        // Watches for user de-selecting a text box and displays appropriate errors
        View.OnFocusChangeListener onFocusChangeListener = (view, hasFocus) -> {
            if (!hasFocus) {
                int id = view.getId();
                if (id == R.id.editTextSignupUsername) {
                    if (TextUtils.isEmpty(editTextUsername.getText()))
                        textLayoutUsername.setError(getString(R.string.errorEmptyUsername));
                    else {
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereEqualTo("username", editTextUsername.getText().toString());
                        query.findInBackground((users, e) -> {
                           if (e == null) {
                               if (users.size() > 0)
                                   textLayoutUsername.setError(getString(R.string.errorUsernameTaken));
                           }
                           else {
                               Log.e(TAG, "Username lookup error", e);
                           }
                        });
                    }
                }
                else if (id == R.id.editTextSignupEmail) {
                    if (TextUtils.isEmpty(editTextEmail.getText()))
                        textLayoutEmail.setError(getString(R.string.errorEmptyEmail));
                    else if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches())
                        textLayoutEmail.setError(getString(R.string.errorInvalidEmail));
                    else {
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereEqualTo("email", editTextEmail.getText().toString());
                        query.findInBackground((users, e) -> {
                            if (e == null) {
                                if (users.size() > 0)
                                    textLayoutEmail.setError(getString(R.string.errorEmailTaken));
                            }
                            else {
                                Log.e(TAG, "Email lookup error", e);
                            }
                        });
                    }
                }
                else if (id == R.id.editTextSignupPassword) {
                    if (TextUtils.isEmpty(editTextPassword.getText()))
                        textLayoutPassword.setError(getString(R.string.errorEmptyPassword));
                }
                else if (id == R.id.editTextSignupConfirmPassword) {
                    if (TextUtils.isEmpty(editTextConfirmPassword.getText()))
                        textLayoutConfirmPassword.setError(getString(R.string.errorEmptyPassword));
                }
            }
        };

        editTextUsername.addTextChangedListener(textWatcher);
        editTextEmail.addTextChangedListener(textWatcher);
        editTextPassword.addTextChangedListener(textWatcher);
        editTextConfirmPassword.addTextChangedListener(textWatcher);

        editTextUsername.setOnFocusChangeListener(onFocusChangeListener);
        editTextEmail.setOnFocusChangeListener(onFocusChangeListener);
        editTextPassword.setOnFocusChangeListener(onFocusChangeListener);
        editTextConfirmPassword.setOnFocusChangeListener(onFocusChangeListener);
    }

    private void signupUser(String username, String email, String password) {

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);

        user.signUpInBackground(e -> {
            if (e != null) {
                Log.e(TAG, "Issue logging in", e);
                if (e.getCode() == 202)
                    textLayoutUsername.setError(getString(R.string.errorUsernameTaken));
                else if (e.getCode() == 203)
                    textLayoutEmail.setError(getString(R.string.errorEmailTaken));
                else if (e.getCode() == 125)
                    textLayoutEmail.setError(getString(R.string.errorInvalidEmail));
                else
                    Toast.makeText(this, getString(R.string.errorUnknownSignupError),
                            Toast.LENGTH_LONG).show();
            }
            else {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }
}
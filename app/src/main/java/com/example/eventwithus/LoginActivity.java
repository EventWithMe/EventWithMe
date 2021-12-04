package com.example.eventwithus;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private boolean clearErrorsOnTyping = false;

    TextInputLayout textLayoutUsername;
    TextInputLayout textLayoutPassword;
    EditText editTextUsername;
    EditText editTextPassword;
    Button buttonLogIn;
    Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

        textLayoutUsername = findViewById(R.id.textLayoutLoginUsername);
        textLayoutPassword = findViewById(R.id.textLayoutLoginPassword);
        editTextUsername = findViewById(R.id.editTextLoginUsername);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
        buttonLogIn = findViewById(R.id.buttonLoginSignUp);
        buttonSignUp = findViewById(R.id.buttonLoginLogIn);

        buttonLogIn.setEnabled(false);

        buttonLogIn.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();
            loginUser(username, password);
        });
      

        buttonSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        });

        // Watches for change in a text box and updates whether Log In btnRSVP is enabled
        // Also clears any error from the text being empty
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                buttonLogIn.setEnabled(!TextUtils.isEmpty(editTextUsername.getText()) &&
                        !TextUtils.isEmpty(editTextPassword.getText()));
                if (s == editTextUsername.getEditableText() || clearErrorsOnTyping)
                    textLayoutUsername.setError(null);
                if (s == editTextPassword.getEditableText() || clearErrorsOnTyping)
                    textLayoutPassword.setError(null);
            }
        };

        // Watches for user de-selecting a text box and displays an error if it is empty
        View.OnFocusChangeListener onFocusChangeListener = (view, hasFocus) -> {
            if (!hasFocus) {
                int id = view.getId();
                if (id == R.id.editTextLoginUsername) {
                    if (TextUtils.isEmpty(editTextUsername.getText())) {
                        textLayoutUsername.setError(getString(R.string.errorEmptyUsernameEmail));
                        clearErrorsOnTyping = false;
                    }
                }
                else if (id == R.id.editTextLoginPassword) {
                    if (TextUtils.isEmpty(editTextPassword.getText())) {
                        textLayoutPassword.setError(getString(R.string.errorEmptyPassword));
                        clearErrorsOnTyping = false;
                    }
                }
            }
        };

        editTextUsername.addTextChangedListener(textWatcher);
        editTextPassword.addTextChangedListener(textWatcher);

        editTextUsername.setOnFocusChangeListener(onFocusChangeListener);
        editTextPassword.setOnFocusChangeListener(onFocusChangeListener);
    }

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue logging in", e);
                if (e.getCode() == 101) {
                    textLayoutUsername.setError(getString(R.string.errorInvalidCredentials));
                    textLayoutPassword.setError(getString(R.string.errorInvalidCredentials));
                    clearErrorsOnTyping = true;
                }
                else
                    Toast.makeText(this, getString(R.string.errorUnknownLoginError), Toast.LENGTH_LONG).show();
            }
            else {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }
}
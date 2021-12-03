package com.example.eventwithus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

// TODO: 12/2/2021 fix the UI & write the fucntionality

public class PasswordChangeActivity extends AppCompatActivity {

    public static final String TAG = "EditProfileFragment"; // tag for logging
    public static final String PASSWORD_KEY= "password";

    EditText etCurrentPassPE;
    EditText etPasswordPE;
    EditText etConfirmPasswordPE;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        etCurrentPassPE = findViewById(R.id.etCurrentPassPE);
        etPasswordPE = findViewById(R.id.etPasswordPE);
        etConfirmPasswordPE = findViewById(R.id.etConfirmPasswordPE);
    }
}
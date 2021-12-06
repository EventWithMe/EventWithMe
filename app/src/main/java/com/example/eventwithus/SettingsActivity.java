package com.example.eventwithus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.parse.ParseUser;

public class SettingsActivity extends AppCompatActivity {

    private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        currentUser = ParseUser.getCurrentUser();
    }
}
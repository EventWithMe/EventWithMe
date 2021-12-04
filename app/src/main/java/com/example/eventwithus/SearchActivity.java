package com.example.eventwithus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SearchActivity extends AppCompatActivity {


    String[] Categories = { "Concerts", "Sports", "Arts & Theater", "Family", "Film", "Misc"};
    String[] Dates = { "All Dates", "This Weekend"};
    String[] Distance = { "10 mi", "25 mi", "50 mi","75 mi","All (mi)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);









        setContentView(R.layout.activity_search);
    }
}
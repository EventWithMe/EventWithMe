package com.example.eventwithus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MyEventDetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView nameTV;
    TextView cityTV;
    TextView timeTV;
    TextView venueTV;
    TextView descriptionTV;
    TextView dateTV;
    Button rsvpBtn;
    Button chatBtn;
    Button linkBtn;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_detail);

        Bundle extras = getIntent().getExtras();
        imageView = findViewById(R.id.imageView);
        nameTV = findViewById(R.id.nameTV);
        cityTV = findViewById(R.id.cityTV);
        timeTV = findViewById(R.id.timeTV);
        venueTV = findViewById(R.id.venueTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        dateTV = findViewById(R.id.dateTv);
        rsvpBtn = findViewById(R.id.rsvpBtn);
        chatBtn = findViewById(R.id.chatBtn);
        linkBtn = findViewById(R.id.linkBtn);

        if (extras != null) {
            String name = extras.getString("nameTV");
            String city = extras.getString("cityTV");
            String time = extras.getString("timeTV");
            String venue = extras.getString("venueTV");
            String description = extras.getString("descriptionTV");
            String date = extras.getString("dateTV");
            String image = extras.getString("imageView");

            nameTV.setText(name);
            cityTV.setText("City: " + city);
            timeTV.setText("Starts At: " + time);
            venueTV.setText("Venue: " + venue);
            descriptionTV.setText("Event ID : " + description);
            dateTV.setText("Date: " + date);

            if(image != null){
                Picasso.with(this).load(image).fit().centerInside().into(imageView);
            }
        }

        rsvpBtn.setOnClickListener(view -> {
        //TODO RSVP user to the event chosen
        });

        chatBtn.setOnClickListener(view -> {
        //TODO direct to events chat
        });

        linkBtn.setOnClickListener(view -> {
            String url = extras.getString("eventLink");

            Uri uri = Uri.parse(url);
            Intent intent= new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);
        });





    }
}
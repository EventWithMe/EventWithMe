package com.example.eventwithus;

import static com.example.eventwithus.MainActivity.EXTRA_EVENT_NAME;
import static com.example.eventwithus.MainActivity.EXTRA_EVENT_TYPE;
import static com.example.eventwithus.MainActivity.EXTRA_URL;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String eventName = intent.getStringExtra(EXTRA_EVENT_NAME);
        String eventType = intent.getStringExtra(EXTRA_EVENT_TYPE);

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textviewEventName = findViewById(R.id.text_view_event_name);
        TextView textviewEventType = findViewById(R.id.text_view_event_desc);

        Picasso.with(this).load(imageUrl).fit().centerInside().into(imageView);
        textviewEventName.setText(eventName);
        textviewEventType.setText("Info: " + eventType);
    }
}
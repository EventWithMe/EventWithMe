package com.example.eventwithus;

import static com.example.eventwithus.MainActivity.EXTRA_EVENT_NAME;
import static com.example.eventwithus.MainActivity.EXTRA_EVENT_TYPE;
import static com.example.eventwithus.MainActivity.EXTRA_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventwithus.fragments.RsvpFragment;
import com.example.eventwithus.models.EventHelper;
import com.example.eventwithus.models.User;
import com.example.eventwithus.models.UserParse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventDetailActivity extends AppCompatActivity {

    public static final String TAG = "EventDetailActivity"; // tag for logging
    public static final String PARSE_RSVP_KEY= "eventinfo"; // key to get and update the eventinfo column in the User object in Parse DB
    public static final String EXTRA_EVENT_DATE = "date"; // used to extract date data from intent

    // UI elements
    Button btnRSVP;
    TextView textviewEventName;
    TextView textviewEventType;
    TextView tvDate;
    ImageView imageView;

    private boolean rsvp;
    private ParseUser currentUser;
    List<String> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        currentUser = ParseUser.getCurrentUser();
        events = EventHelper.getLoggedInUserEvents(PARSE_RSVP_KEY);
        rsvpCheck();

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String eventName = intent.getStringExtra(EXTRA_EVENT_NAME);
        String eventType = intent.getStringExtra(EXTRA_EVENT_TYPE);
        String eventDate = intent.getStringExtra(EXTRA_EVENT_DATE);

        imageView = findViewById(R.id.image_view_detail);
        textviewEventName = findViewById(R.id.text_view_event_name);
        textviewEventType = findViewById(R.id.text_view_event_desc);
        tvDate = findViewById(R.id.tvDate);

        Picasso.with(this).load(imageUrl).fit().centerInside().transform(new RoundedTransformation(50, 0)).into(imageView);
        textviewEventName.setText(eventName);
        textviewEventType.setText("Info: " + eventType);
        tvDate.setText(eventDate);

        btnRSVP = findViewById(R.id.btnRSVP);

        if(rsvp) {
            btnRSVP.setText("Cancel RSVP");
        }

        btnRSVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "btnRSVP clicked date: " + eventDate);

                if(rsvp) {
                    cancelRSVP("123");
                } else {
                    // TODO: 12/1/2021 add proper id variable and date
                    rsvpEvent("123", eventDate);
                }
            }
        });
    }

    private void rsvpCheck() {
        for(String s : events) {
            String[] eventInfo = s.split("_");
            if(eventInfo[1].equals("EventId")){
                rsvp = true;
            } else {
                rsvp = false;
            }
        }
    }

    private void cancelRSVP(String eventId) {
        for(int i = 0; i < events.size(); i++) {
            String[] format = events.get(i).split("_");
            if(format[0].equals(eventId)){
                events.remove(i);
            }
        }

        String updated = "";

        for(int i = 0; i < events.size() - 1; i++) {
            updated += events.get(i) + ", ";
        }
        updated += events.get(events.size() - 1);

        currentUser.put(PARSE_RSVP_KEY, updated);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "You have cancelled your RSVP", Toast.LENGTH_SHORT).show();
                    btnRSVP.setText("RSVP");
                    rsvp = false;
                } else {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }

    // updates Parse DB User eventsinfo column
    private void rsvpEvent(String eventId, String date) {
        String events = currentUser.getString(PARSE_RSVP_KEY);
        events += "," + eventId + "_" + date;
        currentUser.put(PARSE_RSVP_KEY, events);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "You have RSVP'd", Toast.LENGTH_SHORT).show();
                    btnRSVP.setText("Cancel RSVP");
                    rsvp = true;
                } else {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }
}
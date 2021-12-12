package com.example.eventwithus;

import static com.example.eventwithus.MainActivity.EXTRA_EVENT_NAME;
import static com.example.eventwithus.MainActivity.EXTRA_EVENT_TYPE;
import static com.example.eventwithus.MainActivity.EXTRA_URL;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.eventwithus.models.EventHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import java.util.List;

public class EventDetailActivity extends AppCompatActivity {

    public static final String TAG = "EventDetailActivity"; // tag for logging
    public static final String PARSE_RSVP_KEY= "eventsinfo"; // key to get and update the eventinfo column in the User object in Parse DB
    public static final String EXTRA_EVENT_DATE = "date"; // used to extract date data from intent
    public static final String EXTRA_EVENT_ID = "id"; // used to extract date data from intent
    public static final String EXTRA_EVENT_VENUE_NAME = "venueName"; // used to extract date data from intent
    public static final String EXTRA_EVENT_START_TIME = "startTime"; // used to extract date data from intent
    public static final String EXTRA_VENUE_CITY = "venueCity"; // used to extract date data from intent

    // UI elements
    FloatingActionButton btnRSVP;
    TextView textviewEventName;
    TextView textviewEventType;
    TextView textviewEventVenue;
    TextView tvTimeDate;
    ImageView imageView;

    private boolean rsvp; // variables is used to check if user has already rsvp'd to the event
    private ParseUser currentUser; // used to change the events data throughout the scene
    private View root;
    List<String> events; // holds all the users event data if there is any
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        EventHelper.refreshUserData();
        currentUser = ParseUser.getCurrentUser(); // initializes the ParseUser
        events = EventHelper.getLoggedInUserEvents(PARSE_RSVP_KEY); // calls EventHelper to get list of current user event data
        context = getApplicationContext();

        // extract all intent data about the event from and store in variables
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String eventName = intent.getStringExtra(EXTRA_EVENT_NAME);
        String eventDescription = intent.getStringExtra(EXTRA_EVENT_TYPE);
        String eventDate = intent.getStringExtra(EXTRA_EVENT_DATE);
        String eventID = intent.getStringExtra(EXTRA_EVENT_ID);
        String[] arr = intent.getStringExtra(EXTRA_VENUE_CITY).split(":");
        String venueCity = EventHelper.cityFormatter(arr[1]);
        String venueName = intent.getStringExtra(EXTRA_EVENT_VENUE_NAME) + " in " + venueCity;
        String startTime =  intent.getStringExtra(EXTRA_EVENT_START_TIME);
        String timeDate = EventHelper.formatJsonDate(eventDate) + " " + getResources().getString(R.string.event_detail_activity_date_time_at) + " " + EventHelper.startTimeFormatter(startTime);

        // initialize the UI elements
        imageView = findViewById(R.id.image_view_detail);
        textviewEventName = findViewById(R.id.text_view_event_name);
        textviewEventType = findViewById(R.id.text_view_event_desc);
        textviewEventVenue = findViewById(R.id.text_view_venue_info);
        tvTimeDate = findViewById(R.id.tvTimeDate);
        btnRSVP = findViewById(R.id.btnRSVP);

        // populate the UI elements with event data
        Picasso.with(this).load(imageUrl).fit().centerInside().transform(new RoundedTransformation(50, 0)).into(imageView);
        textviewEventName.setText(eventName);
        textviewEventType.setText(String.format(getString(R.string.event_detail_activity_type_label), eventDescription));
        textviewEventVenue.setText(venueName);
        tvTimeDate.setText(timeDate);

        //printEvents();

        // if the user has event data proceed to check if he is already rsvp'd
        if(events.size() == 1) {
            rsvp = false;
        } else {
            rsvpCheck(eventID);
        }

        // if the user is already rsvp'd then set the btnEditProfile to cancel
        if(rsvp) {
            btnRSVP.setImageResource(R.drawable.ic_favorites);
        }

        btnRSVP.setOnClickListener(view -> {
            Log.d(TAG, "btnRSVP clicked date: " + eventID + " at " + eventDate);
            EventHelper.refreshUserData();
            if(rsvp) {
                Toast mytoast = Toast.makeText(getApplicationContext(), "Unfavorited", Toast.LENGTH_SHORT);
                mytoast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 1000);
                mytoast.show();
                cancelRSVP(eventID);
            } else {
                Toast mytoast = Toast.makeText(getApplicationContext(), "Favorited", Toast.LENGTH_SHORT);
                mytoast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 1000);
                mytoast.show();
                rsvpEvent(eventID, eventDate, eventDescription, eventName, venueName, imageUrl, startTime, venueCity);
            }
        });
    }

    // iterates through a list of events to check and see if the user has rsvp'd already
    private void rsvpCheck(String eventId) {
        Log.d(TAG, "RSVP CHECK:");
        for(String s : events) {
            String[] eventInfo = s.split(";");
            System.out.print("eventInfo[0] : "+eventInfo[0] + "  eventId : " + eventId);
            System.out.println();
            if(eventInfo[0].equals(eventId)){
                rsvp = true;
                return;
            } else {
                rsvp = false;
            }
        }
        System.out.print("RSVP VERDICT: " + rsvp);
    }

    // if user click the btnEditProfile when it says "cancel rsvp" then we remove the item from the list and DB
    private void cancelRSVP(String eventId) {
        Log.d(TAG, "CANCEL RSVP:");
        StringBuilder updated = new StringBuilder();

        Log.d(TAG, "EVENT LIST CHECKING:");
        for(int i = 0; i < events.size(); i++) {
            String[] format = events.get(i).split(";");
            Log.d(TAG, format[0] + " " + eventId);
            if(format[0].equals(eventId)) {
                events.remove(i);
                break;
            }
        }

        System.out.println("Events list after remove");
        System.out.println(events);

        for(int i = 0; i < events.size() - 1; i++) {
            updated.append(events.get(i)).append("<");
        }
        updated.append(events.get(events.size() - 1));

        /*
        currentUser.remove(PARSE_RSVP_KEY);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    btnRSVP.setText("RSVP");
                    rsvp = false;
                } else {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });
        EventHelper.refreshUserData();
        */

        currentUser.put(PARSE_RSVP_KEY, updated.toString());
        currentUser.saveInBackground(e -> {
            if (e == null) {
                btnRSVP.setImageResource(R.drawable.ic_favorites_not_pressed);
                rsvp = false;
            } else {
                Log.e(TAG, "Error: " + e.getMessage());
            }
        });
        EventHelper.refreshUserData();
    }

    // updates Parse DB User eventsinfo column
    private void rsvpEvent(String eventId, String date, String eventDescription, String eventName, String venueName, String imageUrl, String startTime, String venueCity) {
        Log.i(TAG, "adding event Id: " + eventId + " on " + date);
        String eventsinfo = currentUser.getString(PARSE_RSVP_KEY);
        eventsinfo += "<" + eventId.trim() + ";" + date.trim() + ";" + eventDescription.trim() + ";" + eventName.trim() + ";" + venueName.trim() + ";" + imageUrl.trim() + ";" + startTime + ";" + venueCity;
        currentUser.put(PARSE_RSVP_KEY, eventsinfo);
        currentUser.saveInBackground(e -> {
            if (e == null) {
                btnRSVP.setImageResource(R.drawable.ic_favorites);;
                rsvp = true;
                events = EventHelper.getLoggedInUserEvents(PARSE_RSVP_KEY);
            } else {
                Log.e(TAG, "Error: " + e.getMessage());
            }
        });
        EventHelper.refreshUserData();
    }

    private void printEvents() {
        events = EventHelper.getLoggedInUserEvents(PARSE_RSVP_KEY);
        Log.d(TAG, "Printing Events:");
        StringBuilder eventsStringBuilder = new StringBuilder();
        for(String s : events) {
            eventsStringBuilder.append(s).append(" ");
        }
        eventsStringBuilder.append("\n");

        Log.d(TAG, eventsStringBuilder.toString());
    }
}
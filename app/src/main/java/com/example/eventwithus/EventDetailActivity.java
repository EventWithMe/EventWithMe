package com.example.eventwithus;

import static com.example.eventwithus.MainActivity.EXTRA_EVENT_NAME;
import static com.example.eventwithus.MainActivity.EXTRA_EVENT_TYPE;
import static com.example.eventwithus.MainActivity.EXTRA_URL;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
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
    public static final String PARSE_RSVP_KEY= "eventsinfo"; // key to get and update the eventinfo column in the User object in Parse DB
    public static final String EXTRA_EVENT_DATE = "date"; // used to extract date data from intent

    // UI elements
    Button btnRSVP;
    TextView textviewEventName;
    TextView textviewEventType;
    TextView tvDate;
    ImageView imageView;

    private boolean rsvp; // variables is used to check if user has already rsvp'd to the event
    private ParseUser currentUser; // used to change the events data throughout the scene
    List<String> events; // holds all the users event data if there is any
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        EventHelper.refreshUserData();
        currentUser = ParseUser.getCurrentUser(); // initializes the Parseuser
        events = EventHelper.getLoggedInUserEvents(PARSE_RSVP_KEY); // calls EventHelper to get list of current user event data
        context = getApplicationContext();

        // extract all intent data about the event from and store in variables
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String eventName = intent.getStringExtra(EXTRA_EVENT_NAME);
        String eventType = intent.getStringExtra(EXTRA_EVENT_TYPE);
        String eventDate = intent.getStringExtra(EXTRA_EVENT_DATE);

        // initialize the UI elements
        imageView = findViewById(R.id.image_view_detail);
        textviewEventName = findViewById(R.id.text_view_event_name);
        textviewEventType = findViewById(R.id.text_view_event_desc);
        tvDate = findViewById(R.id.tvDate);
        btnRSVP = findViewById(R.id.btnRSVP);

        // populate the UI elements with event data
        Picasso.with(this).load(imageUrl).fit().centerInside().transform(new RoundedTransformation(50, 0)).into(imageView);
        textviewEventName.setText(eventName);
        textviewEventType.setText("Info: " + eventType);
        tvDate.setText(EventHelper.formatJsonDate(eventDate));

        //printEvents();

        // if the user has event data proceed to check if he is already rsvp'd
        if(events == null || events.isEmpty()) {
            rsvp = false;
        } else {
            rsvpCheck();
        }

        // if the user is already rsvp'd then set the button to cancel
        if(rsvp) {
            btnRSVP.setText("Cancel RSVP");
        }

        btnRSVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "btnRSVP clicked date: " + eventDate);
                EventHelper.refreshUserData();

                if(rsvp) {
                    cancelRSVP("123");
                } else {
                    // TODO: 12/1/2021 add proper id variable and date
                    rsvpEvent("123", eventDate);
                }
            }
        });
    }

    // iterates through a list of events to check and see if the user has rsvp'd already
    private void rsvpCheck() {
        System.out.println("RSVP CHECK:");
        for(String s : events) {
            String[] eventInfo = s.split("_");
            System.out.print(eventInfo[1] + " " + tvDate.getText().toString());
            System.out.println();
            if(eventInfo[1].equals(tvDate.getText().toString())){
                rsvp = true;
                return;
            } else {
                rsvp = false;
            }
        }
        System.out.print("RSVP VERDICT: " + rsvp);
    }

    // if user click the button when it says "cancel rsvp" then we remove the item from the list and DB
    private void cancelRSVP(String eventId) {
        System.out.println("CANCEL RSVP:");
        String updated = "";

        System.out.println("EVENT LIST CHECKING:");
        for(int i = 0; i < events.size(); i++) {
            String[] format = events.get(i).split("_");
            System.out.println(format[0] + " " + eventId);
            if(format[0].equals(eventId)) {

                events.remove(i);
            }
        }

        System.out.println("Events list after remove");
        System.out.println(events);

        for(int i = 0; i < events.size() - 1; i++) {
            updated += events.get(i) + ", ";
        }
        updated += events.get(events.size() - 1);

        /*currentUser.remove(PARSE_RSVP_KEY);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context, "You have cancelled your RSVP", Toast.LENGTH_SHORT).show();
                    btnRSVP.setText("RSVP");
                    rsvp = false;
                } else {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });
        EventHelper.refreshUserData();
        */

        currentUser.put(PARSE_RSVP_KEY, updated);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context, "You have cancelled your RSVP", Toast.LENGTH_SHORT).show();
                    btnRSVP.setText("RSVP");
                    rsvp = false;
                } else {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });

        EventHelper.refreshUserData();
    }

    // updates Parse DB User eventsinfo column
    private void rsvpEvent(String eventId, String date) {
        String eventsinfo = currentUser.getString(PARSE_RSVP_KEY);
        eventsinfo += "," + eventId + "_" + date;
        currentUser.put(PARSE_RSVP_KEY, eventsinfo);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context, "You have RSVP'd", Toast.LENGTH_SHORT).show();
                    btnRSVP.setText("Cancel RSVP");
                    rsvp = true;
                    events = EventHelper.getLoggedInUserEvents(PARSE_RSVP_KEY);
                } else {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });
        EventHelper.refreshUserData();
    }

    private void printEvents() {
        events = EventHelper.getLoggedInUserEvents(PARSE_RSVP_KEY);
        System.out.println("Printing Events:");
        for(String s : events) {
            System.out.print(s + " ");
        }
        System.out.println();
    }
}
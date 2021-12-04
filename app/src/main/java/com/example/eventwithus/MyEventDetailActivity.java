package com.example.eventwithus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventwithus.models.EventHelper;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyEventDetailActivity extends AppCompatActivity {

    public static final String TAG = "MyEventDetailActivity"; // tag for logging
    public static final String PARSE_RSVP_KEY= "eventsinfo"; // key to get and update the eventinfo column in the User object in Parse DB
    public static final String EXTRA_EVENT_DATE = "date"; // used to extract date data from intent
    private boolean rsvp; // variables is used to check if user has already rsvp'd to the event
    private ParseUser currentUser; // used to change the events data throughout the scene
    List<String> events;
    Context context;

    ImageView imageView;
    TextView nameTV;
    TextView cityTV;
    TextView timeTV;
    TextView venueTV;
    TextView descriptionTV;
    TextView dateTV;
    Button rsvpBtn;
    Button linkBtn;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_detail);

        events = EventHelper.getLoggedInUserEvents(PARSE_RSVP_KEY);
        currentUser = ParseUser.getCurrentUser();
        context = getApplicationContext();


        Intent intent = getIntent();
        String eventDate = intent.getStringExtra(EXTRA_EVENT_DATE);

        Bundle extras = getIntent().getExtras();
        imageView = findViewById(R.id.ivPfp);
        nameTV = findViewById(R.id.nameTV);
        cityTV = findViewById(R.id.cityTV);
        timeTV = findViewById(R.id.timeTV);
        venueTV = findViewById(R.id.venueTV);
        descriptionTV = findViewById(R.id.descriptionTV);
        dateTV = findViewById(R.id.dateTv);
        rsvpBtn = findViewById(R.id.rsvpBtn);
        linkBtn = findViewById(R.id.linkBtn);

        if (extras != null) {
            String name = extras.getString("nameTV");
            String city = extras.getString("cityTV");
            String time = extras.getString("timeTV");
            String venue = extras.getString("venueTV");
            String description = extras.getString("descriptionTV");
            String date = extras.getString("dateTV");
            String image = extras.getString("ivPfp");

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
//
//        if(rsvp) {
//            rsvpBtn.setText("Cancel RSVP");
//        }
//
//        if(events == null || events.isEmpty()) {
//            rsvp = false;
//        } else {
//            rsvpCheck();
//        }



        rsvpBtn.setOnClickListener(view -> {
//            Log.d(TAG, "btnRSVP clicked date: " + eventDate);
//            EventHelper.refreshUserData();
//
//            if(rsvp) {
//                cancelRSVP("123");
//            } else {
//                rsvpEvent("123", eventDate);
//            }
        });


        linkBtn.setOnClickListener(view -> {
            String url = extras.getString("eventLink");

            Uri uri = Uri.parse(url);
            Intent intent1= new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent1);
        });
    }
    // iterates through a list of events to check and see if the user has rsvp'd already
    private void rsvpCheck() {
        System.out.println("RSVP CHECK:");
        for(String s : events) {
            String[] eventInfo = s.split("_");
            System.out.print(eventInfo[1] + " " + EventHelper.formatJsonDate(dateTV.getText().toString()));
            System.out.println();
            if(eventInfo[1].equals(EventHelper.formatJsonDate(dateTV.getText().toString()))){
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

        currentUser.put(PARSE_RSVP_KEY, updated);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context, "You have cancelled your RSVP", Toast.LENGTH_SHORT).show();
                    rsvpBtn.setText("RSVP");
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
                    rsvpBtn.setText("Cancel RSVP");
                    rsvp = true;
                    events = EventHelper.getLoggedInUserEvents(PARSE_RSVP_KEY);
                } else {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });
        EventHelper.refreshUserData();
    }
}
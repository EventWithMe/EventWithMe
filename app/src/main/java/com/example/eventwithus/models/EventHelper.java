package com.example.eventwithus.models;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.eventwithus.R;
import com.parse.Parse;
import com.parse.ParseUser;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
    This classes purpose is just to have one place for static methods for assisting in events
 */
public class EventHelper {

    public static final String TAG = "EventHelper"; // tag for logging
    public static final String PARSE_EVENTS_KEY= "eventsinfo"; // key to get and update the eventinfo column in the User object in Parse DB

    // this method returns an ArrayList of Strings of the event information. Each string contains the event ID and event date formatted as eventID_eventDate
    public static List<String> getLoggedInUserEvents(String EVENTS_KEY) {
        List<String> eventsinfo = new ArrayList<>();
        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser.getString(EVENTS_KEY) == null) {
            return null;
        } else {
            Log.d(TAG, "NewData: " + currentUser.getString(EVENTS_KEY));
            String eventsString = currentUser.getString(EVENTS_KEY);
            String[] events = new String[0];
            if (eventsString != null) {
                events = eventsString.split("<");
            }
            Log.d(TAG, "EH EVENTS:");
            Log.d(TAG, Arrays.toString(events));

            for(String s : events) {
                System.out.println(s);
                if(!s.equals("null"))
                    eventsinfo.add(s.trim());
            }
            return eventsinfo;
        }
    }

    public static int getEventCount(ParseUser currentUser) {
        int count = 0;
        String events = currentUser.getString(PARSE_EVENTS_KEY);
        for (int i = 0; i < events.length(); i++) {
            if (events.charAt(i) == '<') {
                count++;
            }
        }
        Log.i(TAG ,"getEventCount : "+count);
        return count;
    }

    public static void refreshUserData() {
        ParseUser.getCurrentUser().fetchInBackground((object, e) -> {
            if (e == null) {
                Log.d(TAG, "User refresh successful");
            } else {
                Log.e(TAG, "Error: " + e.getMessage());
            }
        });
    }

    public static String cityFormatter(String city) {
        String formatted = city.replace("\"", "");
        formatted = formatted.replace("}", "");
        return formatted;
    }

    public static String startTimeFormatter(String unformattedStartTime) {
        String[] arr = unformattedStartTime.split(":");
        String formatted = "";
        if(Integer.parseInt(arr[0]) > 12) {
            formatted += String.valueOf(Integer.parseInt(arr[0]) - 12) + ":" + arr[1] + " pm";
        } else {
            formatted += arr[0] + ":" + arr[1] + " am";
        }
        return formatted;
    }

    public static String formatJsonDate(String rawJsonDate) {
        String formatted = "";
        String[] arr = rawJsonDate.split("-");
        formatted += arr[2] + " ";
        switch(arr[1]){
            case "01":
                formatted += "January";
                break;
            case "02":
                formatted += "February";
                break;
            case "03":
                formatted += "March";
                break;
            case "04":
                formatted += "April";
                break;
            case "05":
                formatted += "May";
                break;
            case "06":
                formatted += "June";
                break;
            case "07":
                formatted += "July";
                break;
            case "08":
                formatted += "August";
                break;
            case "09":
                formatted += "September";
                break;
            case "10":
                formatted += "October";
                break;
            case "11":
                formatted += "November";
                break;
            case "12":
                formatted += "December";
                break;
        }
        return formatted += " " + arr[0];
    }
}
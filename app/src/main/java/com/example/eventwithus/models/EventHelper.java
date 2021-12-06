package com.example.eventwithus.models;

import android.util.Log;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
    This classes purpose is just to have one place for static methods for assisting in events
 */
public class EventHelper {

    public static final String TAG = "EventHelper"; // tag for logging

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
                events = eventsString.split(",");
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

    public static void refreshUserData() {
        ParseUser.getCurrentUser().fetchInBackground((object, e) -> {
            if (e == null) {
                Log.d(TAG, "User refresh successful");
            } else {
                Log.e(TAG, "Error: " + e.getMessage());
            }
        });
    }

    // takes string json date in the form YYYY-MM-DD and turns it into something like 06 January 2022
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
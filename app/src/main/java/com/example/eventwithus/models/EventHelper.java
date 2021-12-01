package com.example.eventwithus.models;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
    This classes purpose is just to have one place for static methods for assisting in events
 */
public class EventHelper {

    // this method returns an ArrayList of Strings of the event information. Each string contains the event ID and event date formatted as eventID_eventDate
    public static List<String> getLoggedInUserEvents(String EVENTS_KEY) {
        List<String> eventsinfo = new ArrayList<>();
        ParseUser currentUser = ParseUser.getCurrentUser();

        String[] events =  currentUser.getString(EVENTS_KEY).split(",");
        System.out.println("EVENTS:");
        System.out.println(Arrays.toString(events));

        for(String s : events) {
            System.out.println(s);
            if(!s.equals("null"))
                eventsinfo.add(s);
        }

        return eventsinfo;
    }
}
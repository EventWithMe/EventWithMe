package com.example.eventwithus.models;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/*
    This classes purpose is just to have one place for static methods for assisting in events
 */
public class EventHelper {

    public static List<String> getLoggedInUserEvents(String EVENTS_KEY) {
        List<String> eventsinfo = new ArrayList<>();
        ParseUser currentUser = ParseUser.getCurrentUser();


        String[] events =  currentUser.getString(EVENTS_KEY).split(",");
        for(String s : events) {
            System.out.println(s);
        }

        return eventsinfo;
    }

}

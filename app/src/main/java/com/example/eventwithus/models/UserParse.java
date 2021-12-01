package com.example.eventwithus.models;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

// NOT SURE IF THIS IS NEEDED BUT DO NOT DELETE JUST IN CASE -BRANDON

@ParseClassName("User")
public class UserParse extends ParseObject {

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_EVENT_INFO = "eventinfo";
    public static final String KEY_USER = "user";

    public String getUsername() {
        return getString(KEY_USERNAME);
    }

    public void setUsername(String description) {
        put(KEY_USERNAME, description);
    }

    public String getEventIds() {
        return getString(KEY_EVENT_INFO);
    }

    public void setEventIds(String events) {
        put(KEY_EVENT_INFO, events);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }
}

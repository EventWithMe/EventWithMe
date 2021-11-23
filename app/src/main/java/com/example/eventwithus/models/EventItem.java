package com.example.eventwithus.models;


public class EventItem {
    private String mImageUrl;
    private String eventName;
    private String type;

    public EventItem(String imageUrl, String eventNames, String types) {
        mImageUrl = imageUrl;
        eventName = eventNames;
        type = types;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getCreator() {
        return eventName;
    }

    public String getLikeCount() {
        return type;
    }
}
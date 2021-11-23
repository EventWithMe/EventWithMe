package com.example.eventwithus.models;


public class EventItem {
    private String mImageUrl;
    private String eventName;
    private String date;

    public EventItem(String imageUrl, String eventNames, String Date) {
        mImageUrl = imageUrl;
        eventName = eventNames;
        date = Date;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getCreator() {
        return eventName;
    }

    public String getLikeCount() {
        return date;
    }
}
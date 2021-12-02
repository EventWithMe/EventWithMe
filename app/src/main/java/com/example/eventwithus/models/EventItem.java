package com.example.eventwithus.models;


public class EventItem {
    private String mImageUrl;
    private String eventName;
    private String date;
    private String id;


    // TODO: 12/1/2021 add the id tot he constructor after the json is set up
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

    public String getDate() {
        return date;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
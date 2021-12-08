package com.example.eventwithus.models;

import java.io.Serializable;

public class MyEvents implements Serializable {

    private String eventName;
    private String city;
    private String date;
    private String startTime;
    private String venueName;
    private String eventType;
    private String eventId;
    private String imageURL;
    private String eventLink;

    public MyEvents(String eventName, String city, String date, String startTime, String venueName, String eventType, String eventId, String imageURL, String eventLink) {
        this.eventName = eventName;
        this.city = city;
        this.date = date;
        this.startTime = startTime;
        this.venueName = venueName;
        this.eventType = eventType;
        this.eventId = eventId;
        this.imageURL = imageURL;
        this.eventLink = eventLink;
    }

    public String getEventLink() {
        return eventLink;
    }

    public void setEventLink(String ticketLink) {
        this.eventLink = ticketLink;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}

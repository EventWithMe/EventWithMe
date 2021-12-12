package com.example.eventwithus;

public class RsvpTag {
    private String eventId;
    private String date;
    private String eventDescription;
    private String eventName;
    private String venueName;
    private String imageUrl;
    private String startTime;
    private String venueCity;

    public RsvpTag(String eventId, String date, String eventDescription, String eventName, String venueName, String imageUrl, String startTime, String venueCity) {
        this.eventId = eventId;
        this.date = date;
        this.eventDescription = eventDescription;
        this.eventName = eventName;
        this.venueName = venueName;
        this.imageUrl = imageUrl;
        this.startTime = startTime;
        this.venueCity = venueCity;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getVenueCity() {
        return venueCity;
    }

    public void setVenueCity(String venueCity) {
        this.venueCity = venueCity;
    }
}
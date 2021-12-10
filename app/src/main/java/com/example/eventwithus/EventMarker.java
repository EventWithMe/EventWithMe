package com.example.eventwithus;

import java.io.Serializable;

public class EventMarker implements Serializable {
    private String EventName;
    private String longitude;
    private String latitude;
    private String VenueName;
    private String VenueURL;

    public String getVenueName() {
        return VenueName;
    }

    public void setVenueName(String venueName) {
        VenueName = venueName;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public EventMarker(String eventName,String venueName, String longitude, String latitude, String venueURL) {
        EventName = eventName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.VenueName = venueName;
        this.VenueURL = venueURL;
    }

    public String getVenueURL() {
        return VenueURL;
    }

    public void setVenueURL(String venueURL) {
        VenueURL = venueURL;
    }

    @Override
    public String toString() {
        return "EventMarker{" +
                "EventName='" + EventName + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}

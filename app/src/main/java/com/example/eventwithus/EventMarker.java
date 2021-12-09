package com.example.eventwithus;

import java.io.Serializable;

public class EventMarker implements Serializable {
    private String EventName;
    private String longitude;
    private String latitude;
    private String VenueName;

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

    public EventMarker(String eventName,String venueName, String longitude, String latitude) {
        EventName = eventName;
        this.longitude = longitude;
        this.latitude = latitude;
        this.VenueName = venueName;
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

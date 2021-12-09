package com.example.eventwithus;

public class EventMarker {
    private String EventName;
    private String longitude;
    private String latitude;

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

    public EventMarker(String eventName, String longitude, String latitude) {
        EventName = eventName;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}

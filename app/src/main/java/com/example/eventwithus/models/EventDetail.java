package com.example.eventwithus.models;

public class EventDetail {
    private String info;
    private String id;
    private String venueName;
    private String startTime;
    private String venueCity;

    public String getVenueCity() {
        return venueCity;
    }

    public void setVenueCity(String venueCity) {
        this.venueCity = venueCity;
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

    public EventDetail(String info, String id, String venueName, String startTime, String venueCity) {
        this.info = info;
        this.id = id;
        this.venueName = venueName;
        this.startTime = startTime;
        this.venueCity = venueCity;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

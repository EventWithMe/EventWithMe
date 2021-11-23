package com.example.eventwithus.models;

import java.time.LocalDate;

public class Event {

    private String eventName;
    private String city;
    private LocalDate date;
    private String startTime; // TODO: 11/19/2021 figure out if there is a better way to store time like some kind of time formatting
    private int occupancy;
    private String description;
    private String venueName;
    private String eventType;
    private String eventId;

    public Event(String eventName, String city, LocalDate date, String startTime, int occupancy, String description, String venueName, String eventType, String eventId) {
        this.eventName = eventName;
        this.city = city;
        this.date = date;
        this.startTime = startTime;
        this.occupancy = occupancy;
        this.description = description;
        this.venueName = venueName;
        this.eventType = eventType;
        this.eventId = eventId;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}

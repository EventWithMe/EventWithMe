package com.example.eventwithus.models;

public class EventDetail {
    private String info;

    public EventDetail(String Eventinfo) {
        info = Eventinfo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

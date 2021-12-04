package com.example.eventwithus.models;

public class EventDetail {
    private String info;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

package com.example.eventwithus;

import com.example.eventwithus.models.User;

import java.util.Date;

class UserMessage {
    String message;
    Userr sender;
    long createdAt;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Userr getSender() {
        return sender;
    }

    public void setSender(Userr sender) {
        this.sender = sender;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
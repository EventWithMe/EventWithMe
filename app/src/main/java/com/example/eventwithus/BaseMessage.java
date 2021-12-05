package com.example.eventwithus;

public class BaseMessage {

        String message;
        Userr sender;
        long createdAt;

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

        public String getMessage() {
                return message;
        }

        public void setMessage(String message) {
                this.message = message;
        }
}

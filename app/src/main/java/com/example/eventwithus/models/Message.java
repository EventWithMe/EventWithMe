package com.example.eventwithus.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.Date;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String USER_ID_KEY = "userId";
    public static final String BODY_KEY = "body";
    public static final String EVENT_ID_KEY = "eventId";
    public static final String TIMESTAMP_KEY = "timestamp";

    public String getUserId() {
        return getString(USER_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public String getEventId() {
        return getString(EVENT_ID_KEY);
    }

    public Date getTimestamp() {
        return getDate(TIMESTAMP_KEY);
    }

    public String getTimestampAsString() {
        Date timestamp = getDate(TIMESTAMP_KEY);
        if (timestamp == null)
            return null;

        LocalDateTime dateTime =  Instant.ofEpochMilli(timestamp.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return dateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
    }

    public void setUserId(String userId) {
        put(USER_ID_KEY, userId);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public void setEventId(String body) {
        put(EVENT_ID_KEY, body);
    }

    public void setTimestamp(Date timestamp) {
        put(TIMESTAMP_KEY, timestamp);
    }
}

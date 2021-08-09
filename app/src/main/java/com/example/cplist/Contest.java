package com.example.cplist;

public class Contest {
    private final String event;
    private final String startTime;
    private final String endTime;
    private final String duration;
    private final String url;
    private final String in24Hours;


    public Contest(String eventName, String eventStartTime, String eventEndTime, String eventDuration, String eventUrl, String eventIn24Hour) {
        event = eventName;
        startTime = eventStartTime;
        endTime = eventEndTime;
        duration = eventDuration;
        url = eventUrl;
        in24Hours = eventIn24Hour;
    }

    public String getEvent() {
        return event;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDuration() {
        return duration;
    }

    public String getUrl() {
        return url;
    }

    public String getIn24Hours() {
        return in24Hours;
    }
}

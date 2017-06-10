package com.example.federation.GetterSetterClasses;

/**
 * Created by HP-PC on 16-04-2017.
 */

public class Event {
    int eventId;
    String eventName,eventCode,eventVenue,eventStartingDate,eventEndingDate;
    public Event(int eventId,String eventCode,String eventName,String eventVenue,String eventStartingDate,String eventEndingDate){
        this.eventCode=eventCode;this.eventId=eventId;this.eventEndingDate=eventEndingDate;this.eventName=eventName;this.eventVenue=eventVenue;this.eventStartingDate=eventStartingDate;
    }
    public String getEventName(){
        return eventName;
    }
    public String getEventCode(){
        return eventCode;
    }
    public int getEventId(){
        return eventId;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public String getEventStartingDate() {
        return eventStartingDate;
    }

    public String getEventEndingDate() {
        return eventEndingDate;
    }
}

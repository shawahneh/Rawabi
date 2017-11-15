package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.ITriger;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alaam on 11/15/2017.
 */

public class WebApi implements CalendarWebApi {
    private static WebApi instance;
    private WebApi(){}

    public static WebApi getInstance(){
        if(instance == null)
            instance = new WebApi();
        return instance;
    }

    @Override
    public void getEventAtDate(Date date, ITriger<ArrayList<Event>> eventITriger) {
        ArrayList<Event> dummyEvents = new ArrayList<>();
        Event event  = new Event();
        event.setDate(date);
        event.setName("Event name");
        event.setDescription("Description ...");
        event.setImageUrl("http://edugate.aauj.edu/faces/javax.faces.resource/images/logo1.png?ln=demo");

        dummyEvents.add(event);
        dummyEvents.add(event);
        dummyEvents.add(event);
        dummyEvents.add(event);

        eventITriger.onTriger(dummyEvents);
    }
}

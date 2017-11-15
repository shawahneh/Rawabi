package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.ITriger;

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
    public void getEventAtDate(Date date, ITriger<Event> eventITriger) {

    }
}

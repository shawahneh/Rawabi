package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.ITriger;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alaam on 11/15/2017.
 */

public interface CalendarWebApi {
    void getEventAtDate(Date date, ITriger<ArrayList<Event>> eventITriger);
}

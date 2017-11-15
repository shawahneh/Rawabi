package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.ITriger;

import java.util.ArrayList;

/**
 * Created by alaam on 11/15/2017.
 */

public interface AnnouncmentWebApi {
    void getAnnouns(ITriger<ArrayList<Event>> eventITriger);
}

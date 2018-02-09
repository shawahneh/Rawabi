package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.Announcement;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.Beans.Job;
import com.techcamp.aauj.rawabi.Beans.MediaItem;
import com.techcamp.aauj.rawabi.Beans.Transportation;
import com.techcamp.aauj.rawabi.IResponeTriger;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alaam on 2/3/2018.
 */

public interface BasicApi {
    void getAnnouns(IResponeTriger<ArrayList<Announcement>> eventITriger);
    void getJobs(IResponeTriger<ArrayList<Job>> triger);
    void getTransportation(IResponeTriger<Transportation> triger);
    void getWeather(IResponeTriger<String> triger);
    void getMedia(IResponeTriger<ArrayList<MediaItem>> triger);

    void getEventAtDate(Date date, IResponeTriger<ArrayList<Event>> eventITriger);
    void getEvents(IResponeTriger<ArrayList<Event>> triger); // get all events
}

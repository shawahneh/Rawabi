package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.model.Announcement;
import com.techcamp.aauj.rawabi.model.Event;
import com.techcamp.aauj.rawabi.model.Job;
import com.techcamp.aauj.rawabi.model.MediaItem;
import com.techcamp.aauj.rawabi.model.Transportation;
import com.techcamp.aauj.rawabi.ICallBack;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ALa on 2/3/2018.
 */

/**
 * Basic Api used with Q center, Events, Jobs, Media and Transportation
 */
public interface BasicApi {
    void getAnnouns(ICallBack<ArrayList<Announcement>> eventITriger);
    void getJobs(ICallBack<ArrayList<Job>> triger);
    void getTransportation(ICallBack<Transportation> triger);
    void getWeather(ICallBack<String> triger);
    void getMedia(ICallBack<ArrayList<MediaItem>> triger);

    void getEventAtDate(Date date, ICallBack<ArrayList<Event>> eventITriger);
    void getEvents(ICallBack<ArrayList<Event>> triger); // get all events
}

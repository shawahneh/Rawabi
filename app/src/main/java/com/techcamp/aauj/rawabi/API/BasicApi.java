package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.Announcement;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.Beans.Job;
import com.techcamp.aauj.rawabi.Beans.MediaItem;
import com.techcamp.aauj.rawabi.Beans.Transportation;
import com.techcamp.aauj.rawabi.ICallBack;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alaam on 2/3/2018.
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

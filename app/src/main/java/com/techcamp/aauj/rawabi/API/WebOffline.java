package com.techcamp.aauj.rawabi.API;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.techcamp.aauj.rawabi.model.AlbumItem;
import com.techcamp.aauj.rawabi.model.Announcement;
import com.techcamp.aauj.rawabi.model.Event;
import com.techcamp.aauj.rawabi.model.Job;
import com.techcamp.aauj.rawabi.model.Journey;
import com.techcamp.aauj.rawabi.model.Ride;
import com.techcamp.aauj.rawabi.model.Transportation;

import java.util.List;

import static com.techcamp.aauj.rawabi.API.services.OfflineApi.*;

public class WebOffline {
    public static final String CODE_JOBS = "code_jobs";
    public static final String CODE_ALBUMS = "CODE_Albums";
    public static final String CODE_EVENTS = "CODE_events";
    public static final String CODE_TRANSPORTATION = "CODE_TRANSPORTATION";
    public static final String CODE_ANNOUNCEMENTS= "CODE_ANNOUNCEMENTS";
    public static final String CODE_JOURNEY_ID_EQUAL = "CODE_JOURNEY_ID_EQUAL";
    public static final String CODE_RIDE_ID_EQUAL = "CODE_RIDE_ID_EQUAL";

    public List<Job> getJobs(Context context) {
        return getModel(context,CODE_JOBS,new TypeToken<List<Job>>(){}.getType());
    }
    public List<AlbumItem> getAlbums(Context context) {
        return getModel(context,CODE_ALBUMS,new TypeToken<List<AlbumItem>>(){}.getType());
    }

    public List<Event> getEvents(Context context) {
        return getModel(context,CODE_EVENTS,new TypeToken<List<Event>>(){}.getType());
    }
    public Transportation getTransportation(Context context){
        return getModel(context,CODE_TRANSPORTATION,Transportation.class);
    }
    public List<Announcement> getAnnouncements(Context context){
        return getModel(context,CODE_ANNOUNCEMENTS,new TypeToken<List<Announcement>>(){}.getType());
    }
    public Journey getJourney(Context context,int jid){
        return getModel(context,CODE_JOURNEY_ID_EQUAL + jid,Journey.class);
    }
    public Ride getRide(Context context,int rid){
        return getModel(context,CODE_RIDE_ID_EQUAL  + rid,Ride.class);
    }
    public void saveJourney(Context context, Journey journey){
        try {
            saveModel(context,journey,CODE_JOURNEY_ID_EQUAL + journey.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void saveRide(Context context, Ride ride){
        try {
            saveModel(context,ride,CODE_RIDE_ID_EQUAL + ride.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

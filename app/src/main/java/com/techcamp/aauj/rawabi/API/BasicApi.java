package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.AlbumItem;
import com.techcamp.aauj.rawabi.model.Announcement;
import com.techcamp.aauj.rawabi.model.Event;
import com.techcamp.aauj.rawabi.model.Job;
import com.techcamp.aauj.rawabi.model.MediaItem;
import com.techcamp.aauj.rawabi.model.Transportation;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ALa on 2/3/2018.
 */

/**
 * Basic Api methods used with Q center, Events, Jobs, Media and Transportation
 */
public interface BasicApi {
    void getAnnouns(IListCallBack<Announcement> callBack); // announcements for Q Center
    void getJobs(IListCallBack<Job> callBack);
    void getTransportation(ICallBack<Transportation> callBack);
    void getWeather(ICallBack<String> callBack);
    void getMedia(IListCallBack<MediaItem> callBack);

    void getAlbums(IListCallBack<AlbumItem> callBack);
    void getGalleryForAlbum(IListCallBack<MediaItem> callBack);

    void getEventAtDate(Date date, IListCallBack<Event> callBack);
    void getEvents(IListCallBack<Event> callBack); // get all events
}

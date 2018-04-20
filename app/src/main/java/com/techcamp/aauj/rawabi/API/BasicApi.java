package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.API.services.RequestService;
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
    RequestService getAnnouns(IListCallBack<Announcement> callBack); // announcements for Q Center
    RequestService getJobs(IListCallBack<Job> callBack);
    RequestService getTransportation(ICallBack<Transportation> callBack);
    RequestService getWeather(ICallBack<String> callBack);
//    void getMedia(IListCallBack<MediaItem> callBack); // deprecated

    RequestService getAlbums(IListCallBack<AlbumItem> callBack);
    RequestService getGalleryForAlbum(int albumId,IListCallBack<MediaItem> callBack); // get all media for album

    RequestService getEventAtDate(Date date, IListCallBack<Event> callBack);
    RequestService getEvents(IListCallBack<Event> callBack); // get all events
}

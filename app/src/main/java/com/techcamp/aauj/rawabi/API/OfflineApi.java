package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.AlbumItem;
import com.techcamp.aauj.rawabi.model.Announcement;
import com.techcamp.aauj.rawabi.model.Event;
import com.techcamp.aauj.rawabi.model.Job;
import com.techcamp.aauj.rawabi.model.MediaItem;
import com.techcamp.aauj.rawabi.model.Transportation;

import org.json.JSONObject;

import java.util.Date;

public class OfflineApi implements BasicApi{
    public static void setData(String code, JSONObject data,ICallBack callBack){

    }

    @Override
    public void getAnnouns(IListCallBack<Announcement> callBack) {

    }

    @Override
    public void getJobs(IListCallBack<Job> callBack) {

    }

    @Override
    public void getTransportation(ICallBack<Transportation> callBack) {

    }

    @Override
    public void getWeather(ICallBack<String> callBack) {

    }

    @Override
    public void getMedia(IListCallBack<MediaItem> callBack) {

    }

    @Override
    public void getAlbums(IListCallBack<AlbumItem> callBack) {

    }

    @Override
    public void getGalleryForAlbum(IListCallBack<MediaItem> callBack) {

    }

    @Override
    public void getEventAtDate(Date date, IListCallBack<Event> callBack) {

    }

    @Override
    public void getEvents(IListCallBack<Event> callBack) {

    }
}

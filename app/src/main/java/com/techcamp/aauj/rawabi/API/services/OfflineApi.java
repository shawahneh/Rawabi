package com.techcamp.aauj.rawabi.API.services;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.database.cacheDB;
import com.techcamp.aauj.rawabi.model.AlbumItem;
import com.techcamp.aauj.rawabi.model.Announcement;
import com.techcamp.aauj.rawabi.model.CacheItem;
import com.techcamp.aauj.rawabi.model.Event;
import com.techcamp.aauj.rawabi.model.Job;
import com.techcamp.aauj.rawabi.model.MediaItem;
import com.techcamp.aauj.rawabi.model.Transportation;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class OfflineApi {
    public static final String CODE_JOBS = "code_jobs";
    public static final String CODE_ALBUMS = "CODE_Albums";
    public static final String CODE_EVENTS = "CODE_events";

    private static void setData(Context context, String code, String data){
        cacheDB.getInstance(context).saveBean(new CacheItem(code,data));
    }
    private static CacheItem getData(Context context, String code){
        return cacheDB.getInstance(context).getBeanById(code);
    }
    public  static <T> void saveModel(Context context, T model, String code) throws Exception{
        setData(context,code,new Gson().toJson(model,model.getClass())) ;
    }

    public static <T> T getModel(Context context, String code, Type type){
        CacheItem cacheItem = getData(context,code);
        if(cacheItem == null)
            return null;
        return new Gson().fromJson(cacheItem.getValue(),type);
    }

    public List<Job> getJobs(Context context) {
        return getModel(context,CODE_JOBS,new TypeToken<List<Job>>(){}.getType());
    }
    public List<AlbumItem> getAlbums(Context context) {
        return getModel(context,CODE_ALBUMS,new TypeToken<List<AlbumItem>>(){}.getType());
    }

    public List<Event> getEvents(Context context) {
        return getModel(context,CODE_EVENTS,new TypeToken<List<Event>>(){}.getType());
    }



}

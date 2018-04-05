package com.techcamp.aauj.rawabi.API;

import android.content.Context;

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

import java.util.Date;
import java.util.List;

public class OfflineApi {
    public static final String CODE_JOBS = "code_jobs";

    public static void setData(Context context, String code, JSONObject data){
        cacheDB.getInstance(context).saveBean(new CacheItem(code,data.toString()));
    }
    public static CacheItem getData(Context context, String code){
        return cacheDB.getInstance(context).getBeanById(code);
    }
    public List<Job> getJobs(Context context) throws Exception{
        CacheItem cacheItem = getData(context,CODE_JOBS);
        return JsonToModelParser.parseGetJobs(new JSONObject(cacheItem.getValue()));
    }


}

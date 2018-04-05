package com.techcamp.aauj.rawabi.API;

import android.content.Context;

import com.techcamp.aauj.rawabi.API.services.RequestService;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.Job;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebService {
    private static final String url="https://tcamp.000webhostapp.com/api/index.php";
    private Context mContext;
    private static WebService instance;

    public WebService(Context mContext) {
        this.mContext = mContext;
    }

    public static void init(Context context){
        if (instance == null)
            instance = new WebService(context);
    }
    public static WebService getInstance() {
        return instance;
    }

    public RequestService getJobs(IListCallBack<Job> callBack){
        return new RequestService<List<Job>>(mContext,url,callBack) {
            @Override
            public Map<String, String> getParameters() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("action","getJobs");
                return params;
            }

            @Override
            public List<Job> parseResponse(JSONObject response) {
                try {
                    return JsonToModelParser.parseGetJobs(response);
                }catch (Exception e){
                    return null;
                }
            }
        }
        .saveOffline(OfflineApi.CODE_JOBS)
        .start();

    }

}

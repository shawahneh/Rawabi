package com.techcamp.aauj.rawabi.API;

import android.content.Context;

import com.techcamp.aauj.rawabi.API.services.RequestService;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.Job;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebTest {
    private String url;
    private Context mContext;
    public RequestService<List<Job>> getJobs(IListCallBack<Job> callBack){
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
        };
    }

}

package com.techcamp.aauj.rawabi.API;

import android.content.Context;
import android.util.Log;

import com.techcamp.aauj.rawabi.API.services.RequestService;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.AlbumItem;
import com.techcamp.aauj.rawabi.model.Job;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
                    if(response.has("jobs")) {

                        JSONArray jsonArray = response.getJSONArray("jobs");
                        JSONObject jsonTemp;
                        Job jobTemp;
                        ArrayList<Job> jobsArray = new ArrayList<Job>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jobTemp = new Job();
                            jsonTemp = jsonArray.getJSONObject(i);
                            jobTemp.setId(jsonTemp.getInt("id"));
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                            Log.d("tag", "endDate: " + jsonTemp.getString("endDate"));
                            jobTemp.setDate(simpleDateFormat.parse(jsonTemp.getString("endDate")));
                            Log.d("tag", "endDate: " + simpleDateFormat.parse(jsonTemp.getString("endDate")));


                            jobTemp.setName(jsonTemp.getString("jobTitle"));
                            jobTemp.setDescription(jsonTemp.getString("description"));
                            jobTemp.setCompanyName(jsonTemp.getString("companyName"));

                            if (jsonTemp.has("imageUrl"))
                                jobTemp.setImageUrl(jsonTemp.getString("imageUrl"));

                            jobsArray.add(jobTemp);
                        }
                        return jobsArray;
                    }

                    return null;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
        };


    }

    public RequestService<List<AlbumItem>> getAlbums(IListCallBack<AlbumItem> callBack){
        // TODO: 4/10/2018 implement this
        return new RequestService<List<AlbumItem>>(mContext,url,callBack) {
            @Override
            public Map<String, String> getParameters() {

                /*   put parameters here   */

                return null;
            }

            @Override
            public List<AlbumItem> parseResponse(JSONObject Response) {

                /*        parse json response here         */


                return null;
            }
        };
    }

}

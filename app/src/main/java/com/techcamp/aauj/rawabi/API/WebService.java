package com.techcamp.aauj.rawabi.API;

import android.content.Context;
import android.util.Log;

import com.techcamp.aauj.rawabi.API.services.RequestService;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.AlbumItem;
import com.techcamp.aauj.rawabi.model.Event;
import com.techcamp.aauj.rawabi.model.Job;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
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
        return new RequestService<List<AlbumItem>>(mContext,url,callBack) {
            @Override
            public Map<String, String> getParameters() {

                /*   put parameters here   */
                Map<String,String> params = new HashMap<>();
                params.put("action","getAlbums");

                return params;
            }

            @Override
            public List<AlbumItem> parseResponse(JSONObject Response) {

                /*        parse json response here         */
                try {
                    if(Response.has("media")){

                        JSONArray jsonArray = Response.getJSONArray("media");
                        JSONObject jsonTemp;
                        AlbumItem albumTemp;
                        ArrayList<AlbumItem> albumArray = new ArrayList<AlbumItem>();

                        for (int i=0 ; i< jsonArray.length() ; i++){
                            albumTemp = new AlbumItem();
                            jsonTemp = jsonArray.getJSONObject(i);
                            albumTemp.setId(jsonTemp.getInt("id"));

                            albumTemp.setTitle(jsonTemp.getString("name"));
                            albumTemp.setDescription(jsonTemp.getString("description"));
                            albumTemp.setImgUrl(jsonTemp.getString("imageUrl"));
                            albumArray.add(albumTemp);
                        }

                        return albumArray;


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }


                return null;
            }
        };
    }
    public RequestService<List<Event>> getEvents(IListCallBack<Event> callBack){
        return new RequestService<List<Event>>(mContext,url,callBack) {
            @Override
            public Map<String, String> getParameters() {

                /*   put parameters here   */
                Map<String,String> params = new HashMap<String, String>();
                params.put("action","getEvents");

                return params;
            }

            @Override
            public List<Event> parseResponse(JSONObject response) {

                /*        parse json response here         */
                try {
                    if(response.has("events")){

                        JSONArray jsonArray = response.getJSONArray("events");
                        JSONObject jsonTemp;
                        Event eventTemp;
                        ArrayList<Event> eventsArray = new ArrayList<Event>();

                        for (int i=0 ; i< jsonArray.length() ; i++){
                            eventTemp = new Event();
                            jsonTemp = jsonArray.getJSONObject(i);
                            eventTemp.setId(jsonTemp.getInt("id"));
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            eventTemp.setDate(simpleDateFormat.parse(jsonTemp.getString("startDateTime")));
                            eventTemp.setImageUrl(jsonTemp.getString("imageUrl"));
                            eventTemp.setDescription(jsonTemp.getString("description"));
                            eventTemp.setName(jsonTemp.getString("title"));
                            eventsArray.add(eventTemp);

                        }
                        return eventsArray;


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }

                return null;
            }
        };
    }

}

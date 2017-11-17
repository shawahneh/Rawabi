package com.techcamp.aauj.rawabi.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.ITriger;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alaam on 11/15/2017.
 */

public class WebApi implements CalendarWebApi,AnnouncmentWebApi,AuthWebApi ,
PoolingJourney{
    public String apiUrl = "https://tcamp.000webhostapp.com/api/index.php";
    RequestQueue requestQueue;
    private static WebApi instance;
    private Context mContext;

    private WebApi(Context context) {
        mContext = context;
    }
    public static WebApi getInstance(Context context) {
        if (instance == null)
            instance = new WebApi(context);
        return instance;
    }

    private void send(final Map<String,String> params, final ITriger<JSONObject> result, final ITriger<VolleyError> errorResponse)
    {
        Log.d("tag","send");

        if (requestQueue==null)
        {

             requestQueue = Volley.newRequestQueue(mContext);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("tag","Response : "+ response);
                Gson gson = new Gson();
                try {
//                JSONObject jsonObject = gson.fromJson(response,JSONObject.class);
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("tag",jsonObject.toString());
                    result.onTriger(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tag","ERROR "+ error.toString());
                errorResponse.onTriger(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> paramss = new HashMap<String, String>();
//                paramss.put("action","userAuth");
//                paramss.put("username","driver1");
//                paramss.put("password","driver1");
//                return paramss;
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    @Override
    public void getEventAtDate(Date date, ITriger<ArrayList<Event>> eventITriger) {
        ArrayList<Event> dummyEvents = new ArrayList<>();
        Event event = new Event();
        event.setDate(date);
        event.setName("Event name");
        event.setDescription("Description ...");
        event.setImageUrl("http://edugate.aauj.edu/faces/javax.faces.resource/images/logo1.png?ln=demo");

        dummyEvents.add(event);
        dummyEvents.add(event);
        dummyEvents.add(event);
        dummyEvents.add(event);

        eventITriger.onTriger(dummyEvents);
    }

    @Override
    public void getAnnouns(ITriger<ArrayList<Event>> eventITriger) {
        ArrayList<Event> dummyEvents = new ArrayList<>();
        Event event = new Event();
        event.setDate(new Date());
        event.setName("Event name");
        event.setDescription("Description ...");
        event.setImageUrl("http://edugate.aauj.edu/faces/javax.faces.resource/images/logo1.png?ln=demo");

        dummyEvents.add(event);
        dummyEvents.add(event);
        dummyEvents.add(event);
        dummyEvents.add(event);

        eventITriger.onTriger(dummyEvents);
    }



    public Boolean isLogin(){

        if(mContext != null) {
            SharedPreferences sp = mContext.getSharedPreferences("db", Context.MODE_PRIVATE);
            return sp.getBoolean("login", false);
        }
        return false;
    }
    @Nullable
    public User getLocalUser(){
        if(mContext == null)
            return null;
        SharedPreferences sp = mContext.getSharedPreferences("db",Context.MODE_PRIVATE);

        String username = sp.getString("username",null);
        String password = sp.getString("password",null);
        int id = sp.getInt("id",-1);
        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setId(id);

        if(username == null)
            return null;
        return user;
    }

    @Override
    public void userRegister(User user, final ITriger<Boolean> booleanITriger) {
        Map<String,String> params = new HashMap<String, String>();

        //($username,$password,$fullname,$gender,$birthdate,$address,$userType,$image,$phone)
        params.put("action","userRegister");
        params.put("username",user.getUsername());
        params.put("password",user.getPassword());
        params.put("fullname",user.getFullname());
        params.put("gender",user.getGender()+"");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
        params.put("birthdate",simpleDateFormat.format(user.getBirthdate()));
        params.put("address",user.getAddress());
        params.put("userType","1");
        params.put("image",user.getImageurl());
        params.put("phone",user.getPhone());
        send(params, new ITriger<JSONObject>() {
            @Override
            public void onTriger(JSONObject value) {
                try {
                    if (value.getString("registration").equals("success")){
                        Log.i("tag", "register process is done");
                        booleanITriger.onTriger(true);
                    }else
                    {
                        Log.i("tag", "register process is failed");
                        booleanITriger.onTriger(false);
                    }
                } catch (JSONException e) {
                    Log.i("tag","Error on JSON getting item");
                    booleanITriger.onTriger(false);
                    e.printStackTrace();
                }
            }
        }, new ITriger<VolleyError>() {
            @Override
            public void onTriger(VolleyError value) {
                Log.i("tag", "Error while getting data from send() method ");
                value.printStackTrace();
            }
        });
    }

    @Override
    public void setUserDetails(User user, String OldPassword, ITriger<Boolean> booleanITriger) {

    }


    @Override
    public void checkAuth(String username, String password, final ITriger<Boolean> booleanITriger) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","userAuth");
        params.put("username",username);
        params.put("password",password);
        send(params, new ITriger<JSONObject>() {
            @Override
            public void onTriger(JSONObject value) {
                try {
                    if (value.getString("auth").equals("true")){

                        Log.i("tag", "user auth is ok");
                        booleanITriger.onTriger(true);
                    }else
                    {

                        Log.i("tag", "user auth is not ok");
                        booleanITriger.onTriger(false);
                    }
                } catch (JSONException e) {
                    Log.i("tag","Error on JSON getting item");
                    booleanITriger.onTriger(false);
                    e.printStackTrace();
                }
            }
        }, new ITriger<VolleyError>() {
            @Override
            public void onTriger(VolleyError value) {
                Log.d("tag", "Error while getting data from send() method ");
                value.printStackTrace();
            }
        });
    }


    @Override
    public void getJourneys(int userId, int limitStart, int limitNum, ITriger<ArrayList<Journey>> journeys) {

    }

    @Override
    public void getJourneyDetails(int id, ITriger<Journey> journey) {

    }

    @Override
    public void setNewJourney(Journey newJourney, ITriger<Integer> journeyId) {

    }


    @Override
    public void filterJourneys(LatLng startPoint, LatLng endPoint, Date goingDate,int sortBy, ITriger<ArrayList<Journey>> Journeys) {
        ArrayList<Journey> journeys = new ArrayList<>();
        Journey journey = new Journey();
        journey.setGoingDate(new Date());
        journey.setStartPoint(startPoint);
        journey.setEndPoint(endPoint);
        User user = new User();
        user.setFullname("ALA AMARNEH");
        user.setImageurl("https://scontent.fjrs2-1.fna.fbcdn.net/v/t1.0-9/23376279_1508595089223011_6837471793707392618_n.jpg?oh=2d620ecf5841f11c2a550b75a2fbb650&oe=5A990C1E");

        journey.setUser(user);

        journeys.add(journey );
        journeys.add(journey );
        journeys.add(journey );
        journeys.add(journey );
        journeys.add(journey );
        Journeys.onTriger(journeys);
    }


}

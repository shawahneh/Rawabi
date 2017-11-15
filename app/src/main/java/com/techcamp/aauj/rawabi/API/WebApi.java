package com.techcamp.aauj.rawabi.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.techcamp.aauj.rawabi.Beans.DriverUser;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.Beans.RiderUser;
import com.techcamp.aauj.rawabi.ITriger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by alaam on 11/15/2017.
 */

public class WebApi implements CalendarWebApi,AnnouncmentWebApi,AuthWebApi {
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

    @Override
    public void RiderRegister(RiderUser user, ITriger<Boolean> booleanITriger) {

    }

    @Override
    public void DriverRegister(DriverUser user, ITriger<Boolean> booleanITriger) {

    }

    @Override
    public void checkAuth(String username, String pass, ITriger<Boolean> booleanITriger) {

    }
    public Boolean isLogin(){
        if(mContext != null) {
            SharedPreferences sp = mContext.getSharedPreferences("db", Context.MODE_PRIVATE);
            return sp.getBoolean("login", false);
        }
        return false;
    }
}

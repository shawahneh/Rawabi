package com.techcamp.aauj.rawabi.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.ITriger;

import java.util.ArrayList;
import java.util.Date;

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
        String id = sp.getString("id",null);
        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setId(id);

        if(username == null)
            return null;
        return user;
    }

    @Override
    public void userRegister(User user, ITriger<Boolean> booleanITriger) {

    }

    @Override
    public void setUserDetail(User user, String OldPassword, ITriger<Boolean> booleanITriger) {

    }

    @Override
    public void checkAuth(String id, String pass, ITriger<Boolean> booleanITriger) {

    }
}

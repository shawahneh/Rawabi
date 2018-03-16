package com.techcamp.aauj.rawabi.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.Beans.User;

import java.util.Date;

/**
 * Created by ALa on 12/21/2017.
 */

public class SPController {
    public static User getLocalUser(Context context){
        SharedPreferences sp = context.getSharedPreferences("dbUser",Context.MODE_PRIVATE);

        String username = sp.getString("username",null);
        String password = sp.getString("password",null);
        String name = sp.getString("name",null);
        int id = sp.getInt("id",-1);
        String imageurl = sp.getString("imageurl",null);
        User user = new User();
        user.setFullname(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setId(id);
        user.setImageurl(imageurl);
        user.setPhone(sp.getString("phone",null));

        if(username == null)
            return null;
        return user;
    }
    public static void saveLocalUser(Context context, User user){
        SharedPreferences sp = context.getSharedPreferences("dbUser",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(user == null){
            editor.clear();
        }else{

        editor.putString("username",user.getUsername());
        editor.putString("password",user.getPassword());
        editor.putString("name",user.getFullname());
        editor.putInt("id",user.getId());
        editor.putString("imageurl",user.getImageurl());
        editor.putString("phone",user.getPhone());
        }
        editor.apply();
    }
    public static void savePendingJourney(Context context, Journey journey){
        SharedPreferences sp = context.getSharedPreferences("pendingJourney",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("jid",journey.getId());
        editor.putString("startPoint_lat",journey.getStartPoint().latitude + "");
        editor.putString("startPoint_lng",journey.getStartPoint().longitude + "");
        editor.putString("endPoint_lat",journey.getEndPoint().latitude + "");
        editor.putString("endPoint_lng",journey.getEndPoint().longitude + "");
        editor.putInt("seats",journey.getSeats());
        editor.putLong("goingDate",journey.getGoingDate().getTime());
        editor.putInt("genderPrefer",journey.getGenderPrefer());
        editor.putString("carDescription",journey.getCarDescription());
        editor.putInt("status",journey.getStatus());

        editor.apply();
    }
    public static Journey getPendingJourney(Context context){
        SharedPreferences sp = context.getSharedPreferences("pendingJourney",Context.MODE_PRIVATE);
        int id = sp.getInt("jid",-1);
        if(id == -1)
            return null;

        Journey journey = new Journey();

        double startPoint_lat =Double.parseDouble(sp.getString("startPoint_lat","0"));
        double startPoint_lng = Double.parseDouble(sp.getString("startPoint_lng","0"));
        double endPoint_lat =Double.parseDouble(sp.getString("endPoint_lat","0"));
        double endPoint_lng = Double.parseDouble(sp.getString("endPoint_lng","0"));

        LatLng startPoint = new LatLng(startPoint_lat,startPoint_lng);
        LatLng endPoint = new LatLng(endPoint_lat,endPoint_lng);
        int seats = sp.getInt("seats",0);
        Date goingDate = new Date(sp.getLong("goingDate",0));
        int genderPrefer = sp.getInt("genderPrefer",0);
        String carDescription = sp.getString("carDescription",null);
        int status = sp.getInt("status",0);

        journey.setId(id);
        journey.setStartPoint(startPoint);
        journey.setEndPoint(endPoint);
        journey.setSeats(seats);
        journey.setGoingDate(goingDate);
        journey.setGenderPrefer(genderPrefer);
        journey.setCarDescription(carDescription);
        journey.setStatus(status);

       return journey;
    }

    public static LatLng getLocationOfRawabi(){
        return new LatLng(32.008049, 35.187367);
    }

}

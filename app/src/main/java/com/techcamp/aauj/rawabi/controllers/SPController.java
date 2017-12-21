package com.techcamp.aauj.rawabi.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import com.techcamp.aauj.rawabi.Beans.User;

/**
 * Created by alaam on 12/21/2017.
 */

public class SPController {
    public static User getLocalUser(Context context){
        SharedPreferences sp = context.getSharedPreferences("db",Context.MODE_PRIVATE);

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

        if(username == null)
            return null;
        return user;
    }
    public static void saveLocalUser(Context context, User user){
        SharedPreferences sp = context.getSharedPreferences("db",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username",user.getUsername());
        editor.putString("password",user.getPassword());
        editor.putString("name",user.getFullname());
        editor.putInt("id",user.getId());
        editor.putString("imageurl",user.getImageurl());
        editor.apply();
    }
}

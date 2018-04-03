package com.techcamp.aauj.rawabi.Core;

import android.app.Application;

import com.techcamp.aauj.rawabi.API.WebFactory;

/**
 * Created by ALa on 3/13/2018.
 */

public class FireApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        //initialize Web API
        WebFactory.initAll(getApplicationContext());
    }
}

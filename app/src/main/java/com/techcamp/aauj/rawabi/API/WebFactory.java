package com.techcamp.aauj.rawabi.API;

import android.content.Context;

public class WebFactory {

    public static AuthWebApi getAuthService(){
        return WebService.getInstance();
    }
    public static BasicApi getBasicService(){
        return WebService.getInstance();
    }
    public static CarpoolApi getCarpoolService(){
        return WebService.getInstance();
    }
    public static WebOffline getOfflineService(){return new WebOffline();}

    public static void initAll(Context context){
        WebApi.init(context);
        WebService.init(context);
    }

}

package com.techcamp.aauj.rawabi.API;

import android.content.Context;

public class WebFactory {

    public static AuthWebApi getAuthService(){
        return WebApi.getInstance();
    }
    public static BasicApi getBasicService(){
        return WebApi.getInstance();
    }
    public static CarpoolApi getCarpoolService(){
        return WebApi.getInstance();
    }


    public static void initAll(Context context){
        WebApi.init(context);
        WebDummy.init(context);
        WebService.init(context);
    }

}

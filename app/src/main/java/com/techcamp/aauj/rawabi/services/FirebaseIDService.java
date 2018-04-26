package com.techcamp.aauj.rawabi.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;

public class FirebaseIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("tag", "Refreshed token: " + refreshedToken);
        if(WebApi.getInstance().isLogin())
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        WebFactory.getCarpoolService().sendUserTokenToServer(refreshedToken, new ICallBack<Boolean>() {
            @Override
            public void onResponse(Boolean success) {
                if(success)
                Log.d("tag","token changed successfully");
                else
                Log.e("tag","error in changing token");
            }

            @Override
            public void onError(String err) {
                Log.e("tag","error in changing token, " + err);

            }
        })
        .start();
    }
}

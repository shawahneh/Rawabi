package com.techcamp.aauj.rawabi.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.JourneyDetailActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.RideDetailActivity;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.model.Journey;
import com.techcamp.aauj.rawabi.model.Ride;
import com.techcamp.aauj.rawabi.services.MyFirebaseMessagingService;

public class RouterActivity extends Activity {
    private SweetAlertDialog pDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showProgress("Loading..");

        int type = getIntent().getIntExtra("type",-1);
        int id = getIntent().getIntExtra("id",-1);
        if(type == -1 || id == -1){
            endActivity();
            return;
        }

        switch (type){
            case MyFirebaseMessagingService.TYPE_RIDER_SEND_TO_DRIVER: {
                final Intent intent = new Intent(this, JourneyDetailActivity.class);

                Journey journey = WebFactory.getOfflineService().getJourney(this, id);
                if (journey == null) {
                    WebFactory.getCarpoolService().getJourneyDetails(id, new ICallBack<Journey>() {
                        @Override
                        public void onResponse(Journey j) {
                            if(j != null){

                            intent.putExtra(JourneyDetailActivity.ARG_JOURNEY, j);
                            startActivity(intent);
                            endActivity();
                            }else{
                                onError("Error");
                            }
                        }

                        @Override
                        public void onError(String err) {
                            endActivity();
                        }
                    }).start();
                } else {
                    intent.putExtra(JourneyDetailActivity.ARG_JOURNEY, journey);
                    pDialog.dismiss();
                    startActivity(intent);
                }
            }

                break;
            case MyFirebaseMessagingService.TYPE_DRIVER_ACCEPT_REJECT_RIDER: {
                final Intent intent = new Intent(this, RideDetailActivity.class);

                Ride ride = WebFactory.getOfflineService().getRide(this, id);
                if (ride == null) {
                    WebFactory.getCarpoolService().getRideDetails(id, new ICallBack<Ride>() {
                        @Override
                        public void onResponse(Ride j) {
                            if(j != null){
                            intent.putExtra(RideDetailActivity.ARG_RIDE, j);
                            startActivity(intent);
                            endActivity();
                            }else{
                                onError("Error");
                            }
                        }

                        @Override
                        public void onError(String err) {
                            endActivity();
                        }
                    }).start();
                } else {
                    intent.putExtra(RideDetailActivity.ARG_RIDE, ride);

                    startActivity(intent);
                    endActivity();
                }

            }
                break;
            case MyFirebaseMessagingService.TYPE_DRIVER_CANCELLED_JOURNEY: {
                final Intent intent = new Intent(this, RideDetailActivity.class);

                Ride ride = WebFactory.getOfflineService().getRide(this, id);
                if (ride == null) {
                    WebFactory.getCarpoolService().getRideDetails(id, new ICallBack<Ride>() {
                        @Override
                        public void onResponse(Ride j) {
                            if(j != null){
                            intent.putExtra(RideDetailActivity.ARG_RIDE, j);

                            startActivity(intent);
                            endActivity();
                            }else{
                                onError("Error");
                            }
                        }

                        @Override
                        public void onError(String err) {
                            endActivity();
                        }
                    }).start();
                } else {
                    intent.putExtra(RideDetailActivity.ARG_RIDE, ride);
                    startActivity(intent);
                    endActivity();
                }

            }
            break;

            case MyFirebaseMessagingService.TYPE_RIDER_CANCELLED_RIDE: {
                final Intent intent = new Intent(this, JourneyDetailActivity.class);

                Journey journey = WebFactory.getOfflineService().getJourney(this, id);
                if (journey == null) {
                    WebFactory.getCarpoolService().getJourneyDetails(id, new ICallBack<Journey>() {
                        @Override
                        public void onResponse(Journey j) {
                            if(j != null){
                            intent.putExtra(JourneyDetailActivity.ARG_JOURNEY, j);
                            startActivity(intent);
                            endActivity();
                            }else{
                                onError("Error");
                            }
                        }

                        @Override
                        public void onError(String err) {
                            endActivity();
                        }
                    }).start();
                } else {
                    intent.putExtra(JourneyDetailActivity.ARG_JOURNEY, journey);
                    startActivity(intent);
                    endActivity();
                }

            }
            break;

        }
    }

    private void endActivity() {
        pDialog.dismiss();
        finish();
    }
    private void showProgress(String text){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(text);
        pDialog.setCancelable(false);
        pDialog.show();
    }

}

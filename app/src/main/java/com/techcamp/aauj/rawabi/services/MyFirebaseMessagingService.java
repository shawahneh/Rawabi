package com.techcamp.aauj.rawabi.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.JourneyDetailActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.RideDetailActivity;
import com.techcamp.aauj.rawabi.model.Ride;
import com.techcamp.aauj.rawabi.utils.NotificationUtil;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    /**
     * DATA JSON TEMPLATE :
     * {
     *     type:1,
     *     name:"rider name",
     *     jid,journey id
     * }
     */
    private final int TYPE_RIDER_SEND_TO_DRIVER = 1;

    /**
     * DATA JSON TEMPLATE :
     * {
     *     type:2,
     *     status: ride status,
     *     rid,ride id
     * }
     */
    private final int TYPE_DRIVER_ACCEPT_REJECT_RIDER = 2;

    /**
     * DATA JSON TEMPLATE :
     * {
     *     type:3,
     *     name:"driver name",
     *     rid,ride id
     * }
     */
    private final int TYPE_DRIVER_CANCELLED_JOURNEY= 3;

    /**
     * DATA JSON TEMPLATE :
     * {
     *     type:4,
     *     name:"rider name",
     *     jid,journey id
     * }
     */
    private final int TYPE_RIDER_CANCELLED_RIDE= 4;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("tag","onMessageReceived " + remoteMessage.getMessageId());
        Log.d("tag","onMessageReceived " + remoteMessage.getData());


        try {

            if(remoteMessage.getData().size() > 0) {
                int type = Integer.parseInt(remoteMessage.getData().get("type"));
                Map<String,String> dataMap = remoteMessage.getData();
                switch (type){
                    case TYPE_RIDER_SEND_TO_DRIVER: {
                        String name = dataMap.get("name");
                        int jid = Integer.parseInt(dataMap.get("jid"));
                        Log.d("tag",(WebFactory.getOfflineService().getJourney(this, jid) == null) + " journey ");

                        Intent notifyIntent = new Intent(this, JourneyDetailActivity.class);
                        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        notifyIntent.putExtra(JourneyDetailActivity.ARG_JOURNEY,
                                WebFactory.getOfflineService().getJourney(this, jid));
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 99,
                                notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        NotificationUtil.carpoolNotify(this,
                                "Carpool",
                                name + " sent you a request",
                                pendingIntent
                        );
                    }
                        break;
                    case TYPE_DRIVER_ACCEPT_REJECT_RIDER:{

                        int rid = Integer.parseInt(dataMap.get("rid"));
                        int status = Integer.parseInt(dataMap.get("status"));

                        Intent notifyIntent = new Intent(this,RideDetailActivity.class);
                        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        notifyIntent.putExtra(RideDetailActivity.ARG_RIDE,
                                WebFactory.getOfflineService().getRide(this,rid));
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 99,
                                notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        if(status == Ride.STATUS_ACCEPTED)
                        NotificationUtil.carpoolNotify(this,
                                "Carpool",
                                "your ride has been accepted",
                                pendingIntent
                        );
                        else
                        NotificationUtil.carpoolNotify(this,
                                "Carpool",
                                "your ride has been rejected",
                                pendingIntent
                        );
                    }
                        break;
                    case TYPE_DRIVER_CANCELLED_JOURNEY:{

                        int rid = Integer.parseInt(dataMap.get("rid"));
                        String name = dataMap.get("name");

                        Intent notifyIntent = new Intent(this,RideDetailActivity.class);
                        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        notifyIntent.putExtra(RideDetailActivity.ARG_RIDE,
                                WebFactory.getOfflineService().getRide(this,rid));
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 99,
                                notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            NotificationUtil.carpoolNotify(this,
                                    "Carpool",
                                    "Oops, "+name+ " cancelled the journey",
                                    pendingIntent
                            );

                    }
                        break;
                    case TYPE_RIDER_CANCELLED_RIDE:
                    {
                        String name = dataMap.get("name");
                        int jid = Integer.parseInt(dataMap.get("jid"));

                        Intent notifyIntent = new Intent(this, JourneyDetailActivity.class);
                        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        notifyIntent.putExtra(JourneyDetailActivity.ARG_JOURNEY,
                                WebFactory.getOfflineService().getJourney(this, jid));
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 99,
                                notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        NotificationUtil.carpoolNotify(this,
                                "Carpool",
                                name + " cancelled his ride",
                                pendingIntent
                        );
                    }
                        break;

                }


            }


        }catch (Exception e){e.printStackTrace();}
    }
}

package com.techcamp.aauj.rawabi.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.JourneyDetailActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.MyJourneysActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.MyRiderActivity;

public class RideService extends Service {
    DatabaseReference mData;
    ChildEventListener listener;
    public RideService() {
    }

    @Override
    public void onCreate() {
        mData = FirebaseDatabase.getInstance().getReference().child("rides");
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("tag","ride | received " + dataSnapshot.getValue().toString());
                RideService.this.notify("Carpool","Ride status changed");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null){
            String mode = intent.getStringExtra("mode");
            int id = intent.getIntExtra("rid",-1);
            if(mode.equals("create")){ // when send request to driver

                Log.d("tag","rid: " + id);
                if(id != -1){
                    mData.child(""+id).addChildEventListener(listener);

                }
            }else if(mode.equals("cancel")){  // when cancel the  request to driver
                mData.child(""+ id).removeEventListener(listener);
            }



        }









        return super.onStartCommand(intent, flags, startId);
    }

    public void notify(String title, String text)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.car_notexture);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.car_notexture));
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text).setDefaults(Notification.DEFAULT_ALL) ;


        Intent resultIntent = new Intent(this, MyRiderActivity.class);
        resultIntent.putExtra(JourneyDetailActivity.ARG_JOURNEY,new Journey());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        //mBuilder.addAction(R.drawable.rate_it,"rate",resultPendingIntent);


        mBuilder.setAutoCancel(true);


        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        int notificationID = ((int)(Math.random()*1000));
        mNotificationManager.notify(notificationID, mBuilder.build());
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

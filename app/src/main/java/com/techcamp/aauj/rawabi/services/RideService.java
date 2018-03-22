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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.MyRidesListActivity;
import com.techcamp.aauj.rawabi.model.Journey;
import com.techcamp.aauj.rawabi.model.Ride;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.JourneyDetailActivity;

// unused
public class RideService extends Service {
    DatabaseReference mData;
    ValueEventListener listener;
    public RideService() {
    }

    @Override
    public void onCreate() {
        mData = FirebaseDatabase.getInstance().getReference().child("rides");
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null && dataSnapshot.getValue() != null){

                Log.d("tag","ride | received " + dataSnapshot.getValue().toString());
                if(!dataSnapshot.getValue().toString().equals(Ride.STATUS_PENDING+""))
                    RideService.this.notify("Carpool","Ride status changed");
                }
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
                    mData.child(""+id).child("OrderStatus").addValueEventListener(listener);

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


        Intent resultIntent = new Intent(this, MyRidesListActivity.class);
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
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

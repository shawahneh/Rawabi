package com.techcamp.aauj.rawabi.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techcamp.aauj.rawabi.model.Journey;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.JourneyDetailActivity;
import com.techcamp.aauj.rawabi.activities.unusedActivities.MyJourneysActivity;

// unused
public class MyService extends Service {
    ChildEventListener listener;
    DatabaseReference mData;
    public MyService() {
    }

    @Override
    public void onCreate() {
        mData = FirebaseDatabase.getInstance().getReference().child("journeys_riders");
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("tag","Journey | received " + dataSnapshot.getValue().toString());
                MyService.this.notify("Carpool","You have new Rider!");
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



        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.car_notexture);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.car_notexture));
        mBuilder.setContentTitle("Carpool");
        mBuilder.setContentText("You have a pending journey").setDefaults(Notification.DEFAULT_ALL) ;


        Intent resultIntent = new Intent(this, MyJourneysActivity.class);
        resultIntent.putExtra(JourneyDetailActivity.ARG_JOURNEY,new Journey());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);



        mBuilder.setAutoCancel(true);
        int notificationID = ((int)(Math.random()*1000));
//        startForeground(notificationID,mBuilder.getNotification());




    }
    public void removeJourneyListener(){
        SharedPreferences sp = getSharedPreferences("notify",MODE_PRIVATE);
        int jid = sp.getInt("jid",-1);
        mData.child(""+jid).removeEventListener(listener);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null)
            return START_STICKY;

        int id = intent.getIntExtra("jid",-1);

        Log.d("tag","jid: " + id);

        mData.child("" + id).addChildEventListener(listener);
        SharedPreferences sp = getSharedPreferences("notify",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("jid",id);
        editor.apply();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        removeJourneyListener();
        super.onDestroy();
    }

    public void notify(String title, String text)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.car_notexture);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.car_notexture));
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text).setDefaults(Notification.DEFAULT_ALL) ;


        Intent resultIntent = new Intent(this, MyJourneysActivity.class);
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

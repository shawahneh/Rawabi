package com.techcamp.aauj.rawabi.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.techcamp.aauj.rawabi.R;

/**
 * Created by ALa on 12/24/2017.
 */

public class NotificationUtil {

    public static void carpoolNotify(Context context,String title,String text,PendingIntent pendingIntent){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "notify_022");

        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher));
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text).setDefaults(Notification.DEFAULT_ALL) ;

        NotificationCompat.BigTextStyle inboxStyle = new NotificationCompat.BigTextStyle().bigText(text);

        mBuilder.setStyle(inboxStyle);

        mBuilder.setAutoCancel(true);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);




        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_022",
                    context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.setSound(null,null);
            mNotificationManager.createNotificationChannel(channel);
        }

        int notificationID = 2; /*   for testing  */

        if (mNotificationManager != null) {
            mNotificationManager.notify(notificationID, mBuilder.build());
            Log.d("tag","mNotificationManager.notify");
        }
    }
}

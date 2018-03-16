package com.techcamp.aauj.rawabi.controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.receivers.AlarmReceiver;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ALa on 12/28/2017.
 */
// unused
public class AlarmController {
    public static void addAlarm(Context context, Journey journey){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, journey.getId(), intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Date date  = journey.getGoingDate();
        int h = date.getHours();
        int m = date.getMinutes();
        int d = date.getDay();

//        calendar.set(Calendar.DAY_OF_MONTH,d);
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);

        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmIntent);
        }

        Log.d("tag","alarm set: h " + h +" m " + m + " d " + d);

    }
    public static void cancelAlarm(Context context, Journey journey){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, journey.getId(), intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntent);
        }

        Log.d("tag","alarm cancelled");

    }
}

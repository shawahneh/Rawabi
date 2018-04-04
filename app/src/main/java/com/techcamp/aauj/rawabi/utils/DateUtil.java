package com.techcamp.aauj.rawabi.utils;

import android.content.Context;
import android.provider.ContactsContract;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ALa on 11/17/2017.
 */

public class DateUtil {

    public static String formatDateToTime(long time){
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return dateFormat.format(date);
    }
    public static String getMonthName(Date date){
        Calendar cal= Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(cal.getTime());
        return month_name;
    }
//    public static String formatDate(long time){
//        Date date = new Date(time);
//        Date nowDate = new Date();
//        SimpleDateFormat dateFormat = null;
//        if(date.getYear() < nowDate.getYear()){
//            dateFormat = new SimpleDateFormat("EEE, d MMM yyyy, h:mm a", Locale.getDefault());
//        }else if(date.getMonth() == nowDate.getMonth()){
//            if(date.getDay() - nowDate.getDay() > 7 ){
//                dateFormat = new SimpleDateFormat("EEE, d MMM, h:mm a", Locale.getDefault());
//            }else if (date.getDay() != nowDate.getDay()){
//                dateFormat = new SimpleDateFormat("EEE", Locale.getDefault());
//            }else {
//                dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
//            }
//        }else {
//            dateFormat = new SimpleDateFormat("EEE, d MMM, h:mm a", Locale.getDefault());
//        }
//
//        return dateFormat.format(date);
//    }
    public static boolean isPassed(Date date){
        Date date1 = new Date();
        if(date.getTime() < date1.getTime())
            return true;
        return false;
    }

    public static String getRelativeDate(Context context, Date date){
        return DateUtils.getRelativeDateTimeString(context,date.getTime(),DateUtils.MINUTE_IN_MILLIS,DateUtils.WEEK_IN_MILLIS,0).toString();
    }
    public static String getRelativeTime(Date date){
        return DateUtils.getRelativeTimeSpanString(date.getTime()).toString();
    }
    public static String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }
}

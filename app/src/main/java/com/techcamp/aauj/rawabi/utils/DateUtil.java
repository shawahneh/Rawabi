package com.techcamp.aauj.rawabi.utils;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alaam on 11/17/2017.
 */

public class DateUtil {

    public static String formatDateToTime(long time){
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return dateFormat.format(date);
    }
    public static String formatDate(long time){
        Date date = new Date(time);
        Date nowDate = new Date();
        SimpleDateFormat dateFormat = null;
        if(date.getYear() < nowDate.getYear()){
            dateFormat = new SimpleDateFormat("EEE, d MMM yyyy, h:mm a", Locale.getDefault());
        }else if(date.getMonth() == nowDate.getMonth()){
            if(date.getDay() - nowDate.getDay() > 7 ){
                dateFormat = new SimpleDateFormat("EEE, d MMM, h:mm a", Locale.getDefault());
            }else if (date.getDay() != nowDate.getDay()){
                dateFormat = new SimpleDateFormat("EEE", Locale.getDefault());
            }else {
                dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            }
        }else {
            dateFormat = new SimpleDateFormat("EEE, d MMM, h:mm a", Locale.getDefault());
        }

        return dateFormat.format(date);
    }
    public static boolean isPassed(Date date){
        Date date1 = new Date();
        if(date.getTime() < date1.getTime())
            return true;
        return false;
    }
}

package com.techcamp.aauj.rawabi.test;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by maysaraodeh on 27/04/2018.
 */

public class Test  {
    public void test() throws ParseException {
        String time = "2018-04-27 05:12:12 GMT";
        SimpleDateFormat inputFormat = new SimpleDateFormat
                ("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        Date t = inputFormat.parse(time);
        Log.d("tag" , t+"" );
    }

    public String convertUTCtoLocalTime(String p_city, String p_UTCDateTime) throws Exception{

        String lv_dateFormateInLocalTimeZone="";//Will hold the final converted date
        Date lv_localDate = null;
        String lv_localTimeZone ="";
        SimpleDateFormat lv_formatter;
        SimpleDateFormat lv_parser;

//Temp for testing(mapping of cities and timezones will eventually be in a properties file
        if(p_city.equals("LON")){
            lv_localTimeZone="Europe/London";
        }else if(p_city.equals("NBI")){
            lv_localTimeZone="EAT";
        }else if(p_city.equals("BRS")){
            lv_localTimeZone="Europe/Brussels";
        }else if(p_city.equals("IL")){
            lv_localTimeZone="America/Montreal";
        }else if(p_city.equals("LAS")){
            lv_localTimeZone="PST";}

//create a new Date object using the UTC timezone
        lv_parser = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        lv_parser.setTimeZone(TimeZone.getTimeZone("UTC"));
        lv_localDate = lv_parser.parse(p_UTCDateTime);

//Set output format - // prints "2007/10/25  18:35:07 EDT(-0400)"
        lv_formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z'('Z')'");
        System.out.println("convertUTCtoLocalTime "+p_city+": "+ "The Date in the UTC time zone(UTC) " + lv_formatter.format(lv_localDate));

//Convert the UTC date to Local timezone
        lv_formatter.setTimeZone(TimeZone.getTimeZone(lv_localTimeZone));
        lv_dateFormateInLocalTimeZone = lv_formatter.format(lv_localDate);
        System.out.println("convertUTCtoLocalTime: "+p_city+": "+"The Date in the LocalTime Zone time zone " + lv_formatter.format(lv_localDate));

        return lv_dateFormateInLocalTimeZone;
    }

}

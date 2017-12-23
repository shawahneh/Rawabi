package com.techcamp.aauj.rawabi.utils;

import java.text.DecimalFormat;

/**
 * Created by alaam on 12/22/2017.
 */

public class NumberUtil {
    public static double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }
    public static String getDistanceString(double d){
        if(d >= 1000)
            return roundTwoDecimals(d/1000) + " km";
        else
            return roundTwoDecimals(d) + " m";
    }
}

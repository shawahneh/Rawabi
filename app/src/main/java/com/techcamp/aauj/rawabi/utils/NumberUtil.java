package com.techcamp.aauj.rawabi.utils;

import java.text.DecimalFormat;

/**
 * Created by alaam on 12/22/2017.
 */

public class NumberUtil {
    private static final String arabic = "\u06f0\u06f1\u06f2\u06f3\u06f4\u06f5\u06f6\u06f7\u06f8\u06f9";
    private static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    public static double roundTwoDecimals(double d)
    {
//        DecimalFormat twoDForm = new DecimalFormat("#.##");
//        return getDouble( twoDForm.format(getDouble(d+""))) ;
        return Math.round(d * 100.0) / 100.0;
    }
    public static double getDouble(String n){
        String e = arabicToDecimal(n);
        try{
            return Double.parseDouble(e);
        }catch (Exception ae){
            ae.printStackTrace();
            return 0;
        }
    }
    public static String getDistanceString(double d){
        if(d >= 1000)
            return roundTwoDecimals(d/1000) + " km";
        else
            return roundTwoDecimals(d) + " m";
    }
}

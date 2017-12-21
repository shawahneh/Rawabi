package com.techcamp.aauj.rawabi.utils;

/**
 * Created by alaam on 12/21/2017.
 */

public class StringUtil {
    public static String getRideStatus(int status){
        switch (status){
            case 0:
                return "Pending";
            case 1:
                return "Accepted";
            case 2:
                return "Cancelled";

        }
        return null;
    }
    public static String getJourneyStatus(int status){
        switch (status){
            case 0:
                return "Pending";
            case 1:
                return "Cancelled";
            case 2:
                return "Completed";

        }
        return null;
    }
}

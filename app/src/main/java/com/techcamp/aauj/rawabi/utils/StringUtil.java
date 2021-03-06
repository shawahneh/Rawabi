package com.techcamp.aauj.rawabi.utils;

import com.techcamp.aauj.rawabi.model.*;

/**
 * Created by ALa on 12/21/2017.
 */

public class StringUtil {
    public static String getRideStatus(int status){
        switch (status){
            case Ride.STATUS_PENDING:
                return "Pending";
            case Ride.STATUS_ACCEPTED:
                return "Accepted";
            case Ride.STATUS_CANCELLED:
                return "Cancelled";
            case Ride.STATUS_DRIVER_REJECTED:
                return "REJECTED";
            case Ride.STATUS_RIDER_CANCELLED:
                return "Rider Cancelled";
            case Ride.STATUS_ACCEPTED_TIME_LEFT:
                return "Accepted|time left";
            case Ride.STATUS_TIME_LEFT:
                return "Cancelled | Time Left";
            case Ride.STATUS_JOURNEY_CANCELLED:
                return "Journey Cancelled";


        }
        return null;
    }
    public static String getJourneyStatus(int status){
        switch (status){
            case Journey.STATUS_PENDING:
                return "Available to riders";
            case Journey.STATUS_CANCELLED:
                return "Cancelled";
            case Journey.STATUS_COMPLETED:
                return "Completed";
            case Journey.STATUS_DRIVER_CANCELLED:
                return "You canceled the journey";
            case Journey.STATUS_DRIVER_CLOSED:
                return "Closed";
        }
        return null;
    }
}

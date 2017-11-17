package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.ITriger;

import java.util.ArrayList;

/**
 * Created by User on 11/16/2017.
 */

public interface PoolingRides {
    void getRides(int userId,int limitStart,int limitNum,ITriger<ArrayList<Ride>> rides);
    void getRideDetails(int rideId,ITriger<Ride> ride);
    //void setRideOnJourney(int journeyId,int meetingLocationX,int meetingLocationY , ITriger<Boolean> result);

    void setRideOnJourney(Ride newRide, ITriger<Integer> rideId);
    void changeRideStatus(int rideId, int status, ITriger<Boolean> result);
}

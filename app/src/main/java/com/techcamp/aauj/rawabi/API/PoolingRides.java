package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.ITriger;

import java.util.ArrayList;

/**
 * Created by User on 11/16/2017.
 */

public interface PoolingRides {
    void getRides(int userId,int limitStart,int limitNum,IResponeTriger<ArrayList<Ride>> rides);
    void getRideDetails(int rideId,IResponeTriger<Ride> ride);
    //void setRideOnJourney(int journeyId,int meetingLocationX,int meetingLocationY , ITriger<Boolean> result);

    void setRideOnJourney(Ride newRide, IResponeTriger<Integer> rideId); // create ride request
    void changeRideStatus(int rideId, int status, IResponeTriger<Boolean> result); // accept ride request or reject
}

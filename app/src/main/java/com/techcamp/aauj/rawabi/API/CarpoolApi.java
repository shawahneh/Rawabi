package com.techcamp.aauj.rawabi.API;

import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.model.Journey;
import com.techcamp.aauj.rawabi.model.Ride;
import com.techcamp.aauj.rawabi.ICallBack;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ALa on 2/8/2018.
 */

public interface CarpoolApi {
    void getJourneys(int userId,int limitStart,int limitNum,ICallBack<ArrayList<Journey>> callBack);
    void getJourneyDetails(int id,ICallBack<Journey> journey);
    void setNewJourney(Journey newJourney,ICallBack<Integer> journeyId);
    void filterJourneys(LatLng startPoint, LatLng endPoint, Date goingDate, int sortBy, ICallBack<ArrayList<Journey>> callBack);
    //sortBy: 1-> Distance, 2-> Time
    void getNumberOfJourneys(ICallBack<Integer> callBack);
    void changeJourneyStatus(Journey journey,int status,ICallBack<Boolean> callBack);
//    void getCustomJourney(int jid, ICallBack<CustomJourney> triger);

    void getRides(int userId,int limitStart,int limitNum,ICallBack<ArrayList<Ride>> callBack);
    void getRideDetails(int rideId,ICallBack<Ride> ride);
    //void setRideOnJourney(int journeyId,int meetingLocationX,int meetingLocationY , ITrigger<Boolean> result);
    void getRidersOfJourney(Journey journey, ICallBack<ArrayList<Ride>> callBack); // hint: set journey for each ride
    void setRideOnJourney(Ride newRide, ICallBack<Integer> rideId); // create ride request
    void changeRideStatus(int rideId, int status, ICallBack<Boolean> result); // accept ride request or reject or the rider cancel the ride

    void getStatusOfRide(int rideId, ICallBack<Integer> callBack);


}

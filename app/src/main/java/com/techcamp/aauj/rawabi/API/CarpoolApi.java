package com.techcamp.aauj.rawabi.API;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.API.services.RequestService;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.Journey;
import com.techcamp.aauj.rawabi.model.Ride;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ALa on 2/8/2018.
 */

public interface CarpoolApi {
    RequestService getJourneys(int userId, int limitStart, int limitNum, IListCallBack<Journey> callBack);
    RequestService getJourneyDetails(int id,ICallBack<Journey> journey);
    RequestService setNewJourney(Journey newJourney,ICallBack<Integer> journeyId);
    RequestService filterJourneys(LatLng startPoint, LatLng endPoint, Date goingDate, int sortBy, IListCallBack<Journey> callBack);
    //sortBy: 1-> Distance, 2-> Time
    RequestService getNumberOfJourneys(ICallBack<Integer> callBack);
    RequestService changeJourneyStatus(Journey journey,int status,ICallBack<Boolean> callBack);
//    void getCustomJourney(int jid, ICallBack<CustomJourney> triger);

    RequestService getRides(int userId,int limitStart,int limitNum,IListCallBack<Ride> callBack);
    RequestService getRideDetails(int rideId,ICallBack<Ride> ride);
    //void setRideOnJourney(int journeyId,int meetingLocationX,int meetingLocationY , ITrigger<Boolean> result);
    RequestService getRidersOfJourney(Journey journey, IListCallBack<Ride> callBack); // hint: set journey for each ride
    RequestService setRideOnJourney(Ride newRide, ICallBack<Integer> rideId); // create ride request
    RequestService changeRideStatus(int rideId, int status, ICallBack<Boolean> result); // accept ride request or reject or the rider cancel the ride
    RequestService changeMyRideStatus(int rideId,int journeyID,  int status, ICallBack<Boolean> result);
    RequestService getStatusOfRide(int rideId, ICallBack<Integer> callBack);


    RequestService sendUserTokenToServer(String token, @Nullable ICallBack<Boolean> callBack);


}

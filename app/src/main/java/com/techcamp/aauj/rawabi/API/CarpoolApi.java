package com.techcamp.aauj.rawabi.API;

import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.Beans.CustomBeans.CustomJourney;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.IResponeTriger;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alaam on 2/8/2018.
 */

public interface CarpoolApi {
    void getJourneys(int userId,int limitStart,int limitNum,IResponeTriger<ArrayList<Journey>> journeys);
    void getJourneyDetails(int id,IResponeTriger<Journey> journey);
    void setNewJourney(Journey newJourney,IResponeTriger<Integer> journeyId);
    void filterJourneys(LatLng startPoint, LatLng endPoint, Date goingDate, int sortBy, IResponeTriger<ArrayList<Journey>> Journeys);
    //sortBy: 1-> Distance, 2-> Time
    void getNumberOfJourneys(IResponeTriger<Integer> triger);
    void changeJourneyStatusAndGetRiders(Journey journey,int status,IResponeTriger<CustomJourney> triger);
    void getCustomJourney(int jid, IResponeTriger<CustomJourney> triger);

    void getRides(int userId,int limitStart,int limitNum,IResponeTriger<ArrayList<Ride>> rides);
    void getRideDetails(int rideId,IResponeTriger<Ride> ride);
    //void setRideOnJourney(int journeyId,int meetingLocationX,int meetingLocationY , ITriger<Boolean> result);
    void getRidersOfJourney(int jID, IResponeTriger<ArrayList<Ride>> triger);
    void setRideOnJourney(Ride newRide, IResponeTriger<Integer> rideId); // create ride request
    void changeRideStatus(int rideId, int status, IResponeTriger<Boolean> result); // accept ride request or reject or the rider cancel the ride

    void getStatusOfRide(int rideId, IResponeTriger<Integer> triger);


}

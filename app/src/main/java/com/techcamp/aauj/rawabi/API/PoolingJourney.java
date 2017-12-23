package com.techcamp.aauj.rawabi.API;

import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.ITriger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 11/16/2017.
 */

public interface PoolingJourney {
    public void getJourneys(int userId,int limitStart,int limitNum,IResponeTriger<ArrayList<Journey>> journeys);
    public void getJourneyDetails(int id,IResponeTriger<Journey> journey);
    void setNewJourney(Journey newJourney,IResponeTriger<Integer> journeyId);
    void filterJourneys(LatLng startPoint, LatLng endPoint, Date goingDate, int sortBy, IResponeTriger<ArrayList<Journey>> Journeys);
        //sortBy: 1-> Distance, 2-> Time

    void changeJourneyStatusAndGetRiders(Journey journey,int status,IResponeTriger<ArrayList<Ride>> triger);
}

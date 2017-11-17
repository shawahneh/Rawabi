package com.techcamp.aauj.rawabi.API;

import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.ITriger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 11/16/2017.
 */

public interface PoolingJourney {
    public void getJourneys(int userId,int limitStart,int limitNum,ITriger<ArrayList<Journey>> journeys);
    public void getJourneyDetails(int id,ITriger<Journey> journey);
    void setNewJourney(Journey newJourney,ITriger<Integer> journeyId);
    void filterJourneys(LatLng startPoint, LatLng endPoint, Date goingDate, int sortBy, ITriger<ArrayList<Journey>> Journeys);
        //sortBy: 1-> Distance, 2-> Time

}
package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.ITriger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11/16/2017.
 */

public interface PoolingJourney {
    public void getMyJourneys(ITriger<ArrayList<Journey>> journeys);
    public void getJourneyDetails(int id,ITriger<Journey> journey);
    void setNewJourney(Journey newJourney,ITriger<Boolean> result);
    void filterJourneys(Ride ride, ITriger<ArrayList<Journey>> Journeys);

}

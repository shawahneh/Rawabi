package com.techcamp.aauj.rawabi.API;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.ITriger;
import com.techcamp.aauj.rawabi.utils.Dummy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by User on 11/15/2017.
 */

public class WebService implements PoolingJourney,PoolingRides{
    Context context;
    private static WebService instance;
    public WebService(Context context){
       this.context = context;
    }
    public static WebService getInstance(Context context) {
        if (instance == null)
            instance = new WebService(context);
        return instance;
    }

    @Override
    public void getRides(int userId, int limitStart, int limitNum, IResponeTriger<ArrayList<Ride>> rides) {
        Dummy.getRides(rides);
    }

    @Override
    public void getRideDetails(int rideId, IResponeTriger<Ride> ride) {

    }

    @Override
    public void getRidersOfJourney(int jID, final IResponeTriger<ArrayList<Ride>> triger) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    ArrayList<Ride> rides = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        Ride ride = new Ride();
                        ride.setMeetingLocation(new LatLng(32.01183468173907 + i, 35.18930286169053));
                        User user = new User();
                        user.setFullname("ALA AMARNEH");
                        user.setImageurl("https://scontent.fjrs2-1.fna.fbcdn.net/v/t1.0-9/23376279_1508595089223011_6837471793707392618_n.jpg?oh=2d620ecf5841f11c2a550b75a2fbb650&oe=5A990C1E");
                        user.setPhone("0592355");

                        ride.setUser(user);

                        ride.setOrderStatus(i);
                        ride.setId(i);

                        rides.add(ride);
                    }
                    triger.onResponse(rides);
                }catch (Exception e){}
            }
        }).start();

    }

    @Override
    public void setRideOnJourney(Ride newRide, IResponeTriger<Integer> rideId) {

    }

    @Override
    public void changeRideStatus(int rideId, int status, final IResponeTriger<Boolean> result) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                result.onResponse(true);
            }
        }, 1000);
    }

    @Override
    public void getStatusOfRide(int rideId, final IResponeTriger<Integer> triger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                triger.onResponse((int)(Math.random() * 3));
            }
        }, 1000);
    }

    @Override
    public void getJourneys(int userId, int limitStart, int limitNum, IResponeTriger<ArrayList<Journey>> journeys) {
        Dummy.filterJouneys(journeys);
    }

    @Override
    public void getJourneyDetails(int id, IResponeTriger<Journey> journey) {

    }

    @Override
    public void setNewJourney(Journey newJourney, final IResponeTriger<Integer> journeyId) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                journeyId.onResponse(1);
            }
        }, 1000);
    }

    @Override
    public void filterJourneys(LatLng startPoint, LatLng endPoint, Date goingDate, int sortBy, IResponeTriger<ArrayList<Journey>> Journeys) {
        Dummy.filterJouneys(Journeys);
    }

    @Override
    public void changeJourneyStatusAndGetRiders(Journey journey, int status, IResponeTriger<ArrayList<Ride>> triger) {
        getRidersOfJourney(0,triger);
    }
}

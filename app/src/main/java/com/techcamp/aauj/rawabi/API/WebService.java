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
import com.techcamp.aauj.rawabi.Beans.Announcement;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.Beans.Job;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.Beans.Transportation;
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

public class WebService implements PoolingJourney,PoolingRides,AuthWebApi,AnnouncmentWebApi,CalendarWebApi{
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
    public void setRideOnJourney(Ride newRide, final IResponeTriger<Integer> rideId) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                rideId.onResponse( 1);
            }
        }, 1000);
    }

    @Override
    public void changeRideStatus(int rideId, int status, final IResponeTriger<Boolean> result) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                result.onResponse( true);
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
    public void getNumberOfJourneys(final IResponeTriger<Integer> triger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               triger.onResponse(10);
            }
        }, 1000);
    }

    @Override
    public void changeJourneyStatusAndGetRiders(Journey journey, int status, IResponeTriger<ArrayList<Ride>> triger) {
        getRidersOfJourney(0,triger);
    }

    @Override
    public void userRegister(User user, IResponeTriger<Boolean> booleanITriger) {

    }

    @Override
    public void setUserDetails(User user, String OldPassword, final IResponeTriger<Boolean> booleanITriger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                booleanITriger.onResponse(true);
            }
        }, 1000);
    }

    @Override
    public void getUserDetails(int userId, IResponeTriger<User> resultUser) {

    }

    @Override
    public void login(String username, String password, final IResponeTriger<User> resultUser) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                user.setFullname("ALa Amarneh");
                user.setPassword("driver1");
                user.setUsername("driver1");
                user.setId(1);

                resultUser.onResponse(user);
            }
        }, 1000);
    }

    @Override
    public void checkAuth(String username, String password, IResponeTriger<Boolean> booleanITriger) {

    }

    @Override
    public void getAnnouns(final IResponeTriger<ArrayList<Announcement>> eventITriger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<Announcement> dummyEvents = new ArrayList<>();
                for(int i=0;i<3;i++){

                    Announcement announcement = new Announcement();
                    announcement.setDate(new Date());
                    announcement.setName("Event name " +i);
                    announcement.setDescription("Description ... " +i);

                    dummyEvents.add(announcement);
                }

                eventITriger.onResponse(dummyEvents);
            }
        },1000);
    }

    @Override
    public void getJobs(final IResponeTriger<ArrayList<Job>> triger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<Job> dummyEvents = new ArrayList<>();
                for(int i=0;i<3;i++){

                    Job announcement = new Job();
                    announcement.setDate(new Date());
                    announcement.setName("Event name " +i);
                    announcement.setDescription("Description ... " +i);

                    dummyEvents.add(announcement);
                }

                triger.onResponse(dummyEvents);
            }
        },1000);
    }

    @Override
    public void getTransportation(final IResponeTriger<Transportation> triger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> list1 = new ArrayList<>();
                for (int i=0;i<10;i++){
                list1.add(i+":30");

                }
                ArrayList<String> list2 = (ArrayList<String>)list1.clone();
                list2.add("12:12");
                Transportation t = new Transportation();
                t.setFromRamallah(list1);
                t.setFromRawabi(list2);

                triger.onResponse(t);
            }
        },1000);
    }

    @Override
    public void getWeather(final IResponeTriger<String> triger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                triger.onResponse("25 C Sunny");
            }
        }, 1000);
    }

    @Override
    public void getEventAtDate(Date date, final IResponeTriger<ArrayList<Event>> eventITriger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<Event> dummyEvents = new ArrayList<>();
                for(int i=0;i<3;i++){

                    Event event = new Event();
                    event.setDate(new Date());
                    event.setName("Event name " +i);
                    event.setDescription("Description ... " +i);

                    dummyEvents.add(event);
                }
                Event event = new Event();
                event.setDate(new Date());
                event.setName("Event name");
                event.setDescription("Description ...");
                event.setImageUrl("http://edugate.aauj.edu/faces/javax.faces.resource/images/logo1.png?ln=demo");

                dummyEvents.add(event);
                dummyEvents.add(event);

                eventITriger.onResponse(dummyEvents);
            }
        },1000);

    }
}

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
import com.techcamp.aauj.rawabi.Beans.CustomBeans.CustomJourney;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.Beans.Job;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.MediaItem;
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

public class WebService implements CarpoolApi,AuthWebApi, BasicApi{
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
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

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
                    ride.setId(i+1);

                    rides.add(ride);
                }
                triger.onResponse(rides);
            }
        }, 1000);
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
    public void getJourneyDetails(int id, IResponeTriger<Journey> triger) {
        Journey journey1 = new Journey();
        journey1.setId(id);
//        changeJourneyStatusAndGetRiders(journey1,Journey.STATUS_PENDING,triger);
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
    public void changeJourneyStatusAndGetRiders(Journey journey, final int status, final IResponeTriger<CustomJourney> triger) {
        getRidersOfJourney(0, new IResponeTriger<ArrayList<Ride>>() {
            @Override
            public void onResponse(ArrayList<Ride> item) {
                CustomJourney cj = new CustomJourney();
                cj.setStatus(status);
                cj.setRiders(item);
                triger.onResponse(cj);
            }

            @Override
            public void onError(String err) {

            }
        });
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

                    Announcement announcement1 = new Announcement();
                    announcement1.setDate(new Date());
                    announcement1.setName("");
                    announcement1.setDescription("Description ... " +i);
                    announcement1.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t1.0-9/26907083_2009955205948738_1687566795894215353_n.jpg?oh=80ba2e78704a59233b9f4a287220d8bc&oe=5B262BBB");
                    dummyEvents.add(announcement1);


                    Announcement announcement2 = new Announcement();
                    announcement2.setDate(new Date());
                    announcement2.setName("TOMMY HILFIGER, PAUL&SHARK");
                    announcement2.setDescription("Description ... " +i);
                    announcement2.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t31.0-8/26173178_1995119340765658_5030634414212496139_o.jpg?oh=b70574238d7dd0e166d7c07325371426&oe=5ADE543D");
                    dummyEvents.add(announcement2);

                    Announcement announcement3 = new Announcement();
                    announcement3.setDate(new Date());
                    announcement3.setName("دورة تأسيسية لتعليم الأطفال الشطرنج");
                    announcement3.setDescription("Description ... " +i);
                    announcement3.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t1.0-9/25354153_1987912081486384_6247984867950597829_n.jpg?oh=a947fb219a2c6c1575a1fb50f87a6d28&oe=5AE0722E");
                    dummyEvents.add(announcement3);

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
//                for(int i=0;i<3;i++){
//
//                    Job announcement = new Job();
//                    announcement.setDate(new Date());
//                    announcement.setName("Event name " +i);
//                    announcement.setDescription("Description ... " +i);
//
//                    dummyEvents.add(announcement);
//                }

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
                    event.setName("Tech Talk at COnnect - The Myth of the Visionary");
                    event.setDescription("Description ... " +i);
                    event.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t31.0-8/27500564_217423392159403_6308589131273449865_o.jpg?oh=57bccd73d39fd59e4feccd01d945db68&oe=5B251B42");
                    dummyEvents.add(event);
                }
                Event event = new Event();
                event.setDate(new Date());
                event.setName("Tech Talk at COnnect - The Myth of the Visionary");
                event.setDescription("Many believe that people are born to be successful entrepreneurs – that a “Eureka moment” gifts those people with a great idea and the ability to see the future and that the market will beat a path to their door.\n" +
                        "\n" +
                        "But that is a myth. Brant’s talk empowers startup founders to make the changes they want to see in the world. Using lean innovation principles — Empathy, Experiments and Evidence — anyone willing to hustle, persevere, act bold can “learn” their way to success.\n" +
                        "Whether you are just forming an idea or have already started your own business, this talk has something for entrepreneurs at any stage.\n" +
                        "\n" +
                        "Speaker Biography:\n" +
                        "Brant Cooper is the CEO of Moves the Needle and author of the New York Times best seller The Lean Entrepreneur. He also serves as an advisor to entrepreneurs, accelerators and corporate innovation teams. With over a decade of expertise helping companies bring innovative products to market, he blends design thinking and lean methodology to ignite entrepreneurial action within large organizations. He has been in leadership roles in notable startups such as: Tumbleweed, Timestamp, WildPackets, inCode, and more. His current venture, Moves The Needle, empowers organizations to be closer to customers, move faster, and act bolder.");
                event.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t31.0-8/27500564_217423392159403_6308589131273449865_o.jpg?oh=57bccd73d39fd59e4feccd01d945db68&oe=5B251B42");

                dummyEvents.add(event);
                dummyEvents.add(event);

                eventITriger.onResponse(dummyEvents);
            }
        },1000);

    }

    @Override
    public void getEvents(IResponeTriger<ArrayList<Event>> triger) {
        getEventAtDate(new Date(),triger);
    }

    @Override
    public void getMedia(final IResponeTriger<ArrayList<MediaItem>> triger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<MediaItem> dummyMedia = new ArrayList<>();
                for(int i=0;i<3;i++){

                    MediaItem item = new MediaItem();
                    item.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t31.0-8/27500564_217423392159403_6308589131273449865_o.jpg?oh=57bccd73d39fd59e4feccd01d945db68&oe=5B251B42");

                    dummyMedia.add(item);

                }

                triger.onResponse(dummyMedia);
            }
        },1000);
    }
}

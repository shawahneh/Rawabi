package com.techcamp.aauj.rawabi.API;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techcamp.aauj.rawabi.Beans.Announcement;
import com.techcamp.aauj.rawabi.Beans.CustomBeans.CustomJourney;
import com.techcamp.aauj.rawabi.Beans.CustomBeans.FireJourney;
import com.techcamp.aauj.rawabi.Beans.CustomBeans.FireRide;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.Beans.Job;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.MediaItem;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.Beans.Transportation;
import com.techcamp.aauj.rawabi.Beans.TransportationElement;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.ICallBack;
import com.techcamp.aauj.rawabi.controllers.SPController;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by User on 11/15/2017.
 */


// Dummy API
public class WebService implements CarpoolApi,AuthWebApi, BasicApi{
    private Context context;
    private static WebService instance;
    public WebService(Context context){
       this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static WebService getInstance(Context context) {
        if (instance == null )
            instance = new WebService(context);
        instance.setContext(context);

        return instance;
    }
    public static void clear(){
        instance = null;
    }

    @Override
    public void getRides(int userId, int limitStart, int limitNum,final ICallBack<ArrayList<Ride>> rides) {
        final ArrayList<Ride> rideArrayList = new ArrayList<>();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("rides");
        mRef.orderByChild("user/id").equalTo(SPController.getLocalUser(context).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterable = dataSnapshot.getChildren().iterator();
                while (iterable.hasNext()){
                    DataSnapshot dataSnapshot1 = iterable.next();
                    FireRide ride =  dataSnapshot1.getValue(FireRide.class);
                    ride.setId(Integer.parseInt( dataSnapshot1.getKey()) );
                    if(ride.getJourney().getGoingDate().before(new Date()))
                    {
                        if(ride.getOrderStatus()==Ride.STATUS_ACCEPTED) ride.setOrderStatus(Ride.STATUS_ACCEPTED_TIME_LEFT);
                        else if(ride.getOrderStatus() == Ride.STATUS_PENDING) ride.setOrderStatus(Ride.STATUS_TIME_LEFT);
                    }
                    rideArrayList.add(ride.toRide());
                }
                rides.onResponse(rideArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                rides.onError(databaseError.getMessage());
            }
        });
    }

    @Override
    public void getRideDetails(int rideId, ICallBack<Ride> ride) {

    }

    @Override
    public void getRidersOfJourney(Journey journey,final ICallBack<ArrayList<Ride>> triger) {
        final ArrayList<Ride> rides = new ArrayList<>();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("rides");
        mRef.orderByChild("journey/id").equalTo(journey.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterable = dataSnapshot.getChildren().iterator();
                while (iterable.hasNext()){
                    DataSnapshot dataSnapshot1 = iterable.next();
                    FireRide fireRide =  dataSnapshot1.getValue(FireRide.class);
                    fireRide.setId(Integer.parseInt( dataSnapshot1.getKey()) );
                    rides.add(fireRide.toRide());
                }
                triger.onResponse(rides);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                triger.onError(databaseError.getMessage());
            }
        });
    }


    @Override
    public void setRideOnJourney(Ride newRide, final ICallBack<Integer> rideId) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                rideId.onResponse( (int)(Math.random()*1000));
            }
        }, 1000);
    }

    @Override
    public void changeRideStatus(int rideId, int status, final ICallBack<Boolean> triger) {
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child("rides");
        mData.child("" + rideId).child("orderStatus").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    triger.onResponse(task.isSuccessful());
            }
        });
    }

    @Override
    public void getStatusOfRide(int rideId, final ICallBack<Integer> triger) {
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child("rides");
        mData.child("" + rideId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FireRide fireRide = dataSnapshot.getValue(FireRide.class);
                if(fireRide.getJourney().getGoingDate().before(new Date())){
                    {
                        if(fireRide.getOrderStatus()==Ride.STATUS_ACCEPTED) fireRide.setOrderStatus(Ride.STATUS_ACCEPTED_TIME_LEFT);
                        else if(fireRide.getOrderStatus() == Ride.STATUS_PENDING) fireRide.setOrderStatus(Ride.STATUS_TIME_LEFT);
                    }
                }
                triger.onResponse(fireRide.getOrderStatus());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                triger.onError(databaseError.getMessage());
            }
        });
    }

    @Override
    public void getJourneys(int userId, int limitStart, int limitNum,final ICallBack<ArrayList<Journey>> triger) {
        final ArrayList<Journey> journeys = new ArrayList<>();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("journeys");
        mRef.orderByChild("user/id").equalTo(SPController.getLocalUser(context).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterable = dataSnapshot.getChildren().iterator();
                while (iterable.hasNext()){
                    DataSnapshot dataSnapshot1 = iterable.next();
                    FireJourney journey =  dataSnapshot1.getValue(FireJourney.class);
                    journey.setId(Integer.parseInt( dataSnapshot1.getKey()) );
                    updateJourneyStatus(journey);
                    journeys.add(journey.toJourney());
                }
                triger.onResponse(journeys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                triger.onError(databaseError.getMessage());
            }
        });
    }

    private void updateJourneyStatus(FireJourney journey) {
        if(journey.getGoingDate().before(new Date()))
            if (journey.getStatus()==Journey.STATUS_PENDING || journey.getStatus()==Journey.STATUS_DRIVER_CLOSED)
                journey.setStatus(Journey.STATUS_COMPLETED);
    }

    @Override
    public void getJourneyDetails(int id, ICallBack<Journey> triger) {
        Journey journey1 = new Journey();
        journey1.setId(id);
//        changeJourneyStatusAndGetRiders(journey1,Journey.STATUS_PENDING,triger);
    }

    @Override
    public void setNewJourney(Journey newJourney, final ICallBack<Integer> journeyId) {
        final int key = (int)(Math.random()*1000);
        FirebaseDatabase.getInstance().getReference().child("journeys").child(key+"").setValue(newJourney)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            journeyId.onResponse(key);
                        }else
                            journeyId.onError("error");
                    }
                });
    }

    @Override
    public void filterJourneys(LatLng startPoint, LatLng endPoint, Date goingDate, int sortBy,final ICallBack<ArrayList<Journey>> triger) {
        final ArrayList<Journey> journeys = new ArrayList<>();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("journeys");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterable = dataSnapshot.getChildren().iterator();
                while (iterable.hasNext()){
                    DataSnapshot dataSnapshot1 = iterable.next();
                    FireJourney journey =  dataSnapshot1.getValue(FireJourney.class);
                    journey.setId(Integer.parseInt( dataSnapshot1.getKey()) );
                    if(journey.getGoingDate().after(new Date()))
                        if(journey.getStatus() == Journey.STATUS_PENDING)
                            journeys.add(journey.toJourney());
                }
                triger.onResponse(journeys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                triger.onError(databaseError.getMessage());
            }
        });
//
//        new android.os.Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                final ArrayList<Journey> journeys = new ArrayList<>();
//                Journey journey = new Journey();
//                journey.setGoingDate(new Date(System.currentTimeMillis() + (1000*60*60*48)));
//                journey.setStartPoint(new LatLng(32.01183468173907,35.18930286169053));
//                journey.setEndPoint(new LatLng(32.01183468173907,35.18930286169053));
//                User user = new User();
//                user.setFullname("ALA AMARNEH");
//                user.setImageurl("https://scontent.fjrs2-1.fna.fbcdn.net/v/t1.0-9/23376279_1508595089223011_6837471793707392618_n.jpg?oh=2d620ecf5841f11c2a550b75a2fbb650&oe=5A990C1E");
//                user.setPhone("0592355");
//                journey.setUser(user);
//                journey.setStatus(1);
//                journey.setId(1);
//                journeys.add(journey );
//
//                Journey j2 = new Journey();
//                j2.setGoingDate(new Date(System.currentTimeMillis() - (1000*30)));
//                j2.setStartPoint(new LatLng(32.01305201874965,35.19094504415989));
//                j2.setEndPoint(new LatLng(32.01183468173907,35.18930286169053));
//                User user2 = new User();
//                user2.setFullname("Moh AMARNEH");
//                user2.setImageurl("https://scontent.fjrs2-1.fna.fbcdn.net/v/t1.0-9/23376279_1508595089223011_6837471793707392618_n.jpg?oh=2d620ecf5841f11c2a550b75a2fbb650&oe=5A990C1E");
//
//                j2.setUser(user2);
//                j2.setStatus(0);
//                journeys.add(j2 );
//
//                Journey j3 = new Journey();
//                j3.setGoingDate(new Date());
//                j3.setStartPoint(new LatLng(32.01305201874965,35.19094504415989));
//                j3.setEndPoint(new LatLng(32.01183468173907,35.18930286169053));
//                User user3 = new User();
//                user3.setFullname("Moh sfdfdsf");
//                user3.setImageurl("https://scontent.fjrs2-1.fna.fbcdn.net/v/t1.0-9/23376279_1508595089223011_6837471793707392618_n.jpg?oh=2d620ecf5841f11c2a550b75a2fbb650&oe=5A990C1E");
//
//                j3.setUser(user3);
//                j3.setStatus(2);
//                journeys.add(j3 );
//
//
//
//                triger.onResponse(journeys);
//
//            }
//        }, 1000);

    }

    @Override
    public void getNumberOfJourneys(final ICallBack<Integer> triger) {
        FirebaseDatabase.getInstance().getReference().child("journeys")
                .orderByChild("status").equalTo(Journey.STATUS_PENDING).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long i = dataSnapshot.getChildrenCount();
                triger.onResponse((int)i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                triger.onError(databaseError.getMessage());
            }
        });
    }

    @Override
    public void changeJourneyStatus(Journey journey, int status, ICallBack<Boolean> triger) {

    }

//    @Override
//    public void changeJourneyStatusAndGetRiders(Journey journey, final int status, final ICallBack<CustomJourney> triger) {
//        FirebaseDatabase.getInstance().getReference().child("journeys").child(journey.getId()+"")
//                .child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    getRidersOfJourney(0, new ICallBack<ArrayList<Ride>>() {
//                        @Override
//                        public void onResponse(ArrayList<Ride> item) {
//                            CustomJourney cj = new CustomJourney();
//                            cj.setStatus(status);
//                            cj.setRiders(item);
//                            triger.onResponse(cj);
//                        }
//
//                        @Override
//                        public void onError(String err) {
//                            triger.onError(err);
//                        }
//                    });
//                }else
//                    triger.onError("err");
//            }
//        });


//    }

//    @Override
//    public void getCustomJourney(final int jid, final ICallBack<CustomJourney> triger) {
//        getRidersOfJourney(jid, new ICallBack<ArrayList<Ride>>() {
//            @Override
//            public void onResponse(final ArrayList<Ride> item) {
//                FirebaseDatabase.getInstance().getReference().child("journeys").child(jid+"").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        FireJourney fireJourney = dataSnapshot.getValue(FireJourney.class);
//                        updateJourneyStatus(fireJourney);
//                        CustomJourney customJourney = new CustomJourney();
//                        customJourney.setStatus(fireJourney.getStatus());
//                        customJourney.setRiders(item);
//                        triger.onResponse(customJourney);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        triger.onError(databaseError.getMessage());
//                    }
//                });
//
//            }
//
//            @Override
//            public void onError(String err) {
//                triger.onError(err);
//            }
//        });
//    }

    @Override
    public void userRegister(User user, ICallBack<Boolean> booleanITriger) {

    }

    @Override
    public void setUserDetails(User user, String OldPassword, final ICallBack<Boolean> booleanITriger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                booleanITriger.onResponse(true);
            }
        }, 1000);
    }

    @Override
    public void getUserDetails(int userId, ICallBack<User> resultUser) {

    }

    @Override
    public void login(String username, String password, final ICallBack<User> resultUser) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                user.setFullname("ALa Amarneh");
                user.setPassword("driver1");
                user.setUsername("alaamarneh");
                user.setImageurl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t1.0-9/12715321_231944050477975_3807749686171390197_n.jpg?oh=fb36d51e823d98c1581fa9525811b19b&oe=5B004C4C");
                user.setId(2);
                user.setPhone("0592345678");
                resultUser.onResponse(user);
            }
        }, 500);
    }

    @Override
    public void checkAuth(String username, String password, ICallBack<Boolean> booleanITriger) {

    }

    @Override
    public void getAnnouns(final ICallBack<ArrayList<Announcement>> eventITriger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<Announcement> dummyEvents = new ArrayList<>();
                for(int i=0;i<3;i++){

                    Announcement announcement1 = new Announcement();
                    announcement1.setId(1);
                    announcement1.setStartDate(new Date());
                    announcement1.setEndDate(new Date());
                    announcement1.setName("");
                    announcement1.setDescription("Description ... " +i);
                    announcement1.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t1.0-9/26907083_2009955205948738_1687566795894215353_n.jpg?oh=80ba2e78704a59233b9f4a287220d8bc&oe=5B262BBB");
                    dummyEvents.add(announcement1);


                    Announcement announcement2 = new Announcement();
                    announcement2.setId(2);
                    announcement2.setStartDate(new Date());
                    announcement2.setEndDate(new Date());
                    announcement2.setName("TOMMY HILFIGER, PAUL&SHARK");
                    announcement2.setDescription("Description ... " +i);
                    announcement2.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t31.0-8/26173178_1995119340765658_5030634414212496139_o.jpg?oh=b70574238d7dd0e166d7c07325371426&oe=5ADE543D");
                    dummyEvents.add(announcement2);

                    Announcement announcement3 = new Announcement();
                    announcement3.setId(3);
                    announcement3.setStartDate(new Date());
                    announcement3.setEndDate(new Date());
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
    public void getJobs(final ICallBack<ArrayList<Job>> triger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<Job> dummyEvents = new ArrayList<>();


                    Job job1 = new Job();
                    job1.setId(1);
                    job1.setDate(new Date());
                    job1.setName("Jobs at Connect" );
                    job1.setDescription("We are looking for a creative android..." );


                    Job job2 = new Job();
                    job2.setId(2);
                    job2.setDate(new Date());
                    job2.setName("New jobs at ASAL Technologies");
                    job2.setDescription("Are you a  Front-End Developer Or..." );

                Job job3 = new Job();
                job3.setId(3);
                job3.setDate(new Date(System.currentTimeMillis() - 1000*60*60*60));
                job3.setName("IOS Developer");
                job3.setDescription("We are looking for a IOS Developer..." );

                    dummyEvents.add(job1);
                    dummyEvents.add(job2);
                    dummyEvents.add(job3);


                triger.onResponse(dummyEvents);
            }
        },1000);
    }

    @Override
    public void getTransportation(final ICallBack<Transportation> triger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<TransportationElement> list1 = new ArrayList<>();
                for (int i=1;i<10;i++){
                    TransportationElement transportationElement = new TransportationElement();
                    transportationElement.setType(TransportationElement.TYPE_FROM_RAWABI);
                    transportationElement.setTime(i+":30 PM");
                    list1.add(transportationElement);

                }
                ArrayList<TransportationElement> list2 = new ArrayList<>();
                for (int i=1;i<10;i++){
                    TransportationElement transportationElement = new TransportationElement();
                    transportationElement.setType(TransportationElement.TYPE_FROM_RAMALLAH);
                    transportationElement.setTime(i+":30 PM");
                    list2.add(transportationElement);

                }
                Transportation t = new Transportation();
                t.setFromRamallah(list1);
                t.setFromRawabi(list2);

                triger.onResponse(t);
            }
        },1000);
    }

    @Override
    public void getWeather(final ICallBack<String> triger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                triger.onResponse("20 °C partly cloudy");
            }
        }, 1000);
    }

    @Override
    public void getEventAtDate(Date date, final ICallBack<ArrayList<Event>> eventITriger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<Event> dummyEvents = new ArrayList<>();
                for(int i=0;i<3;i++){

                    Event event = new Event();
                    event.setId(i+1);
                    event.setDate(new Date());
                    event.setName("Tech Talk at COnnect - The Myth of the Visionary");
                    event.setDescription("Description ... " +i);
                    event.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t31.0-8/27500564_217423392159403_6308589131273449865_o.jpg?oh=57bccd73d39fd59e4feccd01d945db68&oe=5B251B42");
                    dummyEvents.add(event);
                }
                Event event = new Event();
                event.setId(4);
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
    public void getEvents(ICallBack<ArrayList<Event>> triger) {
        getEventAtDate(new Date(),triger);
    }

    @Override
    public void getMedia(final ICallBack<ArrayList<MediaItem>> triger) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<MediaItem> dummyMedia = new ArrayList<>();


                    MediaItem item = new MediaItem();
                    MediaItem item1 = new MediaItem();
                    MediaItem item2 = new MediaItem();
                    MediaItem item3 = new MediaItem();
                    MediaItem item4 = new MediaItem();
                    MediaItem item5 = new MediaItem();
                    MediaItem item6 = new MediaItem();
                    item1.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t1.0-9/27540686_10156204702514581_4143823496512946324_n.jpg?oh=b2bef4129d3b652966e7981228388759&oe=5B073AC0");
                    item1.setId(1);
                    item.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t1.0-9/27751855_10156204702519581_6803973897487047225_n.jpg?oh=9a1a93a0d06812b137ea55976440451c&oe=5B1FC28F");
                    item.setId(2);
                    item2.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t1.0-9/22555052_10155877170344581_6428570290616065193_n.jpg?oh=7c422d87b1a6ade919e5a6859900ff5d&oe=5B153C47");
                    item2.setId(3);
                    item3.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t1.0-9/26166338_10156104147334581_7996581899405303106_n.jpg?oh=058337a1a6a279cf88bc709c3f4026da&oe=5B053454");
                    item3.setId(4);
                    item4.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t1.0-9/25659490_10156077335224581_1978614859813627029_n.jpg?oh=680261219cee881fe065d00edf4b3cf6&oe=5B4B386D");
                    item4.setId(5);
                    item5.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t1.0-9/22528554_10155865305229581_5444081704540563130_n.jpg?oh=d8bc2c9d8a05e8d939439e838881f2c8&oe=5B0D5C55");
                    item5.setId(6);
                    item6.setImageUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t1.0-9/21768295_10155805348664581_885499770214749177_n.jpg?oh=4c856c90a90388a256db73c1bcebb4ba&oe=5B1B2B54");
                    item6.setId(7);
                    dummyMedia.add(item);
                dummyMedia.add(item1);
                dummyMedia.add(item2);
                dummyMedia.add(item3);
                dummyMedia.add(item4);
                dummyMedia.add(item5);
                dummyMedia.add(item6);
                dummyMedia.add(item2);
                dummyMedia.add(item3);
                dummyMedia.add(item4);
                triger.onResponse(dummyMedia);
            }
        },1000);
    }
}

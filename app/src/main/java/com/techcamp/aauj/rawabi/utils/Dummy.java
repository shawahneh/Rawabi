package com.techcamp.aauj.rawabi.utils;

import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.ITriger;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alaam on 12/21/2017.
 */

public class Dummy {
    public static void getRides(final IResponeTriger<ArrayList<Ride>> rides){
        final ArrayList<Ride> rideArrayList = new ArrayList<>();
        filterJouneys(new IResponeTriger<ArrayList<Journey>>() {
            @Override
            public void onResponse(ArrayList<Journey> item) {
                Ride ride = new Ride();
                int i =0;
                for (Journey j :
                        item) {
                    Ride r = new Ride();
                    r.setId(i++);

                    r.setOrderStatus(1);
                    r.setJourney(j);
                    r.setMeetingLocation(j.getStartPoint());
                }
                rides.onResponse(rideArrayList);
            }

            @Override
            public void onError(String err) {

            }
        });
    }
    public static void filterJouneys(final IResponeTriger<ArrayList<Journey>> triger){
        final ArrayList<Journey> journeys = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);

                    Journey journey = new Journey();
                    journey.setGoingDate(new Date());
                    journey.setStartPoint(new LatLng(32.01183468173907,35.18930286169053));
                    journey.setEndPoint(new LatLng(32.01183468173907,35.18930286169053));
                    User user = new User();
                    user.setFullname("ALA AMARNEH");
                    user.setImageurl("https://scontent.fjrs2-1.fna.fbcdn.net/v/t1.0-9/23376279_1508595089223011_6837471793707392618_n.jpg?oh=2d620ecf5841f11c2a550b75a2fbb650&oe=5A990C1E");
                    user.setPhone("0592355");
                    journey.setUser(user);

                    journeys.add(journey );

                    Journey j2 = new Journey();
                    j2.setGoingDate(new Date());
                    j2.setStartPoint(new LatLng(32.01305201874965,35.19094504415989));
                    j2.setEndPoint(new LatLng(32.01183468173907,35.18930286169053));
                    User user2 = new User();
                    user2.setFullname("Moh AMARNEH");
                    user2.setImageurl("https://scontent.fjrs2-1.fna.fbcdn.net/v/t1.0-9/23376279_1508595089223011_6837471793707392618_n.jpg?oh=2d620ecf5841f11c2a550b75a2fbb650&oe=5A990C1E");

                    j2.setUser(user2);
                    journeys.add(j2 );

                    Journey j3 = new Journey();
                    j3.setGoingDate(new Date());
                    j3.setStartPoint(new LatLng(32.01305201874965,35.19094504415989));
                    j3.setEndPoint(new LatLng(32.01183468173907,35.18930286169053));
                    User user3 = new User();
                    user3.setFullname("Moh sfdfdsf");
                    user3.setImageurl("https://scontent.fjrs2-1.fna.fbcdn.net/v/t1.0-9/23376279_1508595089223011_6837471793707392618_n.jpg?oh=2d620ecf5841f11c2a550b75a2fbb650&oe=5A990C1E");

                    j3.setUser(user3);
                    journeys.add(j3 );



                    triger.onResponse(journeys);


                }catch (Exception e){e.printStackTrace();}
            }
        }).start();
    }
}

package com.techcamp.aauj.rawabi.Beans;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by User on 11/16/2017.
 */

public class Ride {
    private int id;
    private User user;
    private Journey journey;
    private LatLng meetingLocation;
    private int orderStatus;//Pending : 0 , Accepted : 1 , rejected : 2

    public Ride(){

    }


    public Ride(int id, User user, Journey journey, LatLng meetingLocation, int orderStatus) {
        this.id = id;
        this.user = user;
        this.journey = journey;
        this.meetingLocation = meetingLocation;
        this.orderStatus = orderStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public int getOrderStatus() {
        return orderStatus;
    }



    public LatLng getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(LatLng meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}

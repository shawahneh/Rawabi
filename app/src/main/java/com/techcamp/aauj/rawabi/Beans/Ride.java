package com.techcamp.aauj.rawabi.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by User on 11/16/2017.
 */

public class Ride implements Parcelable{
    public final static int STATUS_PENDING = 0;
    public final static int STATUS_CANCELLED = 2;
    public final static int STATUS_ACCEPTED = 1;
    public final static int STATUS_RIDER_CANCELLED = 3;
    public final static int STATUS_DRIVER_REJECTED = 4;
    private int id;
    private User user;
    private Journey journey;
    private LatLng meetingLocation;
    private int orderStatus;// see above

    public Ride(){

    }


    public Ride(int id, User user, Journey journey, LatLng meetingLocation, int orderStatus) {
        this.id = id;
        this.user = user;
        this.journey = journey;
        this.meetingLocation = meetingLocation;
        this.orderStatus = orderStatus;
    }


    protected Ride(Parcel in) {
        id = in.readInt();
        user = in.readParcelable(User.class.getClassLoader());
        journey = in.readParcelable(Journey.class.getClassLoader());
        meetingLocation = in.readParcelable(LatLng.class.getClassLoader());
        orderStatus = in.readInt();
    }

    public static final Creator<Ride> CREATOR = new Creator<Ride>() {
        @Override
        public Ride createFromParcel(Parcel in) {
            return new Ride(in);
        }

        @Override
        public Ride[] newArray(int size) {
            return new Ride[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeParcelable(user, i);
        parcel.writeParcelable(journey, i);
        parcel.writeParcelable(meetingLocation, i);
        parcel.writeInt(orderStatus);
    }
}

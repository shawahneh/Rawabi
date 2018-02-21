package com.techcamp.aauj.rawabi.Beans.CustomBeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.*;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.Beans.User;

/**
 * Created by alaam on 2/18/2018.
 */

public class FireRide  implements Parcelable {
    public final static int STATUS_PENDING = 0;
    public final static int STATUS_CANCELLED = 2;
    public final static int STATUS_ACCEPTED = 1;
    public final static int STATUS_ACCEPTED_TIME_LEFT = 7; // accepted and it can't be cancelled ( journey started )
    public final static int STATUS_RIDER_CANCELLED = 3;
    public final static int STATUS_DRIVER_REJECTED = 4;
    public final static int STATUS_JOURNEY_CANCELLED = 5;
    public final static int STATUS_TIME_LEFT = 6;

    private int id;
    private User user;
    private FireJourney journey;
    private LatLng meetingLocation;
    private int orderStatus;// see above

    public FireRide(){
    }
    public Ride toRide(){
        Ride ride = new Ride();
        ride.setId(getId());
        ride.setMeetingLocation(new com.google.android.gms.maps.model.LatLng(getMeetingLocation().getLatitude(),getMeetingLocation().getLongitude()));
        ride.setOrderStatus(getOrderStatus());
        ride.setUser(getUser());
        ride.setJourney(getJourney().toJourney());

        return ride;
    }


    protected FireRide(Parcel in) {
        id = in.readInt();
        user = in.readParcelable(User.class.getClassLoader());
        journey = in.readParcelable(Journey.class.getClassLoader());
        meetingLocation = in.readParcelable(com.google.android.gms.maps.model.LatLng.class.getClassLoader());
        orderStatus = in.readInt();
    }

    public static final Creator<FireRide> CREATOR = new Creator<FireRide>() {
        @Override
        public FireRide createFromParcel(Parcel in) {
            return new FireRide(in);
        }

        @Override
        public FireRide[] newArray(int size) {
            return new FireRide[size];
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


    public FireJourney getJourney() {
        return journey;
    }

    public void setJourney(FireJourney journey) {
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

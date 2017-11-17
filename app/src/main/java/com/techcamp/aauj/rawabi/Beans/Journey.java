package com.techcamp.aauj.rawabi.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 11/16/2017.
 */

public class Journey implements Parcelable{
    private int id;
    private User user;
    private LatLng startPoint;
    private LatLng endPoint;
    private int seats;

    protected Journey(Parcel in) {
        id = in.readInt();
        user = in.readParcelable(User.class.getClassLoader());
        startPoint = in.readParcelable(LatLng.class.getClassLoader());
        endPoint = in.readParcelable(LatLng.class.getClassLoader());
        seats = in.readInt();
        genderPrefer = in.readInt();
        carDescription = in.readString();
    }

    public static final Creator<Journey> CREATOR = new Creator<Journey>() {
        @Override
        public Journey createFromParcel(Parcel in) {
            return new Journey(in);
        }

        @Override
        public Journey[] newArray(int size) {
            return new Journey[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public LatLng getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(LatLng startPoint) {
        this.startPoint = startPoint;
    }

    public LatLng getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(LatLng endPoint) {
        this.endPoint = endPoint;
    }

    private Date goingDate;
    private int genderPrefer;//0 : male , 1 : female
    private String carDescription;




    public void setUser(User user) {
        this.user = user;
    }

    public Journey()
    {


    }


    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }




    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Date getGoingDate() {
        return goingDate;
    }

    public void setGoingDate(Date goingDate) {
        this.goingDate = goingDate;
    }

    public int getGenderPrefer() {
        return genderPrefer;
    }

    public void setGenderPrefer(int genderPrefer) {
        this.genderPrefer = genderPrefer;
    }

    public String getCarDescription() {
        return carDescription;
    }

    public void setCarDescription(String carDescription) {
        this.carDescription = carDescription;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeParcelable(user, i);
        parcel.writeParcelable(startPoint, i);
        parcel.writeParcelable(endPoint, i);
        parcel.writeInt(seats);
        parcel.writeInt(genderPrefer);
        parcel.writeString(carDescription);
    }
}

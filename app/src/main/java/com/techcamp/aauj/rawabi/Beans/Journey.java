package com.techcamp.aauj.rawabi.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 11/16/2017.
 */

public class Journey implements Parcelable{
    private int id;
    private User user;
    private double startLocationX;
    private double startLocationY;
    private double endLocationX;
    private double endlocationY;
    private int seats;
    private Date goingDate;
    private int genderPrefer;//0 : male , 1 : female
    private String carDescription;

    protected Journey(Parcel in) {
        id = in.readInt();
        startLocationX = in.readInt();
        startLocationY = in.readInt();
        endLocationX = in.readInt();
        endlocationY = in.readInt();
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


    public void setUser(User user) {
        this.user = user;
    }

    public Journey()
    {


    }
    public Journey(int id,User user, int startLocationX, int startLocationY, int endLocationX, int endlocationY, int seats, Date goingDate, int genderPrefer, String carDescription) {

        this.id = id;
        this.user = user;
        this.startLocationX = startLocationX;
        this.startLocationY = startLocationY;
        this.endLocationX = endLocationX;
        this.endlocationY = endlocationY;
        this.seats = seats;
        this.goingDate = goingDate;
        this.genderPrefer = genderPrefer;
        this.carDescription = carDescription;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }


    public double getStartLocationX() {
        return startLocationX;
    }

    public void setStartLocationX(double startLocationX) {
        this.startLocationX = startLocationX;
    }

    public double getStartLocationY() {
        return startLocationY;
    }

    public void setStartLocationY(double startLocationY) {
        this.startLocationY = startLocationY;
    }

    public double getEndLocationX() {
        return endLocationX;
    }

    public void setEndLocationX(double endLocationX) {
        this.endLocationX = endLocationX;
    }

    public double getEndlocationY() {
        return endlocationY;
    }

    public void setEndlocationY(double endlocationY) {
        this.endlocationY = endlocationY;
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
        parcel.writeDouble(startLocationX);
        parcel.writeDouble(startLocationY);
        parcel.writeDouble(endLocationX);
        parcel.writeDouble(endlocationY);
        parcel.writeInt(seats);
        parcel.writeInt(genderPrefer);
        parcel.writeString(carDescription);
    }
}

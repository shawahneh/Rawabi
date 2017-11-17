package com.techcamp.aauj.rawabi.Beans;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 11/16/2017.
 */

public class Journey {
    private int id;
    private User user;
    private int startLocationX;
    private int startLocationY;
    private int endLocationX;
    private int endlocationY;
    private int seats;
    private Date goingDate;
    private int genderPrefer;//0 : male , 1 : female
    private String carDescription;
    private ArrayList<Ride> rides;

    public ArrayList<Ride> getRides() {
        return rides;
    }

    public void setRides(ArrayList<Ride> rides) {
        this.rides = rides;
    }

    public Journey()
    {

    }
    public Journey(int id,User user, int startLocationX, int startLocationY, int endLocationX, int endlocationY, int seats, Date goingDate, int genderPrefer, String carDescription,ArrayList<Ride> rides) {
        this.rides = rides;
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


    public int getStartLocationX() {
        return startLocationX;
    }

    public void setStartLocationX(int startLocationX) {
        this.startLocationX = startLocationX;
    }

    public int getStartLocationY() {
        return startLocationY;
    }

    public void setStartLocationY(int startLocationY) {
        this.startLocationY = startLocationY;
    }

    public int getEndLocationX() {
        return endLocationX;
    }

    public void setEndLocationX(int endLocationX) {
        this.endLocationX = endLocationX;
    }

    public int getEndlocationY() {
        return endlocationY;
    }

    public void setEndlocationY(int endlocationY) {
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
}

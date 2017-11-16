package com.techcamp.aauj.rawabi.Beans;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alaam on 11/15/2017.
 */

public class User {
    private String fullname;
    private int gender;
    private Date birthdate;
    private String id;
    private String username;
    private String password;
    private String imageurl;
    private String phone;
    private ArrayList<Journey> journeys;
    private ArrayList<Ride> rides;

    public User() {
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<Journey> getJourneys() {
        return journeys;
    }

    public void setJourneys(ArrayList<Journey> journeys) {
        this.journeys = journeys;
    }

    public ArrayList<Ride> getRides() {
        return rides;
    }

    public void setRides(ArrayList<Ride> rides) {
        this.rides = rides;
    }

    public User(String fullname, int gender, Date birthdate, String id, String username, String password, String imageurl, String phone, ArrayList<Journey> journeys, ArrayList<Ride> rides) {
        this.rides = rides;
        this.journeys = journeys;
        this.fullname = fullname;
        this.gender = gender;
        this.birthdate = birthdate;
        this.id = id;
        this.username = username;
        this.password = password;
        this.imageurl = imageurl;
        this.phone = phone;
    }
}

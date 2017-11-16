package com.techcamp.aauj.rawabi.Beans;

import java.util.Date;

/**
 * Created by alaam on 11/15/2017.
 */

public class User {
    private String fname,lname;
    private int gender;
    private Date birthdate;
    private String id;
    private String username;
    private String password;
    private String imageurl;
    private String phone;

    public User() {
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public User(String fname, String lname, int gender, Date birthdate, String id, String username, String password, String imageurl, String phone) {

        this.fname = fname;
        this.lname = lname;
        this.gender = gender;
        this.birthdate = birthdate;
        this.id = id;
        this.username = username;
        this.password = password;
        this.imageurl = imageurl;
        this.phone = phone;
    }
}

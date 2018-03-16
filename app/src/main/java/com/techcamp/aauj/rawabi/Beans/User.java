package com.techcamp.aauj.rawabi.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ALa on 11/15/2017.
 */

public class User implements Parcelable{
    public static final int MALE = 0;
    public static final int FEMALE = 1;
    private String fullname;
    private int gender;
    private Date birthdate;
    private int id;
    private String username;
    private String password;
    private String imageurl;
    private String phone;
    private String address;
    private float rating;

    protected User(Parcel in) {
        fullname = in.readString();
        gender = in.readInt();
        id = in.readInt();
        username = in.readString();
        password = in.readString();
        imageurl = in.readString();
        phone = in.readString();
        address = in.readString();
        rating = in.readFloat();
        birthdate = new Date(in.readLong());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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


    public User(String fullname, int gender, Date birthdate, int id, String username, String password, String imageurl, String phone,String address) {
        this.fullname = fullname;
        this.gender = gender;
        this.birthdate = birthdate;
        this.id = id;
        this.username = username;
        this.password = password;
        this.imageurl = imageurl;
        this.phone = phone;
        this.address = address;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fullname);
        parcel.writeInt(gender);
        parcel.writeInt(id);
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(imageurl);
        parcel.writeString(phone);
        parcel.writeString(address);
        parcel.writeFloat(rating);
        parcel.writeLong(birthdate ==null?0:birthdate.getTime());
    }
}

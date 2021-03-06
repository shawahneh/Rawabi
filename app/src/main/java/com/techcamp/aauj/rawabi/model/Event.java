package com.techcamp.aauj.rawabi.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by ALa on 11/15/2017.
 */

public class Event implements Parcelable {
    int id;
    private String name,description,imageUrl;
    private Date date;
    public Event(){}

    protected Event(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        date = new Date(in.readLong());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public int getId() {return id;}
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(imageUrl);
        parcel.writeLong(date.getTime());
    }
}

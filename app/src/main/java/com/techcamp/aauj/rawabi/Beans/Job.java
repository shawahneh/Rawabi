package com.techcamp.aauj.rawabi.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.techcamp.aauj.rawabi.utils.DateUtil;

import java.util.Date;

/**
 * Created by alaam on 12/31/2017.
 */

public class Job  implements Parcelable {
    private String name,description,imageUrl;
    private Date date;
    public Job(){}

    protected Job(Parcel in) {
        name = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        date = new Date(in.readLong());
    }

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

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
    public String getRealDate(){
        return DateUtil.formatDate(date.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(imageUrl);
        parcel.writeLong(date.getTime());
    }
}
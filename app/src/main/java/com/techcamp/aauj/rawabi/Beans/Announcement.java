package com.techcamp.aauj.rawabi.Beans;

import android.text.format.DateUtils;

import com.techcamp.aauj.rawabi.utils.DateUtil;

import java.util.Date;

/**
 * Created by alaam on 12/31/2017.
 */

public class Announcement {
    private String name,description,imageUrl;
    private Date date;
    public Announcement(){}

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
}
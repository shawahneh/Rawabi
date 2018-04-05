package com.techcamp.aauj.rawabi.model;

import java.util.Date;

public class CacheItem{
    private String id,value;

    public CacheItem(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public CacheItem() {

    }

    private Date date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

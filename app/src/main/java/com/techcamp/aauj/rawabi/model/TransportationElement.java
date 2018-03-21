package com.techcamp.aauj.rawabi.model;

/**
 * Created by ALa on 3/8/2018.
 */

public class TransportationElement {
    public static final int TYPE_FROM_RAWABI = 0;
    public static final int TYPE_FROM_RAMALLAH = 1;

    private String time;
    private int id,type;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TransportationElement() {

    }
}

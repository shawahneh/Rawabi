package com.techcamp.aauj.rawabi.model.CustomBeans;

import com.techcamp.aauj.rawabi.model.Ride;

import java.util.ArrayList;

/**
 * Created by ALa on 1/18/2018.
 */

public class CustomJourney {
    private int status;
    private ArrayList<Ride> riders;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<Ride> getRiders() {
        return riders;
    }

    public void setRiders(ArrayList<Ride> riders) {
        this.riders = riders;
    }
}

package com.techcamp.aauj.rawabi.Beans;

/**
 * Created by User on 11/16/2017.
 */

public class Ride {
    private int id;
    private User user;
    private Journey journey;
    private int meetingLocationX;
    private int meetingLocationY;
    private int orderStatus;//Pending : 0 , Accepted : 1 , rejected : 2, cancelled from rider : 4

    public Ride(){

    }
    public Ride(int id, User user, Journey journey, int meetingLocationX, int meetingLocationY, int orderStatus) {
        this.id = id;
        this.user = user;
        this.journey = journey;
        this.meetingLocationX = meetingLocationX;
        this.meetingLocationY = meetingLocationY;
        this.orderStatus = orderStatus;
    }

    public int getId() {
        return id;
    }



    public User getUser() {
        return user;
    }



    public Journey getJourney() {
        return journey;
    }

    public int getMeetingLocationX() {
        return meetingLocationX;
    }

    public void setMeetingLocationX(int meetingLocationX) {
        this.meetingLocationX = meetingLocationX;
    }

    public int getMeetingLocationY() {
        return meetingLocationY;
    }

    public void setMeetingLocationY(int meetingLocationY) {
        this.meetingLocationY = meetingLocationY;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}

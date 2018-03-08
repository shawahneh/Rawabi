package com.techcamp.aauj.rawabi.Beans.CustomBeans;

import android.os.Parcel;
import android.os.Parcelable;

import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.utils.DateUtil;

import java.util.Date;

/**
 * Created by alaam on 2/18/2018.
 */

public class FireJourney implements Parcelable {
    public final static int STATUS_PENDING = 0;
    public final static int STATUS_CANCELLED = 1;
    public final static int STATUS_COMPLETED = 2;
    public final static int STATUS_DRIVER_CANCELLED = 3;
    public final static int STATUS_DRIVER_CLOSED = 4;

    // if pending and started => STATUS_COMPLETED
    // STATUS_DRIVER_CLOSED and started -> STATUS_COMPLETED
    // STATUS_DRIVER_CLOSED and still not started -> STATUS_DRIVER_CLOSED
    // STATUS_PENDING and not started -> pending


    private int id;
    private User user;
    private LatLng startPoint;
    private LatLng endPoint;
    private int seats;
    private Date goingDate;
    private int genderPrefer;//0 : male , 1 : female
    private String carDescription;
    private int status; // 0 : pending , 1 : cancelled , 2 : completed

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    protected FireJourney(Parcel in) {
        id = in.readInt();
        user = in.readParcelable(User.class.getClassLoader());
        startPoint = in.readParcelable(LatLng.class.getClassLoader());
        endPoint = in.readParcelable(LatLng.class.getClassLoader());
        seats = in.readInt();
        genderPrefer = in.readInt();
        carDescription = in.readString();
        status = in.readInt();
        goingDate = new Date(in.readLong());
    }

    public static final Creator<FireJourney> CREATOR = new Creator<FireJourney>() {
        @Override
        public FireJourney createFromParcel(Parcel in) {
            return new FireJourney(in);
        }

        @Override
        public FireJourney[] newArray(int size) {
            return new FireJourney[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public LatLng getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(LatLng startPoint) {
        this.startPoint = startPoint;
    }

    public LatLng getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(LatLng endPoint) {
        this.endPoint = endPoint;
    }


    public String getRealTime(){
        return DateUtil.formatDateToTime(goingDate.getTime());
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FireJourney()
    {


    }


    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }




    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Date getGoingDate() {
        return goingDate;
    }

    public void setGoingDate(Date goingDate) {
        this.goingDate = goingDate;
    }

    public int getGenderPrefer() {
        return genderPrefer;
    }

    public void setGenderPrefer(int genderPrefer) {
        this.genderPrefer = genderPrefer;
    }

    public String getCarDescription() {
        return carDescription;
    }

    public void setCarDescription(String carDescription) {
        this.carDescription = carDescription;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeParcelable(user, i);
        parcel.writeParcelable(startPoint, i);
        parcel.writeParcelable(endPoint, i);
        parcel.writeInt(seats);
        parcel.writeInt(genderPrefer);
        parcel.writeString(carDescription);
        parcel.writeInt(status);
        parcel.writeLong(goingDate == null?0:goingDate.getTime());
    }

    public Journey toJourney(){
        Journey journey = new Journey();
        journey.setId(getId());
        journey.setUser(getUser());


        journey.setStartPoint(new com.google.android.gms.maps.model.LatLng(getStartPoint().getLatitude(),getStartPoint().getLongitude()));
        journey.setEndPoint(new com.google.android.gms.maps.model.LatLng(getEndPoint().getLatitude(),getEndPoint().getLongitude()));
        journey.setSeats(getSeats());
        journey.setGenderPrefer(getGenderPrefer());
        journey.setCarDescription(getCarDescription());
        journey.setGoingDate(getGoingDate());
        journey.setStatus(getStatus());

        return journey;
    }
}

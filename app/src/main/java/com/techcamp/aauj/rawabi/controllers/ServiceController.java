package com.techcamp.aauj.rawabi.controllers;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techcamp.aauj.rawabi.model.Ride;
import com.techcamp.aauj.rawabi.services.MyService;
import com.techcamp.aauj.rawabi.services.RideService;

/**
 * Created by ALa on 1/18/2018.
 */

// unused ( used for testing on Firebase )
public class ServiceController {
    public static void createJourney(Context context, int jid){
        Intent intent = new Intent(context, MyService.class);
        intent.putExtra("jid",jid);
        context.startService(intent);
    }
    public static void stopService(Context context){
        context.stopService(new Intent(context,MyService.class));
    }
    public static void createRide(Context context, Ride ride, int jid){
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child("journeys_riders");
        mData.child("" + jid).push().setValue(ride.getId());
        DatabaseReference mDataRides = FirebaseDatabase.getInstance().getReference().child("rides");
        mDataRides.child(ride.getId()+"").setValue(ride);

        Intent intent = new Intent(context, RideService.class);
        intent.putExtra("rid",ride.getId());
        intent.putExtra("mode","create");
        context.startService(intent);
    }
    public static void cancelRide(Context context, int rid){

        Intent intent = new Intent(context, RideService.class);
        intent.putExtra("rid",rid);
        intent.putExtra("mode","cancel");
        context.startService(intent);
    }

    public static void changeRideStatus(int rid, int status){
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child("rides");
        mData.child("" + rid).child("orderStatus").setValue(status);
    }

}

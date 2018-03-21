package com.techcamp.aauj.rawabi.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.ITrigger;
import com.techcamp.aauj.rawabi.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by ALa on 11/17/2017.
 */

public class MapUtil {
    public static final int ICON_START_POINT = 1;
    public static final int ICON_END_POINT = 2;
    public static final int ICON_MEETING_LOCATION = 3;
    public static final int ICON_CAR = 4;
    public static final int ICON_RIDER = 5;
    public static final int ICON_RIDER_PENDING = 6;
    public static final int ICON_RIDER_ACCEPTED = 7;
    public static final int ICON_RIDER_CANCELLED = 8;

    public static Location CurrentLocation = null;
    public static String getAddress(Context context, LatLng latLng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.isEmpty())
                return "Map point";
            Address obj = addresses.get(0);
            return obj.getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
            return "Map point";
        }
    }
    public static String getSavedAddress(Context context, LatLng latLng) {
        if(latLng.latitude == 32.008049 && latLng.longitude == 35.187367)
            return "Rawabi";
        return "Map point";
    }
    public static double getDistance(LatLng LatLng1, LatLng LatLng2) {
        if(LatLng1 == null || LatLng2 == null)
            return -1;
        double distance = 0;
        Location locationA = new Location("A");
        locationA.setLatitude(LatLng1.latitude);
        locationA.setLongitude(LatLng1.longitude);
        Location locationB = new Location("B");
        locationB.setLatitude(LatLng2.latitude);
        locationB.setLongitude(LatLng2.longitude);
        distance = locationA.distanceTo(locationB);

        return distance;
    }

    public static Location getCurrentLoc(Context context,@Nullable final ITrigger<Location> locationITriger) {
        Log.d("tag","getCurrentLoc");
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (locationManager != null) {
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                CurrentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                CurrentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        return CurrentLocation;
    }

    public static boolean isConnectionAvailable(Context context) {

        boolean netCon = false;

        try {

            //Internet & network information "Object" initialization
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connectivityManager != null) {
                networkInfo = connectivityManager.getActiveNetworkInfo();
            }
            //Internet enable & connectivity checking
            if (networkInfo != null && ("WIFI".equals(networkInfo.getTypeName()) || "MOBILE".equals(networkInfo.getTypeName())) &&  networkInfo.isAvailable() && networkInfo.isConnectedOrConnecting()) {
                netCon = true;
            }
        } catch (Exception e) {

        }
        return netCon;
    }

    public static  boolean isGpsAvailable(Context context) {

        boolean gpsCon = false;

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsCon = true;
        }
        return gpsCon;
    }
    public static BitmapDescriptor getMarkerIconByColor(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
    public static BitmapDescriptor getMarkerIcon(int icon) {
        switch (icon){
            case ICON_CAR:
                return BitmapDescriptorFactory.fromResource(R.drawable.car2);
            case ICON_END_POINT:
                return getMarkerIconByColor("#cf0000");
            case ICON_START_POINT:
                return getMarkerIconByColor("#a1cf68");
            case ICON_MEETING_LOCATION:
                return getMarkerIconByColor("#5898cc");
            case ICON_RIDER:
                return getMarkerIconByColor("#cf0000");
            case ICON_RIDER_ACCEPTED:
                return getMarkerIconByColor("#a1cf68");
            case ICON_RIDER_CANCELLED:
                return getMarkerIconByColor("#cf0000");
            case ICON_RIDER_PENDING:
                return getMarkerIconByColor("#21b5cc");
            default:
                return getMarkerIconByColor("#475862");
        }

    }
}

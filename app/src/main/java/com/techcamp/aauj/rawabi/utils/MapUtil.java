package com.techcamp.aauj.rawabi.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.ITriger;

import java.util.List;
import java.util.Locale;

/**
 * Created by alaam on 11/17/2017.
 */

public class MapUtil {

    public static Location CurrentLocation = null;
    public static String getAddress(Context context, LatLng latLng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.isEmpty())
                return "EmUnknown";
            Address obj = addresses.get(0);

            return obj.getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
            return "ErUnknown";
        }
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

    public static void getCurrentLoc(Context context,@Nullable final ITriger<Location> locationITriger) {
        Log.d("tag","getCurrentLoc");
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        CurrentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // called when the location provider status changes. Possible status: OUT_OF_SERVICE, TEMPORARILY_UNAVAILABLE or AVAILABLE.
            }

            public void onProviderEnabled(String provider) {
                // called when the location provider is enabled by the user
            }

            public void onProviderDisabled(String provider) {
                // called when the location provider is disabled by the user. If it is already disabled, it's called immediately after requestLocationUpdates
            }

            public void onLocationChanged(Location location) {
                if(locationITriger != null)
                locationITriger.onTriger(location);

                CurrentLocation = location;
            }
        });
    }
}

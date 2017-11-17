package com.techcamp.aauj.rawabi.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.ITriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.fragments.DriverFragments.DriverDetailDialogFragment;
import com.techcamp.aauj.rawabi.fragments.ItemsListFragment;
import com.techcamp.aauj.rawabi.fragments.UserTypeFragment;
import com.techcamp.aauj.rawabi.utils.MapUtil;

import java.util.Date;

public class CarpoolActivity extends AppCompatActivity implements OnMapReadyCallback,
        UserTypeFragment.IUserTypeFragmenetListener,ItemsListFragment.IFragmentListener {

    private static final int TYPE_MARK_START = 0;
    private static final int TYPE_MARK_END = 1;
    private GoogleMap mMap;
    private LatLng mMarkerStartPoint;
    private LatLng mMarkerEndPoint;
    private Fragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool);

// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (savedInstanceState == null) {
            setFragment(new UserTypeFragment(), "tag");
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMyLocationEnable();
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng PERTH = new LatLng(-31.90, 115.86);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Start Position").draggable(true).icon(getMarkerIcon("#475862")))
                .setTag(TYPE_MARK_START);
        mMap.addMarker(new MarkerOptions().position(PERTH).title("End Position").draggable(true).icon(getMarkerIcon("#a1cf68")))
                .setTag(TYPE_MARK_END);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));

        MapUtil.getCurrentLoc(this, new ITriger<Location>() {
            @Override
            public void onTriger(Location value) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(value.getLatitude(),value.getLongitude()),10));
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.d("tag", "onMarkerDragStart");
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                int type = (Integer) marker.getTag();
                if (type == TYPE_MARK_START)
                    mMarkerStartPoint = marker.getPosition();
                else
                    mMarkerEndPoint = marker.getPosition();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.d("tag", "onMarkerDragEnd");
                if(mFragment != null)
                    if(mFragment instanceof ItemsListFragment){
                        if((Integer)marker.getTag() == TYPE_MARK_START)
                        ((ItemsListFragment) mFragment).refreshView(marker.getPosition());
                    }
            }
        });
    }

    private void setMyLocationEnable() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void setFragment(android.support.v4.app.Fragment fragment, String tag){
        mFragment = fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content,fragment,tag)
                .commit();
    }
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
    @Override
    public void onTypeClick(int type) {
        if(type == 0){
            Journey journey = new Journey();
            User user = new User();
            user.setFullname("ALa Amarneh");
            user.setPhone("05923587222");

            journey.setUser(user);
            journey.setStartPoint(mMarkerStartPoint);
            journey.setEndPoint(mMarkerEndPoint);
            journey.setCarDescription("Red BMW 2017");
            journey.setGoingDate(new Date());

            DriverDetailDialogFragment fragment = DriverDetailDialogFragment.newInstance(journey);
            fragment.show(getFragmentManager(),"tag");
        }else{
            setFragment(ItemsListFragment.getInstance(mMarkerStartPoint),"tag");
        }
    }

    @Override
    public void onJourneyClick(Journey journey) {
        DriverDetailDialogFragment fragment = DriverDetailDialogFragment.newInstance(journey);
        fragment.show(getFragmentManager(),"tag");
    }
}

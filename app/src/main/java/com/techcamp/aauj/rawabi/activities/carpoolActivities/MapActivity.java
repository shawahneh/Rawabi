package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.internal.zzp;
import com.techcamp.aauj.rawabi.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private boolean getLocation = false;

    private LatLng mLatLngCenter;
    private Marker mMarkerCenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setupMarker();
        setupMap();

        enableGetLocation();
    }

    private void setupMarker() {
        mLatLngCenter = new LatLng(0,0);
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //drawCenterMarker();
        mMarkerCenter = mMap.addMarker(new MarkerOptions().position(googleMap.getCameraPosition().target));
        // setup center marker
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mLatLngCenter = mMap.getCameraPosition().target;
                //drawCenterMarker();
                mMarkerCenter.setPosition(mLatLngCenter);
            }
        });
    }

    private void drawCenterMarker() {
        if(mMap == null)
            return;
        if(!getLocation)
            return;

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(mLatLngCenter));
    }

    private void pressFromMode(){

    }
    private void pressToMode(){

    }
    private void enableGetLocation(){
        getLocation = true;
        showCenterMarker();
    }

    private void showCenterMarker() {

    }

}

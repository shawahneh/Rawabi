package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.techcamp.aauj.rawabi.API.CarpoolApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.controllers.SPController;
import com.techcamp.aauj.rawabi.controllers.ServiceController;
import com.techcamp.aauj.rawabi.utils.MapUtil;

public class MeetingMapActivity extends AppCompatActivity implements OnMapReadyCallback ,IResponeTriger<Integer>{
    private Button btnHisLocation,btnMyLocation;
    private View btnMarker;
    private Button mButtonSubmit;
    private static final int TYPE_MARK_START = 0;
    private static final int TYPE_MARK_END = 1;
    private GoogleMap mMap;
    private LatLng mMarkerStartPoint;
    private LatLng mMarkerEndPoint;
    private LatLng mMarkerMeetingPoint;
    private LatLng mMarkerCenter;
    private Journey mJourney;
    private Ride mRide;
    CarpoolApi poolingRides = WebService.getInstance(this);
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_map);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnHisLocation = findViewById(R.id.btnHisLocation);
        btnMyLocation= findViewById(R.id.btnMyLocation);

        btnMarker = findViewById(R.id.btnMarker);

        mButtonSubmit = findViewById(R.id.btnSubmit);

        mJourney = getIntent().getParcelableExtra("journey");
        mMarkerStartPoint = mJourney.getStartPoint();
        mMarkerEndPoint = mJourney.getEndPoint();
        mMarkerMeetingPoint =new LatLng(MapUtil.CurrentLocation.getLatitude(),MapUtil.CurrentLocation.getLongitude());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestDriver();
            }
        });
        btnHisLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMarkerMeetingPoint = mJourney.getStartPoint();
                drawMarkers();
            }
        });
        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMarkerMeetingPoint =new LatLng(MapUtil.CurrentLocation.getLatitude(),MapUtil.CurrentLocation.getLongitude());
                drawMarkers();
            }
        });
        btnMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMarkerMeetingPoint = mMarkerCenter;
                drawMarkers();
            }
        });
    }


    private void drawMarkers(){
        if(mMap == null)
            return;
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(mMarkerStartPoint)
                .draggable(false)
                .icon(MapUtil.getMarkerIcon(MapUtil.ICON_START_POINT))
                .title("Start point")
        );
        mMap.addMarker(new MarkerOptions()
                .position(mMarkerEndPoint)
                .draggable(false)
                .icon(MapUtil.getMarkerIcon(MapUtil.ICON_END_POINT))
                .title("End point")
        );
        mMap.addPolyline(new PolylineOptions().add(mMarkerStartPoint,mMarkerEndPoint));

        mMap.addMarker(new MarkerOptions()
                .position(mMarkerMeetingPoint)
                .draggable(false)
                .icon(MapUtil.getMarkerIcon(MapUtil.ICON_MEETING_LOCATION))
                .title("Meeting Point")
        ).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLng(mMarkerMeetingPoint));

    }

    private void RequestDriver() {
        mButtonSubmit.setEnabled(false);
        mRide = new Ride();

        mRide.setJourney(mJourney);
        mRide.setUser(SPController.getLocalUser(this));
        mRide.setOrderStatus(Ride.STATUS_PENDING);
        mRide.setMeetingLocation(mMarkerMeetingPoint);

        showProgress();

        poolingRides.setRideOnJourney(mRide,this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMarkerMeetingPoint,15));

        drawMarkers();


        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mMarkerCenter = mMap.getCameraPosition().target;
            }
        });
    }

    private void showProgress(){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Sending Request");
        pDialog.setCancelable(false);
        pDialog.show();
    }
    private void showError(){
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("can't send your request, try again")
                .show();
    }
    @Override
    public void onResponse(Integer item) {
        pDialog.dismissWithAnimation();
        // request created successfully
        if(item >0){
            ServiceController.createRide(this,item,mJourney.getId());
                        //            AlarmController.addAlarm(this,mRide.getJourney());
            Intent i = RideDetailActivity.getIntent(this,mRide);
            startActivity(i);
            finish();
        }else {
            mButtonSubmit.setEnabled(true);
            showError("error");
        }
    }

    @Override
    public void onError(String err) {
        pDialog.dismissWithAnimation();
        showError();
    }
    private void showError(String error){

    }
}

package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.techcamp.aauj.rawabi.API.CarpoolApi;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.model.Journey;
import com.techcamp.aauj.rawabi.model.Ride;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.controllers.SPController;
import com.techcamp.aauj.rawabi.utils.MapUtil;

/**
 * this activity allows user to set meeting location address on the map
 */
public class MeetingMapActivity extends AppCompatActivity implements OnMapReadyCallback, ICallBack<Integer> {
    private Button btnHisLocation, btnMyLocation;
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
    CarpoolApi poolingRides = WebFactory.getCarpoolService();
    SweetAlertDialog pDialog;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_map);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnHisLocation = findViewById(R.id.btnHisLocation);
        btnMyLocation = findViewById(R.id.btnMyLocation);

        btnMarker = findViewById(R.id.btnMarker);

        mButtonSubmit = findViewById(R.id.btnSubmit);

        mJourney = getIntent().getParcelableExtra("journey");
        mMarkerMeetingPoint = getIntent().getParcelableExtra("location");

        mMarkerStartPoint = mJourney.getStartPoint();
        mMarkerEndPoint = mJourney.getEndPoint();

//        LocationManager locationManager = new LocationManager().getLastKnownLocation("");

//        mMarkerMeetingPoint = new LatLng(location.getLatitude(),location.getLongitude());
//        mMarkerMeetingPoint = new LatLng(MapUtil.getCurrentLoc(this,null).getLatitude(),MapUtil.getCurrentLoc(this,null).getLongitude());

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
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(mMarkerMeetingPoint));

    }

    private void RequestDriver() {
        mButtonSubmit.setEnabled(false);
        mRide = new Ride();

        mRide.setJourney(mJourney);
        mRide.setUser(SPController.getLocalUser(this));
        mRide.setOrderStatus(Ride.STATUS_PENDING);
        mRide.setMeetingLocation(mMarkerMeetingPoint);

        showProgress();

        poolingRides.setRideOnJourney(mRide,this)
            .start();

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
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(mJourney.getStartPoint());
        builder.include(mJourney.getEndPoint());
        builder.include(mMarkerMeetingPoint);
        LatLngBounds bounds = builder.build();

        final int zoomWidth = getResources().getDisplayMetrics().widthPixels;
        final int zoomHeight = getResources().getDisplayMetrics().heightPixels;
        final int zoomPadding = (int) (zoomWidth * 0.10); // offset from edges of the map 12% of screen

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,zoomWidth,zoomHeight,zoomPadding));

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
        if(item == -2){
            showError("no available seats");
        }else
        if(item > 0){
            mRide.setId(item);
            Intent i = RideDetailActivity.getIntent(this,mRide);
            startActivity(i);
            finish();
            WebFactory.getOfflineService().saveRide(this,mRide);
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
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}

package com.techcamp.aauj.rawabi.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.techcamp.aauj.rawabi.API.PoolingRides;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.RideDetailActivity;
import com.techcamp.aauj.rawabi.controllers.SPController;
import com.techcamp.aauj.rawabi.utils.MapUtil;

public class MeetingMapActivity extends AppCompatActivity implements OnMapReadyCallback ,IResponeTriger<Integer>{
    private RadioButton mRadioButtonHisLocation,mRadioButtonMyLocation;
    private View mRadioButtonMarker;
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
    MarkerOptions markerOptions;
    PoolingRides poolingRides = WebService.getInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_map);
        mRadioButtonHisLocation = findViewById(R.id.rbHisLocation);
        mRadioButtonMarker = findViewById(R.id.rbMarker);
        mRadioButtonMyLocation = findViewById(R.id.rbMyLocation);
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
        mRadioButtonMyLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mMarkerMeetingPoint =new LatLng(MapUtil.CurrentLocation.getLatitude(),MapUtil.CurrentLocation.getLongitude());
                    drawMarkers();
                }
            }
        });
        mRadioButtonHisLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mMarkerMeetingPoint = mJourney.getStartPoint();
                    drawMarkers();
                }
            }
        });
        mRadioButtonMarker.setOnClickListener(new View.OnClickListener() {
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
                .icon(getMarkerIcon("#475862"))
                .title("Start point")
        );
        mMap.addMarker(new MarkerOptions()
                .position(mMarkerEndPoint)
                .draggable(false)
                .icon(getMarkerIcon("#a1cf68"))
                .title("End point")
        );
        mMap.addPolyline(new PolylineOptions().add(mMarkerStartPoint,mMarkerEndPoint));

        mMap.addMarker(new MarkerOptions()
                .position(mMarkerMeetingPoint)
                .draggable(false)
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
        poolingRides.setRideOnJourney(mRide,this);
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

    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    @Override
    public void onResponse(Integer item) {
        // request created
        if(item >0){
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
        showError(err);
    }
    private void showError(String error){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}

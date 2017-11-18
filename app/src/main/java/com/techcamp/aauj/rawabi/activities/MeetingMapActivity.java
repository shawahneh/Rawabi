package com.techcamp.aauj.rawabi.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.R;

public class MeetingMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private RadioButton mRadioButtonMarker,mRadioButtonHisLocation,mRadioButtonMyLocation;
    private Button mButtonSubmit;
    private static final int TYPE_MARK_START = 0;
    private static final int TYPE_MARK_END = 1;
    private GoogleMap mMap;
    private LatLng mMarkerStartPoint;
    private LatLng mMarkerEndPoint;
    private LatLng mMarkerMeetingPoint;
    private Journey mJourney;
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
        mMarkerMeetingPoint = mMarkerStartPoint;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestDriver();
            }
        });
    }

    private void RequestDriver() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMarkerStartPoint,10));


        mMap.addMarker(new MarkerOptions()
                .position(mMarkerStartPoint)
                .draggable(true)
                .title("Meeting Point")
        ).setTag("meetingPoint");

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                mMarkerMeetingPoint = marker.getPosition();
            }
        });
    }
}

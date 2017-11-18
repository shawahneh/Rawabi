package com.techcamp.aauj.rawabi.activities;

import android.Manifest;
import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.techcamp.aauj.rawabi.API.PoolingJourney;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.ITriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.fragments.DriverFragments.DriverDetailDialogFragment;
import com.techcamp.aauj.rawabi.fragments.ItemsListFragment;
import com.techcamp.aauj.rawabi.fragments.UserTypeFragment;
import com.techcamp.aauj.rawabi.utils.MapUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CarpoolActivity extends AppCompatActivity implements OnMapReadyCallback{
    private PoolingJourney mPoolingJourney = WebApi.getInstance(this);
    private static final int TYPE_MARK_START = 0;
    private static final int TYPE_MARK_END = 1;
    private GoogleMap mMap;
    private LatLng mMarkerStartPoint;
    private LatLng mMarkerEndPoint;
    private Date goingDate = new Date();
    private Fragment mFragment;
    private int mMode;
    private ArrayList<Journey> journeys;
    LatLng sydney = new LatLng(-34, 151);
    LatLng PERTH = new LatLng(-31.90, 115.86);
    private Button mButtonSubmit,mButtonRidingAt;
    private TextView mTextViewFrom,mTextViewTo;
    private EditText mEditTextSeatsNumber,mEditTextCarDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool);
        mMode = getIntent().getIntExtra("mode",0);
        mButtonRidingAt = findViewById(R.id.btnRidingAt);
        mButtonSubmit = findViewById(R.id.btnSubmit);
        mTextViewFrom = findViewById(R.id.tvFrom);
        mTextViewTo = findViewById(R.id.tvTo);
        mEditTextSeatsNumber = findViewById(R.id.txtSeatsNumber);
        mEditTextCarDesc = findViewById(R.id.txtCarDesc);

        initUI();
        setDefaultMarkers();
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        if (savedInstanceState == null) {
//            setFragment(new UserTypeFragment(), "tag");
//        }
    }

    private void setDefaultMarkers() {
        mMarkerStartPoint = new LatLng(32.01305201874965,35.19094504415989);
        mMarkerEndPoint = new LatLng(32.01038562592371,35.191298089921474);
    }

    private void initUI() {
        if(mMode == 0){
            mEditTextCarDesc.setVisibility(View.GONE);
            mEditTextSeatsNumber.setVisibility(View.GONE);
            mButtonSubmit.setVisibility(View.GONE);

        }else{
            mEditTextCarDesc.setVisibility(View.VISIBLE);
            mEditTextSeatsNumber.setVisibility(View.VISIBLE);
            mButtonSubmit.setVisibility(View.VISIBLE);
        }


        mButtonRidingAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowTimePicker();
            }
        });
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitJourney();
            }
        });

    }

    private void ShowTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                updateDateButton(i,i1);
            }
        },hour,minute,true);

        timePickerDialog.setTitle("Set Going Time");
        timePickerDialog.show();
    }

    private void updateDateButton(int hour, int min) {
        Log.d("tag","updateDateButton");
        Log.d("tag","hour " + hour);
        if(mMode == 0){
            mButtonRidingAt.setText("Riding At " + hour+":" + min);
            goingDate = new Date();
            goingDate.setHours(hour);
            goingDate.setMinutes(min);
            refreshData();
        }else{
            mButtonRidingAt.setText("Driving At " + hour+":" + min);
        }
    }

    private void refreshData() {
        mTextViewFrom.setText(MapUtil.getAddress(this,mMarkerStartPoint.latitude,mMarkerStartPoint.longitude));
        mTextViewTo.setText(MapUtil.getAddress(this,mMarkerEndPoint.latitude,mMarkerEndPoint.longitude));
        if(mMode == 0)
            downloadJournays();

    }
    public void downloadJournays(){
        mPoolingJourney.filterJourneys(mMarkerStartPoint, mMarkerEndPoint, goingDate, 0, new ITriger<ArrayList<Journey>>() {
            @Override
            public void onTriger(ArrayList<Journey> value) {
                journeys = value;
                updateMap();
            }
        });
    }

    private void updateMap() {
        if(mMap == null)
            return;

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(mMarkerStartPoint).title("Start Position").draggable(true).icon(getMarkerIcon("#475862")))
                .setTag(TYPE_MARK_START);
        mMap.addMarker(new MarkerOptions().position(mMarkerEndPoint).title("End Position").draggable(true).icon(getMarkerIcon("#a1cf68")))
                .setTag(TYPE_MARK_END);


        Log.d("tag","updateMap");
        if(mMode == 0)
        for (Journey journey : journeys){
            Log.d("tag","add mark "+journey.getStartPoint());
            mMap.addMarker(new MarkerOptions()
                .position(journey.getStartPoint())
                    .title(journey.getUser().getFullname())
                    .snippet("Aavailable seats: " + journey.getSeats())
            ).setTag(journey);
        }
    }

    private void SubmitJourney() {
        String carDesc = mEditTextCarDesc.getText().toString();
        int seats = Integer.parseInt(mEditTextSeatsNumber.getText().toString());
        Journey journey = new Journey();
        journey.setUser(null);
        journey.setStartPoint(mMarkerStartPoint);
        journey.setEndPoint(mMarkerEndPoint);
        journey.setGoingDate(goingDate);
        journey.setCarDescription(carDesc);
        journey.setSeats(seats);
        journey.setGenderPrefer(0);
        journey.setId(-1);
        mPoolingJourney.setNewJourney(journey, new ITriger<Integer>() {
            @Override
            public void onTriger(Integer value) {
                Toast.makeText(CarpoolActivity.this, "joueney created " + value, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMyLocationEnable();
        // Add a marker in Sydney and move the camera


        mMap.addMarker(new MarkerOptions().position(mMarkerStartPoint).title("Start Position").draggable(true).icon(getMarkerIcon("#475862")))
                .setTag(TYPE_MARK_START);
        mMap.addMarker(new MarkerOptions().position(mMarkerEndPoint).title("End Position").draggable(true).icon(getMarkerIcon("#a1cf68")))
                .setTag(TYPE_MARK_END);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
        if(MapUtil.CurrentLocation != null)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(MapUtil.CurrentLocation.getLatitude(),MapUtil.CurrentLocation.getLongitude()),10));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(value.getLatitude(),value.getLongitude()),10));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.d("tag", "onMarkerDragStart");
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Log.d("tag","onMarkerDrag " + marker.getPosition().longitude + " " + marker.getPosition().longitude);
                int type = (Integer) marker.getTag();
                if (type == TYPE_MARK_START)
                    mMarkerStartPoint = marker.getPosition();
                else
                    mMarkerEndPoint = marker.getPosition();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                refreshData();

                Log.d("tag", "onMarkerDragEnd");
//                if(mFragment != null)
//                    if(mFragment instanceof ItemsListFragment){
//                        if((Integer)marker.getTag() == TYPE_MARK_START)
//                        ((ItemsListFragment) mFragment).refreshView(marker.getPosition());
//                    }
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if(marker.getTag() instanceof Journey)
                {
                    DriverDetailDialogFragment fragment = DriverDetailDialogFragment.newInstance((Journey) marker.getTag(), new ITriger<Journey>() {
                        @Override
                        public void onTriger(Journey value) {
                            Intent intent = new Intent(CarpoolActivity.this,MeetingMapActivity.class);
                            intent.putExtra("journey",value);
                            startActivity(intent);
                        }
                    });
                    fragment.show(getFragmentManager(),"tag");
                }
                marker.showInfoWindow();
                return true;
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


//
//    @Override
//    public void onJourneyClick(Journey journey) {
//        DriverDetailDialogFragment fragment = DriverDetailDialogFragment.newInstance(journey, new ITriger<Journey>() {
//            @Override
//            public void onTriger(Journey value) {
//                Intent intent = new Intent(CarpoolActivity.this,MeetingMapActivity.class);
//                intent.putExtra("journey",value);
//                startActivity(intent);
//            }
//        });
//        fragment.show(getFragmentManager(),"tag");
//    }
}

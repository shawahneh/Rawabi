package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.utils.StringUtil;

public class RideDetailActivity extends AppCompatActivity implements OnMapReadyCallback{
    public static final String ARG_RIDE = "ride";
    private static final float DEFAULT_ZOOM = 15;
    private Ride mRide;
    private MapView mMapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail);

        mRide = getIntent().getParcelableExtra(ARG_RIDE);
        mMapView = findViewById(R.id.mapView);

        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvFrom = findViewById(R.id.tvFrom);
        TextView tvTo = findViewById(R.id.tvTo);
        TextView tvCarDesc = findViewById(R.id.tvCarDesc);
        TextView tvDriverName = findViewById(R.id.tvDriverName);
        TextView tvStatus = findViewById(R.id.tvStatus);

        tvDate.setText(mRide.getJourney().getRealDate());
        tvFrom.setText(mRide.getJourney().getStartPoint().toString());
        tvTo.setText(mRide.getJourney().getEndPoint().toString());
        tvCarDesc.setText(mRide.getJourney().getCarDescription());
        tvDriverName.setText(mRide.getJourney().getUser().getFullname());
        tvStatus.setText(StringUtil.getRideStatus(mRide.getOrderStatus()));
    }

    public static Intent getIntent(Context context, Ride ride){
        Intent i = new Intent(context,RideDetailActivity.class);
        i.putExtra(ARG_RIDE,ride);
        return i;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Journey mJourney = mRide.getJourney();
        googleMap.addMarker(new MarkerOptions().position(mRide.getMeetingLocation()).title("Meeting location"));
        googleMap.addMarker(new MarkerOptions().position(mJourney.getStartPoint()).title("Start point"));
        googleMap.addMarker(new MarkerOptions().position(mJourney.getEndPoint()).title("End point"));
        googleMap.addPolyline(new PolylineOptions().add(mJourney.getStartPoint(),mJourney.getEndPoint()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mRide.getMeetingLocation(),DEFAULT_ZOOM));
    }
}

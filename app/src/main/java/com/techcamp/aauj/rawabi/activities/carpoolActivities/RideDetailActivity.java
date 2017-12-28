package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.techcamp.aauj.rawabi.API.PoolingRides;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.controllers.AlarmController;
import com.techcamp.aauj.rawabi.utils.MapUtil;
import com.techcamp.aauj.rawabi.utils.StringUtil;

public class RideDetailActivity extends AppCompatActivity implements OnMapReadyCallback, IResponeTriger<Boolean>{
    public static final String ARG_RIDE = "ride";
    private static final float DEFAULT_ZOOM = 15;
    private Ride mRide;
    private MapView mMapView;
    Button btnCancel;
    TextView tvStatus;
    private PoolingRides poolingRides = WebService.getInstance(this);
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private IResponeTriger<Integer> statusRespones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail);

        mRide = getIntent().getParcelableExtra(ARG_RIDE);
        mMapView = findViewById(R.id.mapView);

        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);

        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvFrom = findViewById(R.id.tvFrom);
        TextView tvTo = findViewById(R.id.tvTo);
        TextView tvCarDesc = findViewById(R.id.tvCarDesc);
        TextView tvDriverName = findViewById(R.id.tvDriverName);
        tvStatus = findViewById(R.id.tvStatus);
        btnCancel = findViewById(R.id.btnCancel);
        tvDate.setText(mRide.getJourney().getRealDate());
        tvFrom.setText(MapUtil.getAddress(this,mRide.getJourney().getStartPoint()));
        tvTo.setText(MapUtil.getAddress(this,mRide.getJourney().getEndPoint()));
        tvCarDesc.setText(mRide.getJourney().getCarDescription());
        tvDriverName.setText(mRide.getJourney().getUser().getFullname());
        tvStatus.setText(StringUtil.getRideStatus(mRide.getOrderStatus()));



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCancel.setEnabled(false);
                mSwipeRefreshLayout.setRefreshing(true);
                poolingRides.changeRideStatus(mRide.getId(),Ride.STATUS_CANCELLED,RideDetailActivity.this);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestStatus();
            }
        });
        statusRespones = new IResponeTriger<Integer>() {
            @Override
            public void onResponse(Integer item) {
                mSwipeRefreshLayout.setRefreshing(false);
                mRide.setOrderStatus(item);
                setupStatus();
            }

            @Override
            public void onError(String err) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };
        setupStatus();
        requestStatus();
    }


    private void setupStatus() {
        if(mRide.getOrderStatus() == Ride.STATUS_CANCELLED || mRide.getJourney().getGoingDate().getTime() < System.currentTimeMillis()){ // ? System.currentTimeMillis()
            btnCancel.setEnabled(false);
            btnCancel.setVisibility(View.GONE);
        }else if (mRide.getOrderStatus() == Ride.STATUS_PENDING){
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setEnabled(true);
        }else if (mRide.getOrderStatus() == Ride.STATUS_ACCEPTED){
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setEnabled(true);
        }

        // setup status color
        if(mRide.getOrderStatus() == Ride.STATUS_CANCELLED){
            tvStatus.setBackgroundColor(Color.RED);
        }else if (mRide.getOrderStatus() == Ride.STATUS_PENDING){
            tvStatus.setBackgroundColor(Color.BLUE);
        }else if (mRide.getOrderStatus() == Ride.STATUS_ACCEPTED){
            tvStatus.setBackgroundColor(Color.GREEN);
        }

        tvStatus.setText(StringUtil.getRideStatus(mRide.getOrderStatus()));
    }
    private void requestStatus(){
        btnCancel.setEnabled(false);
        mSwipeRefreshLayout.setRefreshing(true);
        poolingRides.getStatusOfRide(mRide.getId(), statusRespones);
    }

    public static Intent getIntent(Context context, Ride ride){
        Intent i = new Intent(context,RideDetailActivity.class);
        i.putExtra(ARG_RIDE,ride);
        return i;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Journey mJourney = mRide.getJourney();
        googleMap.addMarker(new MarkerOptions().position(mRide.getMeetingLocation()).icon(MapUtil.getMarkerIcon(MapUtil.ICON_MEETING_LOCATION)).title("Meeting location"));
        googleMap.addMarker(new MarkerOptions().position(mJourney.getStartPoint()).icon(MapUtil.getMarkerIcon(MapUtil.ICON_START_POINT)).title("Start point"));
        googleMap.addMarker(new MarkerOptions().position(mJourney.getEndPoint()).icon(MapUtil.getMarkerIcon(MapUtil.ICON_END_POINT)).title("End point"));
        googleMap.addPolyline(new PolylineOptions().add(mJourney.getStartPoint(),mJourney.getEndPoint()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mRide.getMeetingLocation(),DEFAULT_ZOOM));
    }

    @Override
    public void onResponse(Boolean changed) {
        mSwipeRefreshLayout.setRefreshing(false);
        // status changed (cancelled)
        if(changed) {
            AlarmController.cancelAlarm(this,mRide.getJourney());
            mRide.setOrderStatus(Ride.STATUS_CANCELLED);
            setupStatus();
        }else{
            btnCancel.setEnabled(true);
            showError("Error, we couldn't change the status!");
        }
    }

    @Override
    public void onError(String err) {
        mSwipeRefreshLayout.setRefreshing(false);
        showError("Error, we couldn't change the status!");
    }

    private void showError(String error){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}

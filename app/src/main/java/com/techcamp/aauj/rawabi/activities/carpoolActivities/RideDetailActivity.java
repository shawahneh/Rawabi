package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
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
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.techcamp.aauj.rawabi.API.CarpoolApi;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.model.Journey;
import com.techcamp.aauj.rawabi.model.Ride;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.utils.DateUtil;
import com.techcamp.aauj.rawabi.utils.MapUtil;
import com.techcamp.aauj.rawabi.utils.StringUtil;

public class RideDetailActivity extends AppCompatActivity implements OnMapReadyCallback, ICallBack<Boolean> {
    public static final String ARG_RIDE = "ride";
    private static final float DEFAULT_ZOOM = 15;
    private Ride mRide;
    private MapView mMapView;
    private Button btnCancel,btnFrom,btnTo,btnMeetingLoc;
    private TextView tvStatus;
    private CarpoolApi poolingRides = WebFactory.getCarpoolService();
    private GoogleMap mMap;
    private ICallBack<Integer> statusRespones;
    private SweetAlertDialog pDialog;
    private View layoutDetails,layoutExtra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail);

        mRide = getIntent().getParcelableExtra(ARG_RIDE);
        if (mRide == null)
            finish();

        mMapView = findViewById(R.id.mapView);

        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvFrom = findViewById(R.id.tvFrom);
        TextView tvTo = findViewById(R.id.tvTo);
        TextView tvCarDesc = findViewById(R.id.tvCarDesc);
        TextView tvDriverName = findViewById(R.id.tvDriverName);
        tvStatus = findViewById(R.id.tvStatus);
        btnCancel = findViewById(R.id.btnCancel);
        btnFrom = findViewById(R.id.btnFrom);
        btnTo = findViewById(R.id.btnTo);
        btnMeetingLoc = findViewById(R.id.btnMeetingLoc);
        layoutExtra = findViewById(R.id.layoutExtra);
        layoutDetails = findViewById(R.id.layoutDetails);



        tvDate.setText(DateUtil.getRelativeTime(mRide.getJourney().getGoingDate()));
        tvFrom.setText(MapUtil.getSavedAddress(this,mRide.getJourney().getStartPoint()));
        tvTo.setText(MapUtil.getSavedAddress(this,mRide.getJourney().getEndPoint()));
        tvCarDesc.setText(mRide.getJourney().getCarDescription());
        tvDriverName.setText(mRide.getJourney().getUser().getFullname());
        tvStatus.setText(StringUtil.getRideStatus(mRide.getOrderStatus()));

        layoutDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // toggle layoutExtra visibility
                layoutExtra.setVisibility(layoutExtra.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(RideDetailActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Cancel this Ride")
                        .setConfirmText("Yes,cancel it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                btnCancel.setEnabled(false);
                                showProgress();
                                poolingRides.changeMyRideStatus(mRide.getId(),Ride.STATUS_RIDER_CANCELLED,RideDetailActivity.this);
                            }
                        })
                        .show();

            }
        });
        btnFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMap != null){
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(mRide.getJourney().getStartPoint()));
                }
            }
        });
        btnMeetingLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMap != null){
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(mRide.getMeetingLocation()));
                }
            }
        });
        btnTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMap != null){
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(mRide.getJourney().getEndPoint()));
                }
            }
        });


        statusRespones = new ICallBack<Integer>() {
            @Override
            public void onResponse(Integer item) {
                pDialog.dismissWithAnimation();
                mRide.setOrderStatus(item);
                setupStatus();
            }

            @Override
            public void onError(String err) {
                showError(err);
                pDialog.dismissWithAnimation();
            }
        };
        setupStatus();
        requestStatus();
    }


    private void setupStatus() {
        if (mRide.getOrderStatus() == Ride.STATUS_PENDING){
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setEnabled(true);
        }else if (mRide.getOrderStatus() == Ride.STATUS_ACCEPTED){
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setEnabled(true);
        }else {

            btnCancel.setEnabled(false);
            btnCancel.setVisibility(View.GONE);
        }


        tvStatus.setText(StringUtil.getRideStatus(mRide.getOrderStatus()));
    }
    private void requestStatus(){
        btnCancel.setEnabled(false);
        showProgress();
        poolingRides.getStatusOfRide(mRide.getId(), statusRespones)
        .start();
    }

    public static Intent getIntent(Context context, Ride ride){
        Intent i = new Intent(context,RideDetailActivity.class);
        i.putExtra(ARG_RIDE,ride);
        return i;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Journey mJourney = mRide.getJourney();
        googleMap.addMarker(new MarkerOptions().position(mRide.getMeetingLocation()).icon(MapUtil.getMarkerIcon(MapUtil.ICON_MEETING_LOCATION)).title("Meeting location"));
        googleMap.addMarker(new MarkerOptions().position(mJourney.getStartPoint()).icon(MapUtil.getMarkerIcon(MapUtil.ICON_START_POINT)).title("Start point"));
        googleMap.addMarker(new MarkerOptions().position(mJourney.getEndPoint()).icon(MapUtil.getMarkerIcon(MapUtil.ICON_END_POINT)).title("End point"));
        googleMap.addPolyline(new PolylineOptions().add(mJourney.getStartPoint(),mJourney.getEndPoint()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mRide.getMeetingLocation(),DEFAULT_ZOOM));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            requestStatus();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // called when change the status(when clicked on cancel)
    @Override
    public void onResponse(Boolean changed) {
        pDialog.dismissWithAnimation();
        // status changed (rider cancelled)
        if(changed) {
            mRide.setOrderStatus(Ride.STATUS_RIDER_CANCELLED);
            setupStatus();
        }else{
            btnCancel.setEnabled(true);
            showError("Error, we couldn't change the status!");
        }
    }
    private void showProgress(){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Refreshing");
        pDialog.setCancelable(false);
        pDialog.show();
    }
    //called when there is an error in changing the status of ride ( in cancelling the Ride)
    @Override
    public void onError(String err) {
        pDialog.dismissWithAnimation();
        showError("Error, we couldn't change the status!");
    }

    private void showError(String error){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}

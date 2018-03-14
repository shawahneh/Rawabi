package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.techcamp.aauj.rawabi.API.CarpoolApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.CustomBeans.CustomJourney;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.controllers.ServiceController;
import com.techcamp.aauj.rawabi.utils.MapUtil;
import com.techcamp.aauj.rawabi.utils.StringUtil;

import java.util.ArrayList;

public class JourneyDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    CarpoolApi poolingRides = WebService.getInstance();
    public static final String ARG_JOURNEY = "journey";
    private static final float DEFAULT_ZOOM = 12;
    private Journey mJourney;
    private MapView mMapView;
    private GoogleMap mMap;
    private RecyclerView mRecyclerView;
    private Button btnCancel,btnComplete;
    TextView tvDate,tvFrom,tvTo,tvCarDesc,tvStatus;
    private int prevStatus;
    private ArrayList<Ride> mRiders;
    private  MyJourneysAdapter adapter;
    private SweetAlertDialog pDialog;
    private ICallBack<Boolean> trigerCustomJourney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_detail);

        mJourney = getIntent().getParcelableExtra(ARG_JOURNEY);
        prevStatus = mJourney.getStatus();

        mMapView = findViewById(R.id.mapView);
        mRecyclerView = findViewById(R.id.rv);
        btnCancel = findViewById(R.id.btnCancel);
        btnComplete = findViewById(R.id.btnComplete);

//        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshRiders();
//            }
//        });

        // setup buttons
        if(mJourney.getStatus() == Journey.STATUS_PENDING){
            btnCancel.setVisibility(View.VISIBLE);
            btnComplete.setVisibility(View.VISIBLE);
        }else{
            btnCancel.setVisibility(View.GONE);
            btnComplete.setVisibility(View.GONE);
        }

        trigerCustomJourney = new ICallBack<Boolean>() {
            @Override
            public void onResponse(Boolean item) {
                if(pDialog != null)
                    pDialog.dismissWithAnimation();
//                mJourney.setStatus(item.getStatus());
//                setupStatus();
//                mRiders = item.getRiders();
                updateAdapter();
                drawMarkers(mRiders);
            }

            @Override
            public void onError(String err) {
                if(pDialog != null)
                    pDialog.dismissWithAnimation();
                Toast.makeText(JourneyDetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        };

        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

         tvDate = findViewById(R.id.tvDate);
         tvFrom = findViewById(R.id.tvFrom);
         tvTo = findViewById(R.id.tvTo);
         tvCarDesc = findViewById(R.id.tvCarDesc);
         tvStatus = findViewById(R.id.tvStatus);

        tvDate.setText(mJourney.getGoingDate().toString());
        tvFrom.setText(MapUtil.getSavedAddress(this,mJourney.getStartPoint()));
        tvTo.setText(MapUtil.getSavedAddress(this,mJourney.getEndPoint()));
        tvCarDesc.setText(mJourney.getCarDescription());
        tvStatus.setText(StringUtil.getJourneyStatus(mJourney.getStatus()));
        setupStatus();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        refreshJourney();

    }

    private void setupStatus() {

        if(mJourney.getStatus() == Journey.STATUS_PENDING){
            btnComplete.setText("Complete");
            btnCancel.setVisibility(View.VISIBLE);
            btnComplete.setVisibility(View.VISIBLE);
        }else {

            btnComplete.setVisibility(View.GONE);
            btnCancel.setText("Cancelled");
            btnComplete.setEnabled(false);
            btnCancel.setEnabled(false);

            btnCancel.setVisibility(View.GONE);
            btnComplete.setText("Completed");
        }
    }

//    private void refreshRiders() {
//        mSwipeRefreshLayout.setRefreshing(true);
//        poolingRides.getRidersOfJourney(0,this);
//    }
    private void refreshJourney(){
//        showProgress("Refreshing");
        CarpoolApi api = WebService.getInstance();
//        api.getCustomJourney(mJourney.getId(),trigerCustomJourney);
//        api.getJourneyDetails(mJourney.getId(),trigerCustomJourney);
    }

    private void updateAdapter() {
        if(mRiders == null)
            return;
        adapter = new MyJourneysAdapter(mRiders);
        mRecyclerView.setAdapter(adapter);
        drawMarkers(mRiders);
    }
    private void updateRecycler() {
        if(adapter == null)
            return;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        drawMarkers(null);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mJourney.getStartPoint(),DEFAULT_ZOOM));
    }
    public void openDialog(final Ride ride,int status){


        if(status == Ride.STATUS_ACCEPTED){
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("Accept this Rider")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            acceptRider(ride);

                        }
                    })
                    .show();
        }else if(status == Ride.STATUS_DRIVER_REJECTED){
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("Reject this Rider")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            rejectRider(ride);
                        }
                    })
                    .show();
        }



    }


    private void rejectRider(final Ride ride) {
        showProgress("Rejecting");
        poolingRides.changeRideStatus(ride.getId(), Ride.STATUS_DRIVER_REJECTED, new ICallBack<Boolean>() {
            @Override
            public void onResponse(Boolean item) {
                pDialog.dismissWithAnimation();
                //rider accepted
                if(item){
//                    ServiceController.changeRideStatus(ride.getId(),Ride.STATUS_DRIVER_REJECTED);
                    ride.setOrderStatus(Ride.STATUS_DRIVER_REJECTED);
                }
                updateRecycler();
            }

            @Override
            public void onError(String err) {
            pDialog.dismissWithAnimation();

            }
        });
    }

    private void acceptRider(final Ride ride) {
        showProgress("Accepting");
        poolingRides.changeRideStatus(ride.getId(), Ride.STATUS_ACCEPTED, new ICallBack<Boolean>() {
            @Override
            public void onResponse(Boolean item) {
                if(pDialog != null)
                pDialog.dismissWithAnimation();
                //rider accepted
                if(item){
//                    ServiceController.changeRideStatus(ride.getId(),Ride.STATUS_ACCEPTED);
                    ride.setOrderStatus(Ride.STATUS_ACCEPTED);
                }
                updateRecycler();
            }

            @Override
            public void onError(String err) {
                if(pDialog != null)
                pDialog.dismissWithAnimation();
            }
        });
    }

    public void showRiderOnMap(Ride ride){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ride.getMeetingLocation(),DEFAULT_ZOOM));
    }

    private void drawMarkers(ArrayList<Ride> rides) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(mJourney.getStartPoint()).icon(MapUtil.getMarkerIcon(MapUtil.ICON_START_POINT)).title("Start point"));
        mMap.addMarker(new MarkerOptions().position(mJourney.getEndPoint()).icon(MapUtil.getMarkerIcon(MapUtil.ICON_END_POINT)).title("End point"));
        mMap.addPolyline(new PolylineOptions().add(mJourney.getStartPoint(),mJourney.getEndPoint()));
        if(rides == null)
            return;
        for (Ride r :
                rides) {
            if(r.getOrderStatus()==Ride.STATUS_ACCEPTED)
                mMap.addMarker(new MarkerOptions().position(r.getMeetingLocation()).title(r.getUser().getFullname()).icon(MapUtil.getMarkerIcon(MapUtil.ICON_RIDER_ACCEPTED))).setTag(r);
            else if(r.getOrderStatus() == Ride.STATUS_PENDING)
                mMap.addMarker(new MarkerOptions().position(r.getMeetingLocation()).title(r.getUser().getFullname()).icon(MapUtil.getMarkerIcon(MapUtil.ICON_RIDER_PENDING))).setTag(r);
            else if(r.getOrderStatus() == Ride.STATUS_CANCELLED)
                mMap.addMarker(new MarkerOptions().position(r.getMeetingLocation()).title(r.getUser().getFullname()).icon(MapUtil.getMarkerIcon(MapUtil.ICON_RIDER_CANCELLED))).setTag(r);
        }
    }
    public void changeStatus(int s){
        showProgress("Refreshing");
        WebService.getInstance().changeJourneyStatus(mJourney, s,trigerCustomJourney);
    }

    public void onClick(View view) {

        int id = view.getId();
        switch (id){
            case R.id.btnCancel:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Cancel this journey?!")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                changeStatus(Journey.STATUS_CANCELLED);
                                ServiceController.stopService(JourneyDetailActivity.this);
                            }
                        })
                        .show();


                break;
            case R.id.btnComplete:

                changeStatus(Journey.STATUS_DRIVER_CLOSED);
                break;
        }
    }

    private void showProgress(String text){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(text);
        pDialog.setCancelable(false);
        pDialog.show();
    }
    private class MyJourneysAdapter extends RecyclerView.Adapter<MyJourneysAdapter.MyJourneyHolder>{
        ArrayList<Ride> rides = new ArrayList<>();

        public MyJourneysAdapter(ArrayList<Ride> rides) {
            this.rides = rides;
        }

        @Override
        public MyJourneyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_rider, parent, false);
            return new MyJourneyHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyJourneyHolder holder, int position) {
            holder.bind(rides.get(holder.getAdapterPosition()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JourneyDetailActivity.this.showRiderOnMap(rides.get(holder.getAdapterPosition()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return rides.size();
        }

        class MyJourneyHolder extends RecyclerView.ViewHolder{
            private TextView tvName,tvRating,tvStatus;
            private Button btnAccept,btnReject;
            private RatingBar ratingBar;
            private ImageView imageView;
            public MyJourneyHolder(View itemView) {
                super(itemView);
                btnAccept = itemView.findViewById(R.id.btnAccept);
                btnReject = itemView.findViewById(R.id.btnReject);
                tvStatus = itemView.findViewById(R.id.tvStatus);
                tvName = itemView.findViewById(R.id.tvName);
                tvRating = itemView.findViewById(R.id.tvRating);
                ratingBar = itemView.findViewById(R.id.ratingBar);
                imageView = itemView.findViewById(R.id.imageView);
            }
            public void bind(final Ride ride){
                tvName.setText(ride.getUser().getFullname());
                tvStatus.setText(StringUtil.getRideStatus(ride.getOrderStatus()));
                ratingBar.setRating(ride.getUser().getRating());
                tvRating.setText(ride.getUser().getRating() + "/5");

                if(ride.getUser().getImageurl() != null)
                    Glide.with(itemView.getContext()).load(ride.getUser().getImageurl()).apply(RequestOptions.circleCropTransform()).into(imageView);
                if (ride.getOrderStatus() == Ride.STATUS_PENDING && mJourney.getStatus() == Journey.STATUS_PENDING){
                    btnAccept.setVisibility(View.VISIBLE);
                    btnReject.setVisibility(View.VISIBLE);
                    tvStatus.setVisibility(View.GONE);
                }
                else{
                    tvStatus.setVisibility(View.VISIBLE);
                    tvStatus.setText(StringUtil.getRideStatus(ride.getOrderStatus()));
                    btnAccept.setVisibility(View.GONE);
                    btnReject.setVisibility(View.GONE);
                }
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JourneyDetailActivity.this.openDialog(ride,Ride.STATUS_ACCEPTED);
                    }
                });
                btnReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JourneyDetailActivity.this.openDialog(ride,Ride.STATUS_DRIVER_REJECTED);
                    }
                });
            }
        }
    }
}

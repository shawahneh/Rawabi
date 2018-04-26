package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.Journey;
import com.techcamp.aauj.rawabi.model.Ride;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.utils.DateUtil;
import com.techcamp.aauj.rawabi.utils.MapUtil;
import com.techcamp.aauj.rawabi.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Activity for driver to show the details of his journey
 * the details are : journey start,end points car description ... etc, and the riders of this journey
 * - draw riders location on the map
 * - when click on rider the camera moves to his location
 */
public class JourneyDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    CarpoolApi carpoolApi = WebFactory.getCarpoolService();
    public static final String ARG_JOURNEY = "journey";
    private static final float DEFAULT_ZOOM = 12;
    private Journey mJourney;
    private MapView mMapView;
    private GoogleMap mMap;
    private RecyclerView mRecyclerView;
    private Button btnCancel,btnComplete; // complete is close
    private TextView tvDate,tvFrom,tvTo,tvCarDesc,tvStatus;
    private TextView tvRiders,tvPendingRidersNumber;

    private int newStatus;
    private List<Ride> mRiders;
    private  MyJourneysAdapter adapter;
    private SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_detail);

        //get selected journey
        mJourney = getIntent().getParcelableExtra(ARG_JOURNEY);
        if(mJourney == null)
            finish();

        //binding
        mMapView = findViewById(R.id.mapView);
        mRecyclerView = findViewById(R.id.rv);
        btnCancel = findViewById(R.id.btnCancel);
        btnComplete = findViewById(R.id.btnComplete);
        tvDate = findViewById(R.id.tvDate);
        tvFrom = findViewById(R.id.tvFrom);
        tvTo = findViewById(R.id.tvTo);
        tvCarDesc = findViewById(R.id.tvCarDesc);
        tvStatus = findViewById(R.id.tvStatus);
        tvPendingRidersNumber = findViewById(R.id.tvPendingRidersNumber);
        tvRiders = findViewById(R.id.tvRiders);




        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();



        tvDate.setText(DateUtil.getRelativeTime(mJourney.getGoingDate()));
        tvFrom.setText(MapUtil.getSavedAddress(this,mJourney.getStartPoint()));
        tvTo.setText(MapUtil.getSavedAddress(this,mJourney.getEndPoint()));
        tvCarDesc.setText(mJourney.getCarDescription());
        tvStatus.setText(StringUtil.getJourneyStatus(mJourney.getStatus()));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        //setup buttons
        setupStatus();

        //get riders
        refreshJourney();

    }

    /**
     * update ui(accept and close buttons) according to journey status
     */
    private void setupStatus() {

        if(mJourney.getStatus() == Journey.STATUS_PENDING){
            btnCancel.setVisibility(View.VISIBLE);
            btnComplete.setVisibility(View.VISIBLE);
            btnCancel.setEnabled(true);
            btnComplete.setEnabled(true);
        }else {

            btnComplete.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            btnComplete.setEnabled(false);
            btnCancel.setEnabled(false);
        }
    }

    /**
     * refresh riders of the journey
     * used to see the requests from riders for the journey
     */
    private void refreshJourney(){
        showProgress("Refreshing");
        CarpoolApi api = WebFactory.getCarpoolService();
        api.getRidersOfJourney(mJourney, new IListCallBack<Ride>() {
            @Override
            public void onResponse(List<Ride> item) {
                Log.d("tag","getRidersOfJourney onResponse");
                Log.d("tag","rides count="+item.size());
                pDialog.dismiss();
                mRiders = item;
                updateAdapter();
                drawMarkers(mRiders);
            }

            @Override
            public void onError(String err) {
                Log.d("tag","getRidersOfJourney onError");
                pDialog.dismiss();
                Toast.makeText(JourneyDetailActivity.this, err, Toast.LENGTH_SHORT).show();
            }
        })
        .start();
    }

    /**
     * update recycler view adapter.
     * adapter holds the riders
     */
    private void updateAdapter() {
        if(mRiders == null)
            return;
        adapter = new MyJourneysAdapter(mRiders);
        mRecyclerView.setAdapter(adapter);
        drawMarkers(mRiders);
        tvRiders.setText(mRiders.size() + " " + getString(R.string.riders));

        //get number of pending riders
        int rNumber=0;
        for (Ride ride:
             mRiders) {
            if(ride.getOrderStatus() == Ride.STATUS_PENDING)
                rNumber++;
        }
        tvPendingRidersNumber.setText(rNumber+"");

    }
    /*
        refresh data on recyclerView
     */
    private void updateRecycler() {
        if(adapter == null)
            return;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        drawMarkers(null);
        // move to journey's start point
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mJourney.getStartPoint(),DEFAULT_ZOOM));
    }

    /**
     * this method called when pressed on accept rider or reject rider to show a confirmation dialog
     * @param ride : selected rider
     * @param status : selected action : accept or reject
     */
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

    /**
     * this method called to reject a rider
     * @param ride : selected rider
     */
    private void rejectRider(final Ride ride) {
        showProgress("Rejecting");
        carpoolApi.changeRideStatus(ride.getId(), Ride.STATUS_DRIVER_REJECTED, new ICallBack<Boolean>() {
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

    /**
     * this method called to accept a rider
     * @param ride : selected rider
     */
    private void acceptRider(final Ride ride) {
        showProgress("Accepting");
        carpoolApi.changeRideStatus(ride.getId(), Ride.STATUS_ACCEPTED, new ICallBack<Boolean>() {
            @Override
            public void onResponse(Boolean success) {
                if(pDialog != null)
                pDialog.dismissWithAnimation();

                if(success){//rider accepted
                    ride.setOrderStatus(Ride.STATUS_ACCEPTED);
                    updateRecycler(); // update rider item on recycler view as (accepted)
                }else
                    onError("couldn't accept the rider");

            }

            @Override
            public void onError(String err) {
                if(pDialog != null)
                pDialog.dismissWithAnimation();
                Toast.makeText(getApplicationContext(),err,Toast.LENGTH_SHORT).show();
            }
        })
        .start();
    }

    public void showRiderOnMap(Ride ride){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ride.getMeetingLocation(),DEFAULT_ZOOM));
    }

    /**
     * this method draw a marker on the map for each rider of the journey
     * and also draw the start and end points of the journey
     * @param rides : the riders who sent request to the journey
     */
    private void drawMarkers(List<Ride> rides) {
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

    /**
     * this method calls the WebApi to change journey status
     * @param status : new status
     */
    public void changeStatus(int status){
        newStatus = status;
        showProgress("Refreshing");
        carpoolApi.changeJourneyStatus(mJourney, status, new ICallBack<Boolean>() {
            @Override
            public void onResponse(Boolean changed) {
                if(pDialog != null)
                    pDialog.dismissWithAnimation();

                if(changed){ // success
                    mJourney.setStatus(newStatus);
                    setupStatus();
                }else{
                    onError("Couldn't change the status");
                }
            }

            @Override
            public void onError(String err) {
                if(pDialog != null)
                    pDialog.dismissWithAnimation();
                Toast.makeText(JourneyDetailActivity.this, err, Toast.LENGTH_SHORT).show();
            }
        })
        .start();
    }

    /**
     * when button clicked ( cancel or close button)
     * @param view : the button
     */
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
                                changeStatus(Journey.STATUS_DRIVER_CANCELLED);// this called the api
                            }
                        })
                        .show();


                break;
            case R.id.btnComplete:

                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Close this journey?!")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                changeStatus(Journey.STATUS_DRIVER_CLOSED);// this called the api
                            }
                        })
                        .show();
                break;
        }
    }

    // to show progress dialog with text
    private void showProgress(String text){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(text);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    /**
     * this class is for riders of the journey
     */
    private class MyJourneysAdapter extends RecyclerView.Adapter<MyJourneysAdapter.MyJourneyHolder>{
        List<Ride> rides;

        public MyJourneysAdapter(List<Ride> rides) {
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
            if(rides == null) return 0;
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
                    Glide.with(itemView.getContext()).load(ride.getUser().getImageurl())
                            .apply(RequestOptions.placeholderOf(R.drawable.person))
                            .apply(RequestOptions.circleCropTransform())
                            .into(imageView);

                // when the rider is pending and journey is still pending -> you accept or reject riders
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

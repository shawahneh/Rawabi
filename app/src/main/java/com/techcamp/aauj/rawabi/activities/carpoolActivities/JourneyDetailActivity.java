package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.app.Dialog;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.techcamp.aauj.rawabi.API.PoolingJourney;
import com.techcamp.aauj.rawabi.API.PoolingRides;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.controllers.AlarmController;
import com.techcamp.aauj.rawabi.utils.MapUtil;
import com.techcamp.aauj.rawabi.utils.StringUtil;

import java.util.ArrayList;

public class JourneyDetailActivity extends AppCompatActivity implements OnMapReadyCallback,IResponeTriger<ArrayList<Ride>> {
    PoolingRides poolingRides = WebService.getInstance(this);
    public static final String ARG_JOURNEY = "journey";
    private static final float DEFAULT_ZOOM = 12;
    private Journey mJourney;
    private MapView mMapView;
    private GoogleMap mMap;
    private RecyclerView mRecyclerView;
    private Button btnCancel,btnComplete;
    TextView tvDate,tvFrom,tvTo,tvCarDesc,tvStatus;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int prevStatus;
    private ArrayList<Ride> mRiders;
    private  MyJourneysAdapter adapter;
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

        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRiders();
            }
        });

        // setup buttons
        if(mJourney.getStatus() == Journey.STATUS_PENDING){
            btnCancel.setVisibility(View.VISIBLE);
            btnComplete.setVisibility(View.VISIBLE);
        }else{
            btnCancel.setVisibility(View.GONE);
            btnComplete.setVisibility(View.GONE);
        }

        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

         tvDate = findViewById(R.id.tvDate);
         tvFrom = findViewById(R.id.tvFrom);
         tvTo = findViewById(R.id.tvTo);
         tvCarDesc = findViewById(R.id.tvCarDesc);
         tvStatus = findViewById(R.id.tvStatus);

        tvDate.setText(mJourney.getRealDate());
        tvFrom.setText(MapUtil.getAddress(this,mJourney.getStartPoint()));
        tvTo.setText(MapUtil.getAddress(this,mJourney.getEndPoint()));
        tvCarDesc.setText(mJourney.getCarDescription());
        tvStatus.setText(StringUtil.getJourneyStatus(mJourney.getStatus()));
        setupStatus();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        refreshRiders();

    }

    private void setupStatus() {

        if(mJourney.getStatus() == Journey.STATUS_PENDING){
            btnComplete.setText("Complete");
            btnCancel.setVisibility(View.VISIBLE);
            btnComplete.setVisibility(View.VISIBLE);
        }else if(mJourney.getStatus() == Journey.STATUS_CANCELLED){
            btnComplete.setVisibility(View.GONE);
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setText("Cancelled");
            btnComplete.setEnabled(false);
            btnCancel.setEnabled(false);
        }else if(mJourney.getStatus() == Journey.STATUS_COMPLETED){
            btnComplete.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.GONE);
            btnComplete.setText("Completed");
            btnComplete.setEnabled(false);
            btnCancel.setEnabled(false);
        }
    }

    private void refreshRiders() {
        mSwipeRefreshLayout.setRefreshing(true);
        poolingRides.getRidersOfJourney(0,this);
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
    public void openDialog(final Ride ride){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_two_buttons);
        Button btnAccept = dialog.findViewById(R.id.btnAccept);
        Button btnReject = dialog.findViewById(R.id.btnReject);


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptRider(ride);
                dialog.dismiss();
            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectRider(ride);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void rejectRider(final Ride ride) {
        poolingRides.changeRideStatus(ride.getId(), Ride.STATUS_DRIVER_REJECTED, new IResponeTriger<Boolean>() {
            @Override
            public void onResponse(Boolean item) {
                //rider accepted
                if(item){
                    ride.setOrderStatus(Ride.STATUS_DRIVER_REJECTED);
                }
                if(adapter != null)
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String err) {

            }
        });
    }

    private void acceptRider(final Ride ride) {
        poolingRides.changeRideStatus(ride.getId(), Ride.STATUS_ACCEPTED, new IResponeTriger<Boolean>() {
            @Override
            public void onResponse(Boolean item) {
                //rider accepted
                if(item){
                    ride.setOrderStatus(Ride.STATUS_ACCEPTED);
                }
                if(adapter != null)
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String err) {

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
        tvStatus.setText("...");
        mSwipeRefreshLayout.setRefreshing(true);
        WebService.getInstance(this).changeJourneyStatusAndGetRiders(mJourney, s,this);
    }

    public void onClick(View view) {
        ViewGroup viewGroup = (ViewGroup)findViewById(R.id.layout_container);
        int id = view.getId();
        switch (id){
            case R.id.btnCancel:
                btnCancel.setEnabled(false);
                btnComplete.setEnabled(false);
                TransitionManager.beginDelayedTransition(viewGroup);
                btnComplete.setVisibility(View.GONE);
                btnCancel.setText("Cancelling..");
                mJourney.setStatus(Journey.STATUS_CANCELLED);
                updateRecycler();
                changeStatus(Journey.STATUS_CANCELLED);
                break;
            case R.id.btnComplete:
                btnCancel.setEnabled(false);
                btnComplete.setEnabled(false);
                TransitionManager.beginDelayedTransition(viewGroup);
                btnCancel.setVisibility(View.GONE);
                btnComplete.setText("Completing..");
                mJourney.setStatus(Journey.STATUS_COMPLETED);
                updateRecycler();
                changeStatus(Journey.STATUS_COMPLETED);
                break;
        }
    }

    @Override
    public void onResponse(final ArrayList<Ride> item) {
        // update riders
        if(mJourney.getStatus() == Journey.STATUS_CANCELLED){
            AlarmController.cancelAlarm(this,mJourney);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setupStatus();
                mRiders = item;
                mSwipeRefreshLayout.setRefreshing(false);
                updateAdapter();
                drawMarkers(mRiders);
            }
        });
    }

    @Override
    public void onError(String err) {
        mJourney.setStatus(prevStatus);
        setupStatus();
        updateAdapter();
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
            private TextView tvName,tvRating;
            private Button btnStatus;
            private RatingBar ratingBar;
            private ImageView imageView;
            public MyJourneyHolder(View itemView) {
                super(itemView);
                btnStatus = itemView.findViewById(R.id.btnStatus);
                tvName = itemView.findViewById(R.id.tvName);
                tvRating = itemView.findViewById(R.id.tvRating);
                ratingBar = itemView.findViewById(R.id.ratingBar);
                imageView = itemView.findViewById(R.id.imageView);
            }
            public void bind(final Ride ride){
                tvName.setText(ride.getUser().getFullname());
                btnStatus.setText(StringUtil.getRideStatus(ride.getOrderStatus()));
                ratingBar.setRating(ride.getUser().getRating());
                tvRating.setText(ride.getUser().getRating() + "/5");

                if(ride.getUser().getImageurl() != null)
                    Glide.with(itemView.getContext()).load(ride.getUser().getImageurl()).apply(RequestOptions.circleCropTransform()).into(imageView);
                if (ride.getOrderStatus() == Ride.STATUS_PENDING && mJourney.getStatus() == Journey.STATUS_PENDING)
                    btnStatus.setEnabled(true);
                else
                    btnStatus.setEnabled(false);
                btnStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnStatus.setText("..");
                        JourneyDetailActivity.this.openDialog(ride);
                    }
                });
            }
        }
    }
}

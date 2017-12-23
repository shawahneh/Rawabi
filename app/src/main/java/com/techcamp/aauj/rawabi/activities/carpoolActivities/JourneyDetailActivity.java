package com.techcamp.aauj.rawabi.activities.carpoolActivities;

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
import android.widget.TextView;

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

    private void updateRecycler() {
        if(mRiders == null)
            return;
        MyJourneysAdapter adapter = new MyJourneysAdapter(mRiders);
        mRecyclerView.setAdapter(adapter);
        drawMarkers(mRiders);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        drawMarkers(null);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mJourney.getStartPoint(),DEFAULT_ZOOM));
    }
    public void acceptRider(Ride ride){

    }
    public void showRiderOnMap(Ride ride){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ride.getMeetingLocation(),DEFAULT_ZOOM));
    }

    private void drawMarkers(ArrayList<Ride> rides) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(mJourney.getStartPoint()).title("Start point"));
        mMap.addMarker(new MarkerOptions().position(mJourney.getEndPoint()).title("End point"));
        mMap.addPolyline(new PolylineOptions().add(mJourney.getStartPoint(),mJourney.getEndPoint()));
        if(rides == null)
            return;
        for (Ride r :
                rides) {
            if(r.getOrderStatus()==Ride.STATUS_ACCEPTED)
                mMap.addMarker(new MarkerOptions().position(r.getMeetingLocation()).title(r.getUser().getFullname()).icon(MapUtil.getMarkerIcon("#63d25d"))).setTag(r);
            else if(r.getOrderStatus() == Ride.STATUS_PENDING)
                mMap.addMarker(new MarkerOptions().position(r.getMeetingLocation()).title(r.getUser().getFullname()).icon(MapUtil.getMarkerIcon("#21b5cc"))).setTag(r);
            else if(r.getOrderStatus() == Ride.STATUS_CANCELLED)
                mMap.addMarker(new MarkerOptions().position(r.getMeetingLocation()).title(r.getUser().getFullname()).icon(MapUtil.getMarkerIcon("#cf0000"))).setTag(r);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setupStatus();
                mRiders = item;
                mSwipeRefreshLayout.setRefreshing(false);
                updateRecycler();
                drawMarkers(mRiders);
            }
        });
    }

    @Override
    public void onError(String err) {
        mJourney.setStatus(prevStatus);
        setupStatus();
        updateRecycler();
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
            private TextView tvName;
            private Button btnStatus;
            public MyJourneyHolder(View itemView) {
                super(itemView);
                btnStatus = itemView.findViewById(R.id.btnStatus);
                tvName = itemView.findViewById(R.id.tvName);
            }
            public void bind(final Ride ride){
                tvName.setText(ride.getUser().getFullname());
                btnStatus.setText(StringUtil.getRideStatus(ride.getOrderStatus()));

                if (ride.getOrderStatus() == Ride.STATUS_PENDING && mJourney.getStatus() == Journey.STATUS_PENDING)
                    btnStatus.setEnabled(true);
                else
                    btnStatus.setEnabled(false);
                btnStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JourneyDetailActivity.this.acceptRider(ride);
                    }
                });
            }
        }
    }
}

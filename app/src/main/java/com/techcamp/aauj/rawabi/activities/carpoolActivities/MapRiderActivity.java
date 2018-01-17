package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.techcamp.aauj.rawabi.API.PoolingJourney;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.MapActivity;
import com.techcamp.aauj.rawabi.utils.MapUtil;
import com.techcamp.aauj.rawabi.utils.NumberUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alaam on 12/16/2017.
 */

public class MapRiderActivity extends MapActivity {
    //mMap
    private PoolingJourney webApi = WebApi.getInstance(this);
    private Date mDateRiding;
    private DriverDetailLayout mDriverDetailLayout;
    private ArrayList<Journey> mJourneys;
    private Polyline mPolyline;
    private Marker mMarkerDist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SlidingUpPanelLayout slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        MapUtil.getCurrentLoc(this,null);

        mDriverDetailLayout = new DriverDetailLayout();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_map_rider;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getTag() instanceof Journey) {
                    mDriverDetailLayout.show(null, (Journey) marker.getTag());
                    drawDestination((Journey) marker.getTag());
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                }
                marker.showInfoWindow();
                return true;
            }
        });

    }

    private void drawDestination(final Journey mJourney) {

        if(mPolyline != null)
            mPolyline.remove();

        mPolyline = mMap.addPolyline(new PolylineOptions().add(mJourney.getStartPoint(),mJourney.getEndPoint()));


        if(mMarkerDist != null)
        mMarkerDist.remove();

            mMarkerDist = mMap.addMarker(new MarkerOptions().position(mJourney.getEndPoint()).title(mJourney.getUser().getFullname()).snippet("End point"));

        mPolyline.setVisible(true);
        mMarkerDist.setVisible(true);
    }

    @Override
    public void pressFromMode(View view) {
        super.pressFromMode(view);

        getJourneys();
    }

    @Override
    public void pressToMode(View view) {
        super.pressToMode(view);

        getJourneys();
    }

    @Override
    public void pressTime(View view) {
        super.pressTime(view);


    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        super.onTimeSet(timePicker, i, i1);
        mDateRiding = new Date();
        mDateRiding.setHours(i);
        mDateRiding.setMinutes(i1);

        getJourneys();
    }

    private void getJourneys(){
        if(mMarkerFrom == null || mMarkerTo == null || mDateRiding == null)
            return;

        startLoading(true);
        mDriverDetailLayout.hide();

        webApi.filterJourneys(mMarkerFrom.getPosition(), mMarkerTo.getPosition(), mDateRiding, 0, new IResponeTriger<ArrayList<Journey>>() {
            @Override
            public void onResponse(final ArrayList<Journey> item) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mJourneys = item;
                        startLoading(false);
                        drawJourneys(mJourneys);
                    }
                });

            }

            @Override
            public void onError(String err) {
                startLoading(false);
            }
        });

    }

    private void drawJourneys(ArrayList<Journey> arrayList) {
        if(mMap == null)
            return;

        mMap.clear();

        drawMarkers();
        drawPolyline();
        for (Journey j :
                arrayList) {
        mMap.addMarker(new MarkerOptions()
            .position(j.getStartPoint())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car2))
                .title(j.getUser().getFullname())
                .snippet(j.getRealTime())
        ).setTag(j);


        }
    }



    class DriverDetailLayout{
        private Journey mJourney;
        SlidingUpPanelLayout slidingUpPanelLayout;
        public DriverDetailLayout() {
            slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        }
        public void hide(){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
        private void show(Bundle save, Journey journey){
            this.mJourney = journey;


            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            TextView mTextViewName = findViewById(R.id.tvName);
            TextView mTextViewPhone =findViewById(R.id.tvPhone);
            TextView mTextViewAvilable =findViewById(R.id.tvAvailable);
            TextView mTextViewDistance =findViewById(R.id.tvDistance);
            TextView mTextViewTo = findViewById(R.id.tvTo);
            TextView mTextViewFrom =findViewById(R.id.tvFrom);
            TextView mTextViewCarDesc=findViewById(R.id.tvCarDesc);
            Button mButtonCancel = findViewById(R.id.btnCancel);
            Button mButtonChoose = findViewById(R.id.btnChoose);
            Button mButtonClose = findViewById(R.id.btnClose);

            ImageView mImageView = findViewById(R.id.profile_image);


            User user = mJourney.getUser();
            if(user.getImageurl() != null)
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(mImageView);

            mTextViewName.setText(user.getFullname());
            mTextViewAvilable.setText(mJourney.getRealDate());
            mTextViewCarDesc.setText(mJourney.getCarDescription());
            mTextViewPhone.setText(user.getPhone());
            double distance = MapUtil.getDistance(mJourney.getStartPoint(),mMarkerFrom.getPosition());
            mTextViewDistance.setText(NumberUtil.getDistanceString(distance));

            mButtonChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MapRiderActivity.this, MeetingMapActivity.class);
                    i.putExtra("journey",mJourney);
                    startActivity(i);
                }
            });
            mButtonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            });
            mButtonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hide();
                    if(mPolyline != null)
                    mPolyline.setVisible(false);
                    if(mMarkerDist != null)
                    mMarkerDist.setVisible(false);
                }
            });

        }

    }

}

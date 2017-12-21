package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.techcamp.aauj.rawabi.API.PoolingJourney;
import com.techcamp.aauj.rawabi.API.PoolingPlace;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.MeetingMapActivity;
import com.techcamp.aauj.rawabi.utils.MapUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by alaam on 12/16/2017.
 */

public class MapRiderActivity extends MapActivity {
    //mMap
    private PoolingJourney webApi = WebApi.getInstance(this);
    private Date mDateRiding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SlidingUpPanelLayout slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        MapUtil.getCurrentLoc(this,null);
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
                if(marker.getTag() instanceof Journey)
                    new DriverDetailLayout((Journey) marker.getTag()).show();
                marker.showInfoWindow();
                return true;
            }
        });
    }

    @Override
    protected void pressFromMode(View view) {
        super.pressFromMode(view);

        getJourneys();
    }

    @Override
    protected void pressToMode(View view) {
        super.pressToMode(view);

        getJourneys();
    }

    @Override
    protected void pressTime(View view) {
        super.pressTime(view);

        getJourneys();
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

        webApi.filterJourneys(mMarkerFrom.getPosition(), mMarkerTo.getPosition(), mDateRiding, 0, new IResponeTriger<ArrayList<Journey>>() {
            @Override
            public void onResponse(final ArrayList<Journey> item) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startLoading(false);
                        drawJourneys(item);
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
        for (Journey j :
                arrayList) {
        mMap.addMarker(new MarkerOptions()
            .position(j.getStartPoint())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car2))
                .title(j.getUser().getFullname())
                .snippet(j.getRealDate())
        ).setTag(j);


        }
    }

    class DriverDetailLayout{
        private Journey mJourney;

        public DriverDetailLayout(Journey mJourney) {
            this.mJourney = mJourney;
        }
        private void show(){
            SlidingUpPanelLayout slidingUpPanelLayout = findViewById(R.id.sliding_layout);
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            TextView mTextViewName = findViewById(R.id.tvName);
            TextView mTextViewPhone =findViewById(R.id.tvPhone);
            TextView mTextViewAvilable =findViewById(R.id.tvAvailable);
            TextView mTextViewDistance =findViewById(R.id.tvDistance);
            TextView mTextViewTo = findViewById(R.id.tvTo);
            TextView mTextViewFrom =findViewById(R.id.tvFrom);
            TextView mTextViewCarDesc=findViewById(R.id.tvCarDesc);
            TextView mButtonCancel = findViewById(R.id.btnCancel);
            TextView mButtonChoose = findViewById(R.id.btnChoose);
            ImageView mImageView = findViewById(R.id.profile_image);


            User user = mJourney.getUser();
            if(user.getImageurl() != null)
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(mImageView);

            mTextViewName.setText(user.getFullname());
            mTextViewAvilable.setText(mJourney.getRealDate());
            mTextViewCarDesc.setText(mJourney.getCarDescription());
            mTextViewPhone.setText(user.getPhone());
            mTextViewDistance.setText(MapUtil.getDistance(mJourney.getStartPoint(),mMarkerFrom.getPosition()) +" meter");

            mButtonChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MapRiderActivity.this, MeetingMapActivity.class);
                    i.putExtra("journey",mJourney);
                    startActivity(i);
                }
            });

        }

    }

}

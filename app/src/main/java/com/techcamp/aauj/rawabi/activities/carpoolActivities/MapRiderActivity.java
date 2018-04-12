package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.techcamp.aauj.rawabi.API.CarpoolApi;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.controllers.SPController;
import com.techcamp.aauj.rawabi.model.Journey;
import com.techcamp.aauj.rawabi.model.User;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.MapActivity;
import com.techcamp.aauj.rawabi.utils.MapUtil;
import com.techcamp.aauj.rawabi.utils.NumberUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;

/**
 * Created by ALa on 12/16/2017.
 */

public class MapRiderActivity extends MapActivity {
    //mMap
    private final int CODE_SELECT_DRIVER =11;
    private CarpoolApi webApi = WebFactory.getCarpoolService();
    private Date mDateRiding;
    private DriverDetailLayout mDriverDetailLayout;
    private List<Journey> mJourneys;
    private Polyline mPolyline;
    private Marker mMarkerDist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SlidingUpPanelLayout slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        MapUtil.getCurrentLoc(this,null);

        mDriverDetailLayout = new DriverDetailLayout();


        showTourGuide(0 /*  start point view  */);
    }

    private void showTourGuide(int startNumber) {
        // TODO: 4/11/2018 enable these 3 lines after testing
        /*
        String id = SPController.getString(this,"tourGuid");
        if(id != null)
            return;
*/
        switch (startNumber){
            case 0: // for start button
                new GuideView.Builder(this)
                        .setTitle("Start point address")
                        .setContentText("Guide Description Text\n .....Guide Description Text\n .....Guide Description Text .....")
                        .setGravity(GuideView.Gravity.auto) //optional
                        .setDismissType(GuideView.DismissType.anywhere) //optional - default GuideView.DismissType.targetView
                        .setTargetView(btnSetStart)
                        .setContentTextSize(12)//optional
                        .setTitleTextSize(14)//optional
                        .setGuideListener(new GuideView.GuideListener() {
                            @Override
                            public void onDismiss(View view) {
                                showTourGuide(1);
                            }
                        })
                        .build()
                        .show();
                break;
            case 1: // for end button
                new GuideView.Builder(this)
                        .setTitle("End point address")
                        .setContentText("Guide Description Text\n .....Guide Description Text\n .....Guide Description Text .....")
                        .setGravity(GuideView.Gravity.auto) //optional
                        .setDismissType(GuideView.DismissType.anywhere) //optional - default GuideView.DismissType.targetView
                        .setTargetView(btnSetEnd)
                        .setContentTextSize(12)//optional
                        .setTitleTextSize(14)//optional
                        .setGuideListener(new GuideView.GuideListener() {
                            @Override
                            public void onDismiss(View view) {
                                showTourGuide(2);
                            }
                        })
                        .build()
                        .show();
                break;
            case 2: // for time button
                new GuideView.Builder(this)
                        .setTitle("Time for travelling")
                        .setContentText("Guide Description Text\n .....Guide Description Text\n .....Guide Description Text .....")
                        .setGravity(GuideView.Gravity.auto) //optional
                        .setDismissType(GuideView.DismissType.anywhere) //optional - default GuideView.DismissType.targetView
                        .setTargetView(btnSetTime)
                        .setContentTextSize(12)//optional
                        .setTitleTextSize(14)//optional
                        .setGuideListener(new GuideView.GuideListener() {
                            @Override
                            public void onDismiss(View view) {
                                SPController.saveString(MapRiderActivity.this,"tourGuid","1");
                            }
                        })
                        .build()
                        .show();
                break;
        }

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
                    selectDriver((Journey) marker.getTag());
                }
                marker.showInfoWindow();
                return true;
            }
        });

    }
    private void selectDriver(Journey journey){
        mDriverDetailLayout.show(null, journey);
        drawDestination(journey);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(journey.getStartPoint()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == CODE_SELECT_DRIVER){
            Journey journey = data.getParcelableExtra("data");
            selectDriver(journey);
        }
    }

    @Override
    protected void onMessageClicked() {
        if(mJourneys != null &&  mJourneys.size() > 0){

        Intent i = DriversListActivity.getIntent(this,mJourneys);
        startActivityForResult(i,CODE_SELECT_DRIVER);
        }
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

        webApi.filterJourneys(mMarkerFrom.getPosition(), mMarkerTo.getPosition(), mDateRiding, 0, new IListCallBack<Journey>() {
            @Override
            public void onResponse(List<Journey> item) {
                        mJourneys = item;
                        startLoading(false);
                        drawJourneys(mJourneys);
                        if(mJourneys.size() > 0)
                            showMessage("There are " + mJourneys.size() + " " + "Drivers available");
                        else
                            showMessage("No drivers available");
            }

            @Override
            public void onError(String err) {
                startLoading(false);
            }
        });

    }

    private void drawJourneys(List<Journey> arrayList) {
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
            mTextViewAvilable.setText(mJourney.getGoingDate().toString());
            mTextViewCarDesc.setText(mJourney.getCarDescription());
            mTextViewPhone.setText(user.getPhone());
            Log.d("tag","user.getPhone()="+user.getPhone());
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

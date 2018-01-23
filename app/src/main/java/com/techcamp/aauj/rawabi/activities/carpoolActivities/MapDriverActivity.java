package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.techcamp.aauj.rawabi.API.PoolingJourney;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.MapActivity;
import com.techcamp.aauj.rawabi.controllers.AlarmController;
import com.techcamp.aauj.rawabi.controllers.SPController;
import com.techcamp.aauj.rawabi.controllers.ServiceController;

import java.util.Date;

/**
 * Created by alaam on 12/22/2017.
 */

public class MapDriverActivity extends MapActivity implements IResponeTriger<Integer>{
    private PoolingJourney poolingJourney = WebService.getInstance(this);
    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private Date mDateDriving;
    private Button btnContinue,btnCreateJourney;
    private EditText txtSeatsNumber,txtCarDesc;
    private Journey mJourney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSlidingUpPanelLayout = findViewById(R.id.sliding_layout);
        btnContinue = findViewById(R.id.btnContinue);
        btnCreateJourney = findViewById(R.id.btnCreateJourney);
        txtSeatsNumber = findViewById(R.id.txtSeatsNumber);
        txtCarDesc = findViewById(R.id.txtCarDesc);
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });
        btnCreateJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createJourney();
            }
        });
    }

    private void createJourney() {
        if(!validate())
        {
            showError("please enter valid data");
            return;
        }
        int sNumber = Integer.parseInt(txtSeatsNumber.getText().toString());
        String carDesc = txtCarDesc.getText().toString();
        User user = SPController.getLocalUser(this);

        mJourney = new Journey();
        mJourney.setStatus(Journey.STATUS_PENDING);
        mJourney.setSeats(sNumber);
        mJourney.setUser(user);
        mJourney.setCarDescription(carDesc);
        mJourney.setStartPoint(mMarkerFrom.getPosition());
        mJourney.setEndPoint(mMarkerTo.getPosition());
        mJourney.setGoingDate(mDateDriving);

        btnCreateJourney.setEnabled(false);
        poolingJourney.setNewJourney(mJourney,this);
    }

    private boolean validate() {
        return !(TextUtils.isEmpty(txtSeatsNumber.getText()) || TextUtils.isEmpty(txtCarDesc.getText()));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_map_driver;
    }

    @Override
    public void pressFromMode(View view) {
        super.pressFromMode(view);
        openPanel();
    }
    @Override
    public void pressToMode(View view) {
        super.pressToMode(view);
        openPanel();
    }

    @Override
    public void pressTime(View view) {
        super.pressTime(view);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        super.onTimeSet(timePicker, i, i1);
        mDateDriving = new Date();
        mDateDriving.setHours(i);
        mDateDriving.setMinutes(i1);
        openPanel();
    }
    private void openPanel() {
        if(mMarkerFrom == null || mMarkerTo == null || mDateDriving == null)
            return;
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    public void onResponse(Integer item) {
        // journey created
        if(item > 0){
//            AlarmController.addAlarm(this,mJourney);
            ServiceController.createJourney(this,item);
            Intent i = new Intent(this,JourneyDetailActivity.class);
            i.putExtra(JourneyDetailActivity.ARG_JOURNEY,mJourney);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onError(String err) {

    }
    private void showError(String error){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}

package com.techcamp.aauj.rawabi.activities.basicActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.model.Transportation;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.BasicActivity;
import com.techcamp.aauj.rawabi.fragments.TransportationPageFragment;

/**
 * Created by ALa on 1/1/2018.
 */

public class TransportationActivity extends BasicActivity<Transportation> {

    @Override
    protected int getImage() {
        return R.drawable.bus_notexture;
    }

    @Override
    protected String getBarTitle() {
        return "Transportation";
    }

    @Override
    public Fragment getFragment() {
        return new TransportationPageFragment();
    }
}

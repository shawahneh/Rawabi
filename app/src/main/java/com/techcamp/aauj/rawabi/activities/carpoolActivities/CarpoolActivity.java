package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.BasicActivity;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.CarpoolMainFragment;

/**
 * Created by ALa on 3/22/2018.
 */

public class CarpoolActivity extends BasicActivity {
    @Override
    protected int getImage() {
        return R.drawable.car_notexture;
    }

    @Override
    protected String getBarTitle() {
        return "Carpool";
    }

    @Override
    public Fragment getFragment() {
        return new CarpoolMainFragment();
    }
}
package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.BasicActivity;
import com.techcamp.aauj.rawabi.activities.abstractActivities.EmptyActivity;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.CarpoolMainFragment;

/**
 * Created by alaam on 2/9/2018.
 */

public class CarpoolMainActivity extends EmptyActivity {
//    @Override
//    protected int getImage() {
//        return R.drawable.car_notexture;
//    }

    @Override
    protected String getBarTitle() {
        return "Carpool";
    }

    @Override
    public Fragment getFragment() {
        return new CarpoolMainFragment();
    }
//
//    @Override
//    protected int getImageBackground() {
//        return R.drawable.city;
//    }
//
//    @Override
//    protected int getImageTop() {
//        return R.drawable.car_notexture;
//    }
}

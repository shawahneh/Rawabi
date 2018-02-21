package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.activities.abstractActivities.EmptyActivity;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.MyRidesListFragment;

/**
 * Created by alaam on 2/18/2018.
 */

public class MyRidesListActivity extends EmptyActivity {
    @Override
    protected String getBarTitle() {
        return "My Rides";
    }

    @Override
    public Fragment getFragment() {
        return new MyRidesListFragment();
    }
}

package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.activities.abstractActivities.EmptyActivity;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.MyJourneysListFragment;

/**
 * Created by alaam on 2/18/2018.
 */

public class MyJourneysListActivity extends EmptyActivity {
    @Override
    protected String getBarTitle() {
        return "My Journeys";
    }

    @Override
    public Fragment getFragment() {
        return new MyJourneysListFragment();
    }
}

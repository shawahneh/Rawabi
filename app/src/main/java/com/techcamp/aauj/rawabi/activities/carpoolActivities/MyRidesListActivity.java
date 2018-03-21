package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.model.Ride;
import com.techcamp.aauj.rawabi.activities.abstractActivities.EmptyActivity;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.MyRidesListFragment;

/**
 * Created by ALa on 2/18/2018.
 */

public class MyRidesListActivity extends EmptyActivity implements ListFragment.IFragmentListener<Ride> {
    @Override
    protected String getBarTitle() {
        return "My Rides";
    }

    @Override
    public Fragment getFragment() {
        return new MyRidesListFragment();
    }

    @Override
    public void onItemClicked(Ride item) {
        Intent i = RideDetailActivity.getIntent(this,item);
        startActivity(i);
    }
}

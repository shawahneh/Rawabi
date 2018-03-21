package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.model.Journey;
import com.techcamp.aauj.rawabi.activities.abstractActivities.EmptyActivity;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.MyJourneysListFragment;

/**
 * Created by ALa on 2/18/2018.
 */

public class MyJourneysListActivity extends EmptyActivity implements ListFragment.IFragmentListener<Journey> {
    @Override
    protected String getBarTitle() {
        return "My Journeys";
    }

    @Override
    public Fragment getFragment() {
        return new MyJourneysListFragment();
    }

    @Override
    public void onItemClicked(Journey item) {
        Intent i = new Intent(this,JourneyDetailActivity.class);
        i.putExtra(JourneyDetailActivity.ARG_JOURNEY,item);
        startActivity(i);
    }
}

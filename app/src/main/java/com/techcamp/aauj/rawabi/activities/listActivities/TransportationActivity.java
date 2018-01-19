package com.techcamp.aauj.rawabi.activities.listActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.Beans.Transportation;
import com.techcamp.aauj.rawabi.activities.abstractActivities.BasicActivity;
import com.techcamp.aauj.rawabi.activities.abstractActivities.EmptyActivity;
import com.techcamp.aauj.rawabi.activities.abstractActivities.List2Activity;
import com.techcamp.aauj.rawabi.activities.abstractActivities.ScrollingActivity;
import com.techcamp.aauj.rawabi.fragments.HomeFragment;
import com.techcamp.aauj.rawabi.fragments.TransportationPageFragment;
import com.techcamp.aauj.rawabi.fragments.listFragments.ListFragment;

/**
 * Created by alaam on 1/1/2018.
 */

public class TransportationActivity extends BasicActivity<Transportation> {

    @Override
    protected String getBarTitle() {
        return "Transportation";
    }

    @Override
    public Fragment getFragment() {
        return new TransportationPageFragment();
    }
}

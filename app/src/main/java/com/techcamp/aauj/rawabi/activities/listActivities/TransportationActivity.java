package com.techcamp.aauj.rawabi.activities.listActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.Beans.Transportation;
import com.techcamp.aauj.rawabi.fragments.TransportationPageFragment;

/**
 * Created by alaam on 1/1/2018.
 */

public class TransportationActivity extends List2Activity<Transportation> {
    @Override
    public Fragment getFragment() {
        return new TransportationPageFragment();
    }
}

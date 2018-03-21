package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.activities.abstractActivities.EmptyNoBarActivity;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.ProfileFragment;

public class ProfileActivity extends EmptyNoBarActivity {
    @Override
    public Fragment getFragment() {
        return new ProfileFragment();
    }
}

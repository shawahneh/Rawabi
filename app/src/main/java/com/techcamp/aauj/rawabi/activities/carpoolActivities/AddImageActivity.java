package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.activities.abstractActivities.EmptyActivity;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.ImageProfileFragment;

public class AddImageActivity extends EmptyActivity implements ImageProfileFragment.IListener {
    @Override
    protected String getBarTitle() {
        return "Add Image";
    }

    @Override
    public Fragment getFragment() {
        return new ImageProfileFragment();
    }

    @Override
    public void onFragmentImageProfileSkipClick() {
        Intent i = new Intent(this, CarpoolMainActivity.class);
        startActivity(i);
        finish();
    }
}

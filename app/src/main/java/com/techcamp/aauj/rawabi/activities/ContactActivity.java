package com.techcamp.aauj.rawabi.activities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.activities.abstractActivities.EmptyActivity;
import com.techcamp.aauj.rawabi.fragments.ContactFragment;

public class ContactActivity extends EmptyActivity {
    @Override
    protected String getBarTitle() {
        return "Contact us";
    }

    @Override
    public Fragment getFragment() {
        return new ContactFragment();
    }
}

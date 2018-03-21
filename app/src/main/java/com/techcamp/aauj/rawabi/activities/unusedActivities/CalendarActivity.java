package com.techcamp.aauj.rawabi.activities.unusedActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.model.Event;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.BasicActivity;
import com.techcamp.aauj.rawabi.fragments.CalendarPageFragment;

/**
 * Created by ALa on 12/31/2017.
 */

public class CalendarActivity extends BasicActivity<Event> {

    @Override
    protected int getImage() {
        return R.drawable.media_notexture;
    }

    @Override
    protected String getBarTitle() {
        return "Calendar";
    }

    @Override
    public Fragment getFragment() {
        return CalendarPageFragment.newInstance();
    }
}

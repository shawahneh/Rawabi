package com.techcamp.aauj.rawabi.activities.listActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.activities.abstractActivities.EmptyActivity;
import com.techcamp.aauj.rawabi.activities.abstractActivities.List2Activity;
import com.techcamp.aauj.rawabi.fragments.CalendarPageFragment;

/**
 * Created by alaam on 12/31/2017.
 */

public class CalendarActivity extends EmptyActivity<Event> {

    @Override
    protected String getBarTitle() {
        return "Calendar";
    }

    @Override
    public Fragment getFragment() {
        return CalendarPageFragment.newInstance();
    }
}

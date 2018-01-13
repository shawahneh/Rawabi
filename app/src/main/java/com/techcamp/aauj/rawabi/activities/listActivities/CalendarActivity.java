package com.techcamp.aauj.rawabi.activities.listActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.fragments.CalendarPageFragment;

/**
 * Created by alaam on 12/31/2017.
 */

public class CalendarActivity extends List2Activity<Event> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Calendar");
    }

    @Override
    public Fragment getFragment() {
        return CalendarPageFragment.newInstance();
    }
}

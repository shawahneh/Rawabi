package com.techcamp.aauj.rawabi.activities.basicActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.model.Event;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.BasicActivity;
import com.techcamp.aauj.rawabi.fragments.ItemDetailsFragments.EventDetailsFragment;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.fragments.listFragments.EventsListFragment;

/**
 * Created by ALa on 2/2/2018.
 */

public class EventsListActivity extends BasicActivity<Event> implements ListFragment.IFragmentListener<Event> {
    @Override
    protected int getImage() {
        return R.drawable.ic_events_new_50dp;
    }

    @Override
    protected String getBarTitle() {
        return "Events";
    }

    @Override
    public Fragment getFragment() {
        return EventsListFragment.newInstance();
    }

    @Override
    public void onItemClicked(Event item) {
        replaceFragment(EventDetailsFragment.newInstance(item));
    }
}

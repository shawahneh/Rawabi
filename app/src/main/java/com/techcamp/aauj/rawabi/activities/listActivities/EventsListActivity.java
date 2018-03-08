package com.techcamp.aauj.rawabi.activities.listActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.Beans.Job;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.BasicActivity;
import com.techcamp.aauj.rawabi.fragments.ItemDetailsFragments.EventDetailsFragment;
import com.techcamp.aauj.rawabi.fragments.ItemDetailsFragments.JobDetailsFragment;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.fragments.listFragments.EventsListFragment;

/**
 * Created by alaam on 2/2/2018.
 */

public class EventsListActivity extends BasicActivity<Event> implements ListFragment.IFragmentListener<Event> {
    @Override
    protected int getImage() {
        return R.drawable.events;
    }

    @Override
    protected String getBarTitle() {
        return "Events";
    }

    @Override
    public Fragment getFragment() {
        return EventsListFragment.newInstance(1);
    }

    @Override
    public void onItemClicked(Event item) {
        replaceFragment(EventDetailsFragment.newInstance(item));
    }
}

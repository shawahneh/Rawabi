package com.techcamp.aauj.rawabi.activities.listActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.Beans.Job;
import com.techcamp.aauj.rawabi.activities.abstractActivities.BasicActivity;
import com.techcamp.aauj.rawabi.activities.abstractActivities.EmptyActivity;
import com.techcamp.aauj.rawabi.activities.abstractActivities.List2Activity;
import com.techcamp.aauj.rawabi.activities.abstractActivities.ScrollingActivity;
import com.techcamp.aauj.rawabi.fragments.listFragments.JobsListFragment;

/**
 * Created by alaam on 12/31/2017.
 */

public class JobsListActivity extends BasicActivity<Job> {

    @Override
    protected String getBarTitle() {
        return "Jobs";
    }

    @Override
    public Fragment getFragment() {
        return JobsListFragment.newInstance(1);
    }
}

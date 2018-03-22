package com.techcamp.aauj.rawabi.activities.basicActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.model.Job;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.BasicActivity;
import com.techcamp.aauj.rawabi.fragments.ItemDetailsFragments.JobDetailsFragment;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.fragments.listFragments.JobsListFragment;

/**
 * Created by ALa on 12/31/2017.
 */

public class JobsListActivity extends BasicActivity<Job> implements ListFragment.IFragmentListener<Job> {

    @Override
    protected int getImage() {
       return R.drawable.job_notexture;
    }

    @Override
    protected String getBarTitle() {
        return "JOBS";
    }

    @Override
    public Fragment getFragment() {
        return JobsListFragment.newInstance();
    }
    @Override
    public void onItemClicked(Job item) {
        replaceFragment(JobDetailsFragment.newInstance(item));
    }
}

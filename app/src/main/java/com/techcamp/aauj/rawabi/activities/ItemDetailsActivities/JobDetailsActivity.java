package com.techcamp.aauj.rawabi.activities.ItemDetailsActivities;

import android.content.Context;
import android.content.Intent;

import com.techcamp.aauj.rawabi.Beans.Announcement;
import com.techcamp.aauj.rawabi.Beans.Job;
import com.techcamp.aauj.rawabi.activities.abstractActivities.ScrollingActivity;
import com.techcamp.aauj.rawabi.fragments.ItemDetailsFragments.ItemDetailsFragment;
import com.techcamp.aauj.rawabi.fragments.ItemDetailsFragments.JobDetailsFragment;

/**
 * Created by alaam on 1/15/2018.
 */

public class JobDetailsActivity extends ScrollingActivity<Job> {
    public static Intent getIntent(Context ctx, Job job){
        Intent intent = new Intent(ctx,JobDetailsActivity.class);
        intent.putExtra(ScrollingActivity.ARG_ITEM_ID,job);
        return intent;
    }
    @Override
    protected void onFabClicked() {

    }

    @Override
    protected String getBarTitle() {
        return "Job";
    }

    @Override
    protected ItemDetailsFragment getFragment() {
        return JobDetailsFragment.newInstance(mItem);
    }
}

package com.techcamp.aauj.rawabi.activities.ItemDetailsActivities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.Beans.Announcement;
import com.techcamp.aauj.rawabi.activities.abstractActivities.ScrollingActivity;
import com.techcamp.aauj.rawabi.fragments.ItemDetailsFragments.AnnouncmentDetailsFragment;
import com.techcamp.aauj.rawabi.fragments.ItemDetailsFragments.ItemDetailsFragment;

/**
 * Created by alaam on 1/14/2018.
 */

public class AnnouncmentDetailsActivity extends ScrollingActivity<Announcement> {
    public static Intent getIntent(Context ctx, Announcement announcement){
        Intent intent = new Intent(ctx,AnnouncmentDetailsActivity.class);
        intent.putExtra(ScrollingActivity.ARG_ITEM_ID,announcement);
        return intent;
    }


    @Override
    protected void onFabClicked() {

    }

    @Override
    protected String getBarTitle() {
        return "Announcement";
    }

    @Override
    public ItemDetailsFragment getFragment() {
        return AnnouncmentDetailsFragment.newInstance(mItem);
    }
}

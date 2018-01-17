package com.techcamp.aauj.rawabi.activities.ItemDetailsActivities;

import android.content.Context;
import android.content.Intent;

import com.techcamp.aauj.rawabi.Beans.Announcement;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.activities.abstractActivities.ScrollingActivity;
import com.techcamp.aauj.rawabi.fragments.ItemDetailsFragments.EventDetailsFragment;
import com.techcamp.aauj.rawabi.fragments.ItemDetailsFragments.ItemDetailsFragment;

/**
 * Created by alaam on 1/15/2018.
 */

public class EventDetailsActivity extends ScrollingActivity<Event>{
    public static Intent getIntent(Context ctx, Event event){
        Intent intent = new Intent(ctx,EventDetailsActivity.class);
        intent.putExtra(ScrollingActivity.ARG_ITEM_ID,event);
        return intent;
    }
    @Override
    protected void onFabClicked() {

    }

    @Override
    protected String getBarTitle() {
        return "Event";
    }

    @Override
    protected ItemDetailsFragment getFragment() {
        return EventDetailsFragment.newInstance(mItem);
    }
}

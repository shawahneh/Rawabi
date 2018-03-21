package com.techcamp.aauj.rawabi.activities.basicActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.model.Announcement;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.BasicActivity;
import com.techcamp.aauj.rawabi.fragments.ItemDetailsFragments.AnnouncmentDetailsFragment;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.fragments.listFragments.AnnouncementsListFragment;

/**
 * Created by ALa on 3/7/2018.
 */

public class QCenterListActivity extends BasicActivity implements ListFragment.IFragmentListener<Announcement> {
    @Override
    protected int getImageTop() {
        return R.drawable.logo_qcenter;
    }
    @Override
    protected int getImage() {
        return 0;
    }

    @Override
    protected String getBarTitle() {
        return "Q-Center";
    }

    @Override
    public Fragment getFragment() {
        return AnnouncementsListFragment.newInstance(1);
    }

    @Override
    public void onItemClicked(Announcement item) {
        replaceFragment(AnnouncmentDetailsFragment.newInstance(item));
    }
}

package com.techcamp.aauj.rawabi.activities.listActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.Beans.MediaItem;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.BasicActivity;
import com.techcamp.aauj.rawabi.fragments.listFragments.MediaListFragment;

/**
 * Created by alaam on 2/3/2018.
 */

public class MediaListActivity extends BasicActivity<MediaItem> {
    @Override
    protected int getImage() {
        return R.drawable.media_notexture;
    }

    @Override
    protected String getBarTitle() {
        return "Media";
    }

    @Override
    public Fragment getFragment() {
        return MediaListFragment.newInstance(3);
    }
}

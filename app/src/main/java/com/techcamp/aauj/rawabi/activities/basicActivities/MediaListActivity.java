package com.techcamp.aauj.rawabi.activities.basicActivities;

import android.support.v4.app.Fragment;

import com.stfalcon.frescoimageviewer.ImageViewer;
import com.techcamp.aauj.rawabi.Beans.Job;
import com.techcamp.aauj.rawabi.Beans.MediaItem;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.BasicActivity;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.fragments.listFragments.MediaListFragment;

/**
 * Created by ALa on 2/3/2018.
 */

public class MediaListActivity extends BasicActivity<MediaItem>  implements ListFragment.IFragmentListener<MediaItem> {
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

    @Override
    public void onItemClicked(MediaItem item) {
        new ImageViewer.Builder(this, new String[]{item.getImageUrl()})
                .setStartPosition(0)
                .show();
    }
}

package com.techcamp.aauj.rawabi.activities.basicActivities;

import android.support.v4.app.Fragment;

import com.stfalcon.frescoimageviewer.ImageViewer;
import com.techcamp.aauj.rawabi.model.MediaItem;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.BasicActivity;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.fragments.listFragments.MediaListFragment;

/**
 * Created by ALa on 2/3/2018.
 */

//unused
public class MediaListActivity extends BasicActivity<MediaItem>  implements ListFragment.IFragmentListener<MediaItem> {
    @Override
    protected int getImage() {
        return R.drawable.ic_media_50dp;
    }

    @Override
    protected String getBarTitle() {
        return "Media";
    }

    @Override
    public Fragment getFragment() {
//        return MediaListFragment.newInstance();
        return null;
    }

    @Override
    public void onItemClicked(MediaItem item) {
        new ImageViewer.Builder(this, new String[]{item.getImageUrl()})
                .setStartPosition(0)
                .show();
    }

}

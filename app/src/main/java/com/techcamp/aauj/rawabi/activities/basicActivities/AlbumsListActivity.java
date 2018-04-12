package com.techcamp.aauj.rawabi.activities.basicActivities;

import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.BasicActivity;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.fragments.listFragments.AlbumsListFragment;
import com.techcamp.aauj.rawabi.fragments.listFragments.MediaListFragment;
import com.techcamp.aauj.rawabi.model.AlbumItem;

public class AlbumsListActivity extends BasicActivity implements ListFragment.IFragmentListener<AlbumItem> {
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
        return new AlbumsListFragment();
    }

    @Override
    public void onItemClicked(AlbumItem item) {
        replaceFragment(MediaListFragment.newInstance(item));
    }
}

package com.techcamp.aauj.rawabi.fragments.ItemDetailsFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.techcamp.aauj.rawabi.activities.abstractActivities.ScrollingActivity;

/**
 * Created by alaam on 1/14/2018.
 */

public abstract class ItemDetailsFragment<T> extends Fragment {
    protected T mItem;
    public void refresh(T item){
        mItem = item;
        refreshView();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBean();
    }

    private void setBean() {
        if (getArguments().containsKey(ScrollingActivity.ARG_ITEM_ID)) {
            mItem = getArguments().getParcelable(ScrollingActivity.ARG_ITEM_ID);
        }else {
            mItem=null;
        }
    }

    protected abstract void refreshView();
}
package com.techcamp.aauj.rawabi.fragments.ItemDetailsFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.techcamp.aauj.rawabi.activities.abstractActivities.ScrollingActivity;

/**
 * Created by ALa on 1/14/2018.
 */

public abstract class ItemDetailsFragment<T> extends Fragment {
    public final static String ARG_ITEM = "item";
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
        if (getArguments().containsKey(ARG_ITEM)) {
            Log.d("tag","getArguments().containsKey(ARG_ITEM)=true");
            mItem = getArguments().getParcelable(ARG_ITEM);
        }else {
            mItem=null;
        }
    }

    protected abstract void refreshView();
}
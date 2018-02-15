package com.techcamp.aauj.rawabi.sliderLib;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techcamp.aauj.rawabi.R;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by alaam on 2/15/2018.
 */

public abstract class SliderAdapter<T> extends PagerAdapter {
    private ArrayList<T> items;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public SliderAdapter( Context mContext, ArrayList<T> items) {
        this.items = items;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(getLayout(),container,false);

        bind(view,items.get(position),position);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
    public abstract void bind(View view,T item,int pos);
    public abstract int getLayout();
}

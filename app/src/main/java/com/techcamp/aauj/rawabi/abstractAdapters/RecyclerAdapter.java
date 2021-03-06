package com.techcamp.aauj.rawabi.abstractAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALa on 2/1/2018.
 */

public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<Holder>
{
    protected List<T> mItems;

    public RecyclerAdapter(List<T> items)
    {
        mItems = items;
        if(mItems == null)
            mItems = new ArrayList<>();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(getLayoutId(), parent, false);
        return getNewHolder(view);
    }
    @Override
    public void onBindViewHolder(Holder holder, int position)
    {
        T item = mItems.get(position);
        holder.bind(item,position);
    }

    @Override
    public int getItemCount() {
        if(mItems == null) return 0;
        return mItems.size();
    }
    public void setList(List<T> items){
        if(items == null)
            return;
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public abstract int getLayoutId();
    public abstract Holder getNewHolder(View v);
}
package com.techcamp.aauj.rawabi.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.Beans.MyPlace;
import com.techcamp.aauj.rawabi.R;

import java.util.List;

/**
 * Created by alaam on 12/7/2017.
 */

public class PickerFragment extends ItemFragment {
    public final int ARG_COL_COUNT = 1;
    private List<MyPlace> places;
    private RecyclerView mRecyclerView;
    public static PickerFragment getInstance() {
        PickerFragment fragment = new PickerFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void setupRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.setHasFixedSize(true);
        if(ARG_COL_COUNT <= 1)
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        else
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),ARG_COL_COUNT));

        getPlaces();

    }

    private void updateRecycler(List<MyPlace> places){
        mRecyclerView.setAdapter(new MyAdapter(places));
    }

    private void getPlaces() {

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder>{
        List<MyPlace> places;

        public MyAdapter(List<MyPlace> places) {
            this.places = places;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_place, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            holder.bind(places.get(position),position);
        }

        @Override
        public int getItemCount() {
            return places.size();
        }

        class MyHolder extends RecyclerView.ViewHolder{
            private TextView mTextViewName,mTextViewSummery;
            private ImageView mImageView;
            private View mView;
            public void bind(MyPlace place, int pos){
                mTextViewSummery.setText(place.getSummery());
                mTextViewName.setText(place.getName());
                if(place.getImageurl() != null)
                Glide.with(mView).load(place.getImageurl()).into(mImageView);
            }
            public MyHolder(View itemView) {
                super(itemView);
                mView = itemView;
                mTextViewName = mView.findViewById(R.id.tvName);
                mTextViewSummery = mView.findViewById(R.id.tvSummery);
            }
        }
    }
}

package com.techcamp.aauj.rawabi.fragments.listFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.techcamp.aauj.rawabi.API.BasicApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.Beans.MediaItem;
import com.techcamp.aauj.rawabi.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.abstractAdapters.Holder;
import com.techcamp.aauj.rawabi.abstractAdapters.RecyclerAdapter;
import com.techcamp.aauj.rawabi.database.EventsDB;
import com.techcamp.aauj.rawabi.database.MediaItemsDB;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alaam on 2/3/2018.
 */

public class MediaListFragment extends ListFragment implements ICallBack<ArrayList<MediaItem>> {

    public static Fragment newInstance(int numberOfCols){
        Fragment fragment = new MediaListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_NUM_OF_COLS,numberOfCols);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void setupRecyclerViewAdapter() {
        loadFromDatabase();
        BasicApi api =  WebService.getInstance(getContext());
        api.getMedia(this);
        mSwipeRefreshLayout.setRefreshing(true);

        Fresco.initialize(getActivity());
    }

    private void loadFromDatabase() {
        List<MediaItem> list = MediaItemsDB.getInstance(getContext()).getAll();
        loadListToAdapter(list);
    }

    private void loadListToAdapter(List<MediaItem> list) {
        if(isAdded()){{
            //  available
            if(list.size() <= 0){
                //showMessageLayout("No Media",R.drawable.ic_signal_wifi_off_black_48dp);
            }else{
                hideMessageLayout();
                MediaListFragment.MyAdapter adapter = new MediaListFragment.MyAdapter(list);
                mRecyclerView.setAdapter(adapter);
            }
        }}
    }

    @Override
    public void onResponse(ArrayList<MediaItem> value) {
        //clear db
        MediaItemsDB.getInstance(getContext()).deleteAll();

        // save to db
        for (MediaItem mediaItem : value){
            MediaItemsDB.getInstance(getContext()).saveBean(mediaItem);
        }
        mSwipeRefreshLayout.setRefreshing(false);
        if(isAdded()){{
            //  available
            if(value.size() <= 0){
                //showMessageLayout("No Media",R.drawable.ic_signal_wifi_off_black_48dp);
            }else{
                hideMessageLayout();
                MyAdapter adapter = new MyAdapter(value);
                mRecyclerView.setAdapter(adapter);
            }
        }}
    }

    @Override
    public void onError(String err) {

    }


    private class MyHolder extends Holder<MediaItem> {
        private ImageView mImage;
        public MyHolder(View view) {
            super(view);
            mImage = view.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void bind(MediaItem item, int pos) {
            super.bind(item,pos);
            if(item.getImageUrl() != null)
                Glide.with(getContext()).load(item.getImageUrl()).into(mImage);
        }
        @Override
        public void onClicked(View v) {
            if(mListener != null)
                mListener.onItemClicked(mItem);
        }

    }
    private class MyAdapter extends RecyclerAdapter<MediaItem> {

        public MyAdapter(List<MediaItem> items) {
            super(items);
        }

        @Override
        public int getLayoutId() {
            return R.layout.row_media;
        }

        @Override
        public Holder getNewHolder(View v) {
            return new MyHolder(v);
        }
    }
}

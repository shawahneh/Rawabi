package com.techcamp.aauj.rawabi.fragments.listFragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.API.BasicApi;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.model.Event;
import com.techcamp.aauj.rawabi.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.abstractAdapters.Holder;
import com.techcamp.aauj.rawabi.abstractAdapters.RecyclerAdapter;
import com.techcamp.aauj.rawabi.database.EventsDB;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALa on 2/1/2018.
 */

public class EventsListFragment extends ListFragment implements ICallBack<ArrayList<Event>> {
    BasicApi mCalendarWebApi = WebApi.getInstance();
    private MyAdapter mAdapter;
    public static Fragment newInstance(int numberOfCols){
        Fragment fragment = new EventsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_NUM_OF_COLS,numberOfCols);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void setupRecyclerViewAdapter() {
        if(mAdapter == null)
            mAdapter = new MyAdapter(null);
        mRecyclerView.setAdapter(mAdapter);

        loadFromDatabase();
    }

    @Override
    protected void loadDataFromWeb() {
        mCalendarWebApi.getEvents(this);
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void loadFromDatabase() {
        List<Event> list = EventsDB.getInstance(getContext()).getAll();
        loadListToAdapter(list);
    }

    private void loadListToAdapter(List<Event> list) {
        if(isAdded()){{
            //  available
            if(list.size() <= 0){
//                showMessageLayout("No jobs",R.drawable.ic_signal_wifi_off_black_48dp);
            }else{
                hideMessageLayout();
                if(mAdapter != null)
                    mAdapter.setList(list);
            }
        }}
    }

    // data available from WEB
    @Override
    public void onResponse(ArrayList<Event> value) {
        updateDatabase(value);
        mSwipeRefreshLayout.setRefreshing(false);

        // update list
        if(isAdded()){{
            if(value.size() <= 0){
//                showMessageLayout("No Jobs available",R.drawable.ic_signal_wifi_off_black_48dp);
            }else{
                hideMessageLayout();
                loadListToAdapter(value);
            }
        }}
    }

    @Override
    public void onError(String err) {
        Snackbar.make(getView(),err,Snackbar.LENGTH_SHORT) .show();
        mSwipeRefreshLayout.setRefreshing(false);
    }
    private void updateDatabase(ArrayList<Event> value) {
        //clear db
        EventsDB.getInstance(getContext()).deleteAll();

        //save to db
        for (Event event :value ){
            EventsDB.getInstance(getContext()).saveBean(event);
        }
    }


    private class MyHolder extends Holder<Event> {
        private TextView mEventName,mEventDesc,mEventDate;
        private ImageView mEventImage;
        public MyHolder(View view) {
            super(view);
            mEventDesc = view.findViewById(R.id.eventDescTestView);
            mEventName = view.findViewById(R.id.eventNameTextView);
            mEventDate = view.findViewById(R.id.eventDateTextView);

            mEventImage = view.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void bind(Event event, int pos) {
            super.bind(event,pos);
            mEventDesc.setText(event.getDescription());
            mEventDate.setText(event.getDate().toString());
            mEventName.setText(event.getName());
            if(event.getImageUrl() != null)
                Glide.with(getContext()).load(event.getImageUrl()).into(mEventImage);
        }
        @Override
        public void onClicked(View v) {
            if(mListener != null)
                mListener.onItemClicked(mItem);
        }

    }
    private class MyAdapter extends RecyclerAdapter<Event> {

        public MyAdapter(List<Event> items) {
            super(items);
        }

        @Override
        public int getLayoutId() {
            return R.layout.row_event;
        }

        @Override
        public Holder getNewHolder(View v) {
            return new MyHolder(v);
        }
    }
}

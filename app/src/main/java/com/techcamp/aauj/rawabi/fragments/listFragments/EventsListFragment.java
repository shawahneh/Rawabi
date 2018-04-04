package com.techcamp.aauj.rawabi.fragments.listFragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.API.BasicApi;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.Event;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.abstractAdapters.Holder;
import com.techcamp.aauj.rawabi.abstractAdapters.RecyclerAdapter;
import com.techcamp.aauj.rawabi.database.EventsDB;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALa on 2/1/2018.
 */

public class EventsListFragment extends ListFragment implements IListCallBack<Event> {
    BasicApi mCalendarWebApi = WebApi.getInstance();
    private MyAdapter mAdapter;
    public static Fragment newInstance(){
        Fragment fragment = new EventsListFragment();
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
        setLoading(true);
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
    public void onResponse(List<Event> value) {
        Log.d("tag","onResponse");
        updateDatabase(value);
        setLoading(false);

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
        Log.d("tag","onError");
        if(getView() != null)
        Snackbar.make(getView(),err,Snackbar.LENGTH_SHORT) .show();
        setLoading(false);
    }
    private void updateDatabase(List<Event> value) {
        //clear db
        EventsDB.getInstance(getContext()).deleteAll();

        //save to db
        for (Event event :value ){
            EventsDB.getInstance(getContext()).saveBean(event);
        }
    }


    private class MyHolder extends Holder<Event> {
        private TextView mEventName,mEventDesc,mEventDate;
        private TextView tvDateDay,tvDateMonth;
        private ImageView mEventImage;
        public MyHolder(View view) {
            super(view);
            mEventDesc = view.findViewById(R.id.eventDescTestView);
            mEventName = view.findViewById(R.id.eventNameTextView);
            mEventDate = view.findViewById(R.id.eventDateTextView);
            tvDateDay = view.findViewById(R.id.tvDateDay);
            tvDateMonth = view.findViewById(R.id.tvDateMonth);

            mEventImage = view.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void bind(Event event, int pos) {
            super.bind(event,pos);
            mEventDesc.setText(event.getDescription());
            mEventDate.setText(DateUtil.getRelativeTime(event.getDate()));
            mEventName.setText(event.getName());
            tvDateMonth.setText(DateUtil.getMonthName(event.getDate()));
            tvDateDay.setText(event.getDate().getDay() + "");

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

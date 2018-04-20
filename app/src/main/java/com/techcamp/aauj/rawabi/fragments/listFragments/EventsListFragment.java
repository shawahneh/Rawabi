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
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.API.WebOffline;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.API.services.OfflineApi;
import com.techcamp.aauj.rawabi.API.services.RequestService;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.AlbumItem;
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
    private RequestService requestService;
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

        loadOffline();
    }

    @Override
    protected void loadDataFromWeb() {
        requestService = WebFactory.getBasicService().getEvents(this)
                .saveOffline(WebOffline.CODE_EVENTS)
                .start();
        setLoading(true);
    }


    @Override
    public void onDestroy() {
        if(requestService != null)
            requestService.cancel();
        super.onDestroy();
    }

    private void loadOffline() {
        /*  load offline data */
            List<Event> list =  WebFactory.getOfflineService().getEvents(getContext());
            loadListToAdapter(list);
    }

    private void loadListToAdapter(List<Event> list) {
        if(list == null)
            return;
        if(isAdded()){{
            //  available
                hideMessageLayout();
                if(mAdapter != null)
                    mAdapter.setList(list);

        }}
    }

    // data available from WEB
    @Override
    public void onResponse(List<Event> value) {

        setLoading(false);

            if(value.size() <= 0){

            }else{
                hideMessageLayout();
                loadListToAdapter(value);
            }
    }

    @Override
    public void onError(String err) {
        if(getView() != null)
        Snackbar.make(getView(),err,Snackbar.LENGTH_SHORT) .show();
        setLoading(false);
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
            tvDateDay.setText(DateUtil.getDayOfMonth(event.getDate()) + "");

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

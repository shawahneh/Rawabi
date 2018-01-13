package com.techcamp.aauj.rawabi.fragments.listFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.API.AnnouncmentWebApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Announcement;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alaam on 12/31/2017.
 */

public class AnnouncementsListFragment extends ListFragment implements IResponeTriger<ArrayList<Announcement>> {
    AnnouncmentWebApi api = WebService.getInstance(getContext());
    public static Fragment newInstance(int numberOfCols){
        Fragment fragment = new AnnouncementsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_NUM_OF_COLS,numberOfCols);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void setupRecyclerViewAdapter() {
        api.getAnnouns(this);
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onResponse(ArrayList<Announcement> value) {
        mSwipeRefreshLayout.setRefreshing(false);
        // msg available
        MyAdapter adapter = new MyAdapter(value);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onError(String err) {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private class MyHolder extends ListFragment.Holder<Announcement>{
        private TextView mEventName,mEventDesc,mEventDate;
        private ImageView mEventImage;
        public MyHolder(View view) {
            super(view);
            mEventDesc = view.findViewById(R.id.eventDescTestView);
            mEventName = view.findViewById(R.id.eventNameTextView);
            mEventDate = view.findViewById(R.id.eventDateTextView);
            mEventImage = view.findViewById(R.id.imageView);

        }

        @Override
        public void bind(Announcement announcement, int pos) {
            mEventDesc.setText(announcement.getDescription());
            mEventDate.setText(DateUtils.formatDateTime(getContext(),announcement.getDate().getTime(),DateUtils.FORMAT_SHOW_TIME|DateUtils.FORMAT_ABBREV_TIME));
            mEventName.setText(announcement.getName());
            if(announcement.getImageUrl() != null)
                Glide.with(getContext()).load(announcement.getImageUrl()).into(mEventImage);

        }
        @Override
        public void onClicked(View v) {

        }

    }
    private class MyAdapter extends ListFragment.Adapter<Announcement>{

        public MyAdapter(List<Announcement> items) {
            super(items);
        }

        @Override
        public int getLayoutId() {
            return R.layout.row_announcment;
        }

        @Override
        public Holder getNewHolder(View v) {
            return new MyHolder(v);
        }
    }
}

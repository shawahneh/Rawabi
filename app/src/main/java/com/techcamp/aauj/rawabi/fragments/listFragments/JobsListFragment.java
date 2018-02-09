package com.techcamp.aauj.rawabi.fragments.listFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.API.BasicApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Job;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.abstractAdapters.RecyclerAdapter;
import com.techcamp.aauj.rawabi.activities.ItemDetailsActivities.JobDetailsActivity;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alaam on 12/31/2017.
 */

public class JobsListFragment extends ListFragment implements IResponeTriger<ArrayList<Job>> {
    BasicApi api = WebService.getInstance(getContext());
    public static Fragment newInstance(int numberOfCols){
        Fragment fragment = new JobsListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_NUM_OF_COLS,numberOfCols);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void setupRecyclerViewAdapter() {
        api.getJobs(this);
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onResponse(ArrayList<Job> value) {
        mSwipeRefreshLayout.setRefreshing(false);
        if(isAdded()){{
            //  available
            if(value.size() <= 0){
                showMessageLayout("No internet connection",R.drawable.ic_signal_wifi_off_black_48dp);
            }else{
                hideMessageLayout();
                MyAdapter adapter = new MyAdapter(value);
                mRecyclerView.setAdapter(adapter);
            }
        }}

    }

    @Override
    public void onError(String err) {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private class MyHolder extends com.techcamp.aauj.rawabi.abstractAdapters.Holder<Job> {
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
        public void bind(Job job, int pos) {
            mEventDesc.setText(job.getDescription());
            mEventDate.setText(job.getRealDate());
            mEventName.setText(job.getName());
            if(job.getImageUrl() != null)
                Glide.with(getContext()).load(job.getImageUrl()).into(mEventImage);
        }
        @Override
        public void onClicked(View v) {
            Intent i = JobDetailsActivity.getIntent(getContext(),mItem);
            startActivity(i);
        }

    }
    private class MyAdapter extends RecyclerAdapter<Job> {

        public MyAdapter(List<Job> items) {
            super(items);
        }

        @Override
        public int getLayoutId() {
            return R.layout.row_job;
        }

        @Override
        public com.techcamp.aauj.rawabi.abstractAdapters.Holder getNewHolder(View v) {
            return new MyHolder(v);
        }
    }
}

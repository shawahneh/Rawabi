package com.techcamp.aauj.rawabi.fragments.carpoolFragments;

import android.content.Intent;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.techcamp.aauj.rawabi.API.CarpoolApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.abstractAdapters.Holder;
import com.techcamp.aauj.rawabi.abstractAdapters.RecyclerAdapter;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.JourneyDetailActivity;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.utils.MapUtil;
import com.techcamp.aauj.rawabi.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alaam on 2/18/2018.
 */

public class MyJourneysListFragment extends ListFragment implements ICallBack<ArrayList<Journey>> {
    @Override
    public void setupRecyclerViewAdapter() {
        mSwipeRefreshLayout.setRefreshing(true);
        CarpoolApi api = WebService.getInstance();
        api.getJourneys(0,0,0,this);
    }

    @Override
    protected void loadDataFromWeb() {

    }

    @Override
    public void onResponse(ArrayList<Journey> item) {
        hideMessageLayout();
        MyAdapter adapter = new MyAdapter(item);
        mRecyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(String err) {
        mSwipeRefreshLayout.setRefreshing(false);
        showMessageLayout(err,R.drawable.error_center_x);
    }


    class JourneyHolder extends Holder<Journey> {
        private TextView tvDate,tvFrom,tvTo,tvStatus;
        public JourneyHolder(View view) {
            super(view);

            tvDate = view.findViewById(R.id.tvDate);
            tvFrom = view.findViewById(R.id.tvFrom);
            tvTo = view.findViewById(R.id.tvTo);
            tvStatus = view.findViewById(R.id.tvStatus);
        }

        @Override
        public void bind(Journey journey, int pos) {
            super.bind(journey, pos);
            tvDate.setText(DateUtils.getRelativeDateTimeString(getContext(),journey.getGoingDate().getTime(),DateUtils.MINUTE_IN_MILLIS,DateUtils.WEEK_IN_MILLIS,0));
            tvFrom.setText(MapUtil.getSavedAddress(getContext(),journey.getStartPoint()));
            tvTo.setText(MapUtil.getSavedAddress(getContext(),journey.getEndPoint()));

            tvStatus.setText(StringUtil.getJourneyStatus(journey.getStatus()));
        }

        @Override
        public void onClicked(View v) {
            if(mSwipeRefreshLayout.isRefreshing())
                return;
            if(mListener != null)
                mListener.onItemClicked(mItem);
        }
    }
    class MyAdapter extends RecyclerAdapter<Journey> {

        public MyAdapter(List<Journey> items) {
            super(items);
        }

        @Override
        public int getLayoutId() {
            return R.layout.row_journey;
        }

        @Override
        public Holder getNewHolder(View v) {
            return new JourneyHolder(v);
        }
    }
}

package com.techcamp.aauj.rawabi.fragments.carpoolFragments;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.techcamp.aauj.rawabi.API.CarpoolApi;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragmentWithSwipe;
import com.techcamp.aauj.rawabi.model.Ride;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.abstractAdapters.Holder;
import com.techcamp.aauj.rawabi.abstractAdapters.RecyclerAdapter;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.utils.MapUtil;
import com.techcamp.aauj.rawabi.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALa on 2/18/2018.
 */

public class MyRidesListFragment extends ListFragmentWithSwipe implements IListCallBack<Ride> {

    @Override
    public void setupRecyclerViewAdapter() {
    }

    @Override
    protected void loadDataFromWeb() {

        mSwipeRefreshLayout.setRefreshing(true);
        CarpoolApi api = WebFactory.getCarpoolService();
        api.getRides(-1,0,100,this).start();
    }

    @Override
    public void onResponse(List<Ride> rides) {
        hideMessageLayout();
        MyAdapter adapter = new MyAdapter(rides);
        mRecyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(String err) {
        mSwipeRefreshLayout.setRefreshing(false);
        showMessageLayout(err,R.drawable.error_center_x);
    }


    class RideHolder extends Holder<Ride>{
        private TextView tvDate,tvFrom,tvTo,tvStatus;
        public RideHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvFrom = view.findViewById(R.id.tvFrom);
            tvTo = view.findViewById(R.id.tvTo);
            tvStatus = view.findViewById(R.id.tvStatus);
        }

        @Override
        public void bind(Ride ride, int pos) {
            super.bind(ride, pos);

            tvDate.setText( DateUtils.getRelativeDateTimeString(getContext(),ride.getJourney().getGoingDate().getTime(),DateUtils.MINUTE_IN_MILLIS,DateUtils.WEEK_IN_MILLIS,0) );
            tvFrom.setText(MapUtil.getSavedAddress(getContext(),ride.getJourney().getStartPoint()));
            tvTo.setText(MapUtil.getSavedAddress(getContext(),ride.getJourney().getEndPoint()));
//            tvFrom.setText("Map Point");
//            tvTo.setText("Map Point");
            tvStatus.setText(StringUtil.getRideStatus(ride.getOrderStatus()));
        }

        @Override
        public void onClicked(View v) {
            if(mListener != null)
                mListener.onItemClicked(mItem);
        }
    }
    class MyAdapter extends RecyclerAdapter<Ride>{

        public MyAdapter(List<Ride> items) {
            super(items);
        }

        @Override
        public int getLayoutId() {
            return R.layout.row_ride;
        }

        @Override
        public Holder getNewHolder(View v) {
            return new RideHolder(v);
        }
    }
}

package com.techcamp.aauj.rawabi.fragments.carpoolFragments;

import android.content.Intent;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.techcamp.aauj.rawabi.API.CarpoolApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.abstractAdapters.Holder;
import com.techcamp.aauj.rawabi.abstractAdapters.RecyclerAdapter;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.RideDetailActivity;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.utils.MapUtil;
import com.techcamp.aauj.rawabi.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alaam on 2/18/2018.
 */

public class MyRidesListFragment extends ListFragment implements ICallBack<ArrayList<Ride>> {

    @Override
    public void setupRecyclerViewAdapter() {
        mSwipeRefreshLayout.setRefreshing(true);
        CarpoolApi api = WebService.getInstance(getActivity());
        api.getRides(0,0,0,this);
    }

    @Override
    public void onResponse(ArrayList<Ride> rides) {
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

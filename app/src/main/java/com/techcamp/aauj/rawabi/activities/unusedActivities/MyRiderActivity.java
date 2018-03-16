package com.techcamp.aauj.rawabi.activities.unusedActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.techcamp.aauj.rawabi.API.CarpoolApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.ListActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.RideDetailActivity;
import com.techcamp.aauj.rawabi.utils.MapUtil;
import com.techcamp.aauj.rawabi.utils.StringUtil;

import java.util.ArrayList;

/**
 * Created by ALa on 12/21/2017.
 */

public class MyRiderActivity extends ListActivity<Ride> {
    CarpoolApi poolingRides = WebService.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MY RIDES");
    }
    @Override
    protected void setupRecyclerViewAdapter(final RecyclerView mRecyclerView) {
        setSwipeRefresh(true);
        poolingRides.getRides(0, 0, 0, new ICallBack<ArrayList<Ride>>() {
            @Override
            public void onResponse(final ArrayList<Ride> item) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyAdapter adapter = new MyAdapter(MyRiderActivity.this,item);
                        mRecyclerView.setAdapter(adapter);
                        setSwipeRefresh(false);
                    }
                });

            }

            @Override
            public void onError(String err) {
                Log.d("tag","error "+err);
            }
        });

    }

    class MyAdapter extends ListActivity<Ride>.MyAdapter{

        class MyHolder extends ListActivity<Ride>.MyAdapter.ItemViewHolder{
            private TextView tvDate,tvFrom,tvTo,tvStatus;
            public MyHolder(View view) {
                super(view);

                tvDate = view.findViewById(R.id.tvDate);
                tvFrom = view.findViewById(R.id.tvFrom);
                tvTo = view.findViewById(R.id.tvTo);
                tvStatus = view.findViewById(R.id.tvStatus);
            }

            @Override
            public void bind(Ride ride) {
                tvDate.setText(ride.getJourney().getGoingDate().toString());
                tvFrom.setText(MapUtil.getAddress(MyRiderActivity.this,ride.getJourney().getStartPoint()));
                tvTo.setText(MapUtil.getAddress(MyRiderActivity.this,ride.getJourney().getEndPoint()));
                tvStatus.setText(StringUtil.getRideStatus(ride.getOrderStatus()));
            }
        }
        public MyAdapter(Context mContext, ArrayList<Ride> objs) {
            super(mContext, objs);
        }

        @Override
        protected int getLayout() {
            return R.layout.row_ride;
        }

        @Override
        protected ItemViewHolder getHolder(View view) {
            return new MyHolder(view);
        }

        @Override
        protected void onItemClick(Ride item, int pos) {
            Intent i = RideDetailActivity.getIntent(MyRiderActivity.this,item);
            startActivity(i);
        }
    }
}

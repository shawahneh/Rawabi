package com.techcamp.aauj.rawabi.activities.unusedActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.techcamp.aauj.rawabi.API.CarpoolApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.ListActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.JourneyDetailActivity;
import com.techcamp.aauj.rawabi.utils.MapUtil;
import com.techcamp.aauj.rawabi.utils.StringUtil;

import java.util.ArrayList;

/**
 * Created by alaam on 12/21/2017.
 */

public class MyJourneysActivity extends ListActivity<Journey> {
    CarpoolApi poolingJourney = WebService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MY JOURNEYS");
    }

    @Override
    protected void setupRecyclerViewAdapter(final RecyclerView mRecyclerView) {
        setSwipeRefresh(true);
        poolingJourney.getJourneys(0, 0, 0, new ICallBack<ArrayList<Journey>>() {
            @Override
            public void onResponse(final ArrayList<Journey> item) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setSwipeRefresh(false);
                        MyAdapter adapter = new MyAdapter(MyJourneysActivity.this,item);
                        mRecyclerView.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onError(String err) {
                setSwipeRefresh(false);
            }
        });
    }




    class MyAdapter extends ListActivity<Journey>.MyAdapter{

        class MyHolder extends ListActivity<Journey>.MyAdapter.ItemViewHolder{
            private TextView tvDate,tvFrom,tvTo,tvStatus;
            public MyHolder(View view) {
                super(view);

                tvDate = view.findViewById(R.id.tvDate);
                tvFrom = view.findViewById(R.id.tvFrom);
                tvTo = view.findViewById(R.id.tvTo);
                tvStatus = view.findViewById(R.id.tvStatus);
            }

            @Override
            public void bind(Journey journey) {
                tvDate.setText(journey.getGoingDate().toString());
                tvFrom.setText(MapUtil.getAddress(MyJourneysActivity.this,journey.getStartPoint()));
                tvTo.setText(MapUtil.getAddress(MyJourneysActivity.this,journey.getEndPoint()));

                tvStatus.setText(StringUtil.getJourneyStatus(journey.getStatus()));

            }
        }
        public MyAdapter(Context mContext, ArrayList<Journey> objs) {
            super(mContext, objs);
        }

        @Override
        protected int getLayout() {
            return R.layout.row_journey;
        }

        @Override
        protected ItemViewHolder getHolder(View view) {
            return new MyAdapter.MyHolder(view);
        }

        @Override
        protected void onItemClick(Journey item, int pos) {
            if(mSwipeRefreshLayout.isRefreshing())
                return;
            Intent i = new Intent(MyJourneysActivity.this,JourneyDetailActivity.class);
            i.putExtra(JourneyDetailActivity.ARG_JOURNEY,item);
            startActivity(i);
        }
    }
}

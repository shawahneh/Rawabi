package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.techcamp.aauj.rawabi.API.PoolingRides;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.R;

import java.util.ArrayList;

/**
 * Created by alaam on 12/21/2017.
 */

public class MyRiderActivity extends ListActivity<Ride> {
    PoolingRides poolingRides = WebApi.getInstance(this);
    @Override
    protected void setupRecyclerViewAdapter(final RecyclerView mRecyclerView) {
        poolingRides.getRides(0, 0, 0, new IResponeTriger<ArrayList<Ride>>() {
            @Override
            public void onResponse(final ArrayList<Ride> item) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyAdapter adapter = new MyAdapter(MyRiderActivity.this,item);
                        mRecyclerView.setAdapter(adapter);
                    }
                });

            }

            @Override
            public void onError(String err) {

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
                tvDate.setText(ride.getJourney().getRealDate());
                tvFrom.setText(ride.getJourney().getStartPoint().toString());
                tvTo.setText(ride.getJourney().getEndPoint().toString());
                tvStatus.setText(ride.getOrderStatus() + "");
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
            Log.d("tag","item click " + pos );
        }
    }
}

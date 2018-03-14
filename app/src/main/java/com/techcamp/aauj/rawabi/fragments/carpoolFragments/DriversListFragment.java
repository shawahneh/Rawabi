package com.techcamp.aauj.rawabi.fragments.carpoolFragments;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.API.CarpoolApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.abstractAdapters.Holder;
import com.techcamp.aauj.rawabi.abstractAdapters.RecyclerAdapter;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.fragments.listFragments.JobsListFragment;
import com.techcamp.aauj.rawabi.utils.DateUtil;
import com.techcamp.aauj.rawabi.utils.MapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alaam on 2/19/2018.
 */

public class DriversListFragment extends ListFragment {
    public static final String ARG_LIST = "list_j";
    private ArrayList<Journey> mJourneys;

    public static Fragment newInstance(ArrayList<Journey> journeys){
        Fragment fragment = new DriversListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_LIST,journeys);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void setupRecyclerViewAdapter() {
        if(getArguments() != null ){
            mJourneys  =   getArguments().getParcelableArrayList(ARG_LIST);
            Log.d("tag","mJourneys, " + (mJourneys == null?"Null":"not null"));
        }
//        for (Parcelable journey : getArguments().getParcelableArrayList(ARG_LIST))
//            Log.d("tag","mJourneys.name="+((Journey)journey).getUser().getFullname());
//        if(mJourneys != null){
            MyAdapter adapter  = new MyAdapter(mJourneys);
            mRecyclerView.setAdapter(adapter);
            mSwipeRefreshLayout.setRefreshing(false);
            Log.d("tag","mJourneys != null");
//        }

    }

    @Override
    protected void loadDataFromWeb() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null ){
              mJourneys =   getArguments().getParcelableArrayList(ARG_LIST);
                Log.d("tag","mJourneys, " + (mJourneys == null?"Null":"not null"));
        }

    }

    class DriverHolder extends Holder<Journey>{
        private TextView tvName,tvDate,tvFrom,tvTo,tvDescription;
        private RatingBar ratingBar;
        private View btnView;
        private View layoutDetails;
        private ImageView imgDriver;
        public DriverHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvName  = itemView.findViewById(R.id.tvName);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            btnView = itemView.findViewById(R.id.btnView);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvTo = itemView.findViewById(R.id.tvTo);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            layoutDetails = itemView.findViewById(R.id.layoutDetails);
            imgDriver = itemView.findViewById(R.id.imgDriver);
        }

        @Override
        public void bind(Journey item, int pos) {
            super.bind(item, pos);
            tvName.setText(item.getUser().getFullname());
            tvDate.setText(DateUtil.formatDateToTime(item.getGoingDate().getTime()));
            tvFrom.setText(MapUtil.getSavedAddress(itemView.getContext(),item.getStartPoint()));
            tvTo.setText(MapUtil.getSavedAddress(itemView.getContext(),item.getEndPoint()));
            tvDescription.setText("Seats Available"+" "+item.getSeats());

            Glide.with(itemView.getContext()).load(item.getUser().getImageurl()).into(imgDriver);

            layoutDetails.setVisibility(View.GONE);

            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isAdded()){
                        Intent i = new Intent();
                        i.putExtra("data",mItem);
                        getActivity().setResult(Activity.RESULT_OK,i);
                        getActivity().finish();
                    }
                }
            });
//            ratingBar.setRating(4);
        }

        @Override
        public void onClicked(View v) {

            final int originalHeight = layoutDetails.getHeight();
            animationDown((LinearLayout) layoutDetails, originalHeight);
        }
        public void animationDown(final LinearLayout details, int originalHeight){

            // Declare a ValueAnimator object
            ValueAnimator valueAnimator;
            if (!(details.getVisibility() == View.VISIBLE)) {
                Log.d("tag","animationDown");
                details.setVisibility(View.VISIBLE);
                details.setEnabled(true);
                valueAnimator = ValueAnimator.ofInt(0, 350); // These values in this method can be changed to expand however much you like
            } else {
                valueAnimator = ValueAnimator.ofInt(originalHeight, 0);

                Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out

                a.setDuration(200);
                // Set a listener to the animation and configure onAnimationEnd
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        details.setVisibility(View.GONE);
                        details.setEnabled(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                // Set the animation on the custom view
                details.startAnimation(a);
            }
            valueAnimator.setDuration(200);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    details.getLayoutParams().height = value.intValue();
                    details.requestLayout();
                    Log.d("tag",value.intValue()+" v");
                }
            });


            valueAnimator.start();
        }
    }
    class MyAdapter extends RecyclerAdapter<Journey>{

        public MyAdapter(List<Journey> items) {
            super(items);
        }

        @Override
        public int getLayoutId() {
            return R.layout.row_driver;
        }

        @Override
        public Holder getNewHolder(View v) {
            return new DriverHolder(v);
        }
    }
}

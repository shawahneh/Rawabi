package com.techcamp.aauj.rawabi.fragments.listFragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.API.BasicApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Announcement;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.abstractAdapters.Holder;
import com.techcamp.aauj.rawabi.abstractAdapters.RecyclerAdapter;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alaam on 12/31/2017.
 */

public class AnnouncementsListFragment extends ListFragment implements IResponeTriger<ArrayList<Announcement>> {
    BasicApi api = WebService.getInstance(getContext());
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

    private class MyHolder extends Holder<Announcement> {
        private TextView mEventName,mEventDesc,mEventDate;
        private ImageView mEventImage;
        private View mView;

        public MyHolder(View view) {
            super(view);
            mView = view;
            mEventDesc = view.findViewById(R.id.eventDescTestView);
            mEventName = view.findViewById(R.id.eventNameTextView);
            mEventDate = view.findViewById(R.id.eventDateTextView);
            mEventImage = view.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }


        public View details() {
            return mView.findViewById(R.id.layoutDetails);
        }

        @Override
        public void bind(Announcement announcement, int pos) {
            mEventDesc.setText(announcement.getDescription());
            mEventDate.setText(DateUtils.getRelativeDateTimeString(getContext(),announcement.getDate().getTime(),DateUtils.MINUTE_IN_MILLIS,DateUtils.WEEK_IN_MILLIS,0));
            mEventName.setText(announcement.getName());
            if(announcement.getImageUrl() != null)
                Glide.with(getContext()).load(announcement.getImageUrl()).into(mEventImage);

        }
//        @Override
//        public void onClicked(View v) {
//            Intent i = AnnouncmentDetailsActivity.getIntent(getContext(),mItem);
//            startActivity(i);
//        }
        @Override
        public void onClicked(View v) {
//            final int originalHeight = details().getHeight();
//            animationDown((LinearLayout) details(), originalHeight);//here put the name of you layout that have the options to expand.
        }

        //Animation for devices with kitkat and below
        public void animationDown(final LinearLayout billChoices, int originalHeight){

            // Declare a ValueAnimator object
            ValueAnimator valueAnimator;
            if (!(billChoices.getVisibility() == View.VISIBLE)) {
                Log.d("tag","animationDown");
                billChoices.setVisibility(View.VISIBLE);
                billChoices.setEnabled(true);
                valueAnimator = ValueAnimator.ofInt(0, 168); // These values in this method can be changed to expand however much you like
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
                        billChoices.setVisibility(View.GONE);
                        billChoices.setEnabled(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                // Set the animation on the custom view
                billChoices.startAnimation(a);
            }
            valueAnimator.setDuration(200);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    billChoices.getLayoutParams().height = value.intValue();
                    billChoices.requestLayout();
                    Log.d("tag",value.intValue()+" v");
                }
            });


            valueAnimator.start();
        }
    }






    private class MyAdapter extends RecyclerAdapter<Announcement> {

        public MyAdapter(List<Announcement> items) {
            super(items);
        }

        @Override
        public int getLayoutId() {
            return R.layout.row_qcenter;
        }

        @Override
        public Holder getNewHolder(View v) {
            return new MyHolder(v);
        }
    }
}

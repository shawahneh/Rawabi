package com.techcamp.aauj.rawabi.fragments.listFragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.API.WebOffline;
import com.techcamp.aauj.rawabi.API.services.RequestService;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.Announcement;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.abstractAdapters.Holder;
import com.techcamp.aauj.rawabi.abstractAdapters.RecyclerAdapter;
import com.techcamp.aauj.rawabi.database.AnnouncementDB;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALa on 12/31/2017.
 */

public class AnnouncementsListFragment extends ListFragment implements IListCallBack<Announcement> {
    BasicApi api = WebApi.getInstance();
    private RequestService requestService;
    private MyAdapter mAdapter;
    public static Fragment newInstance(){
        Fragment fragment = new AnnouncementsListFragment();
        return fragment;
    }
    @Override
    public void setupRecyclerViewAdapter() {
        if(mAdapter == null)
            mAdapter = new MyAdapter(null);
        mRecyclerView.setAdapter(mAdapter);

        loadOffline();

    }

    @Override
    protected void loadDataFromWeb() {
        api.getAnnouns(this);
        setLoading(true);
    }

    private void loadOffline() {
        List<Announcement> announcements =  new WebOffline().getAnnouncements(getContext());
        loadListToAdapter(announcements);
    }

    private void loadListToAdapter(List<Announcement> list) {
        if(isAdded()){{
            //  available
            hideMessageLayout();
            if(mAdapter != null)
                mAdapter.setList(list);

        }}
    }

    @Override
    public void onResponse(List<Announcement> value) {
        setLoading(false);
        loadListToAdapter(value);
    }


    @Override
    public void onError(String err) {
        if(getView() != null)
        Snackbar.make(getView(),err,Snackbar.LENGTH_SHORT) .show();
        setLoading(false);
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
            super.bind(announcement,pos);
            mEventDesc.setText(announcement.getDescription());
            mEventDate.setText(DateUtils.getRelativeDateTimeString(getContext(),announcement.getStartDate().getTime(),DateUtils.MINUTE_IN_MILLIS,DateUtils.WEEK_IN_MILLIS,0));
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
            if(mListener != null)
                mListener.onItemClicked(mItem);
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



    @Override
    public void onDestroy() {
        if(requestService != null)
            requestService.cancel();
        super.onDestroy();
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

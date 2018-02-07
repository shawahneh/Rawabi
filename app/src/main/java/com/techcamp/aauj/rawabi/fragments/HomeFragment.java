package com.techcamp.aauj.rawabi.fragments;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.API.AnnouncmentWebApi;
import com.techcamp.aauj.rawabi.API.CalendarWebApi;
import com.techcamp.aauj.rawabi.API.PoolingJourney;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.basicActivities.QCenterActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.LoginRegisterActivity;
import com.techcamp.aauj.rawabi.activities.listActivities.EventsListActivity;
import com.techcamp.aauj.rawabi.activities.listActivities.JobsListActivity;
import com.techcamp.aauj.rawabi.activities.listActivities.MediaListActivity;
import com.techcamp.aauj.rawabi.activities.listActivities.TransportationActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG_JOBS = "tag_jobs";
    private static final String TAG_MEDIA = "tag_media";
    private static final String TAG_TRANSPORTATION = "tag_trans";
    private static final String TAG_CARPOOL = "tag_carpool";

    private Button btnOpenCarpool,btnOpenCalendar;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private IResponeTriger<Integer> trigerCars;
    private IResponeTriger<String> trigerWeather;
    private IResponeTriger<ArrayList<Event>> trigerEvents;
    private TextView tvCarpool,tvWeather,tvEventName;
    private ProgressBar progressBar;
    private ImageView imgWeather;
    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        btnOpenCarpool = view.findViewById(R.id.btnOpenCarpool);
        btnOpenCalendar =view.findViewById(R.id.btnOpenCalendar);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv);
        tvCarpool = view.findViewById(R.id.tvCarpool);
        tvWeather = view.findViewById(R.id.tvWeather);
        tvEventName= view.findViewById(R.id.tvEventName);

        imgWeather = view.findViewById(R.id.imgWeather);

        btnOpenCarpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), LoginRegisterActivity.class);
                startActivity(i);
            }
        });
        btnOpenCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), EventsListActivity.class);
                startActivity(i);
            }
        });
        progressBar = view.findViewById(R.id.progressBar);

        trigerCars = new IResponeTriger<Integer>() {
            @Override
            public void onResponse(Integer item) {
                progressBar.setVisibility(View.GONE);
                if(item>0) {
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(0, item);
                    valueAnimator.setDuration(500);

                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {

                            tvCarpool.setText(valueAnimator.getAnimatedValue().toString()+ " cars available");

                        }
                    });
                    valueAnimator.start();

                }
                else
                    tvCarpool.setText("Looking for a ride?");
            }

            @Override
            public void onError(String err) {
                progressBar.setVisibility(View.GONE);
            }
        };
        trigerWeather = new IResponeTriger<String>() {
            @Override
            public void onResponse(String item) {
                tvWeather.setText(item);
            }

            @Override
            public void onError(String err) {

            }
        };
        trigerEvents = new IResponeTriger<ArrayList<Event>>() {
            @Override
            public void onResponse(ArrayList<Event> item) {
                int n = item.size();
                if(n>0)
                    tvEventName.setText(n + " events today");
                else
                    tvEventName.setText("no events today");
            }

            @Override
            public void onError(String err) {

            }
        };
        FloatingActionButton fabView = view.findViewById(R.id.floatingActionButton);
        fabView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                getNumberOfCars();

            }
        });
        setupRecyclerView();


        getWeather();
        getEventsToday();

        Glide.with(getContext()).load("https://data.whicdn.com/images/245762463/original.gif").into(imgWeather);
        return view;
    }

    private void getEventsToday() {
        CalendarWebApi api = WebService.getInstance(getActivity());
        api.getEventAtDate(new Date(),trigerEvents);
    }

    private void getWeather() {
        AnnouncmentWebApi api = WebService.getInstance(getActivity());
        api.getWeather(trigerWeather);
    }

    private void getNumberOfCars() {
        if(progressBar.getVisibility() == View.VISIBLE)
            return;
        tvCarpool.setText("Looking for a ride?");
        PoolingJourney api = WebService.getInstance(getActivity());
        api.getNumberOfJourneys(trigerCars);
        progressBar.setVisibility(View.VISIBLE);

    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        setupListAndRecycler();
    }

    private void setupListAndRecycler(){
        List<DashboardItem> list = getItems();
        if (mAdapter == null)
        {
            mAdapter = new Adapter(list);
            mRecyclerView.setAdapter(mAdapter);
        }
        else
        {
            mAdapter.setList(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    private List<DashboardItem> getItems() {
        DashboardItem jobs = new DashboardItem(TAG_JOBS,getResources().getDrawable(R.drawable.job_notexture),"JOBS");
        DashboardItem media = new DashboardItem(TAG_MEDIA,getResources().getDrawable(R.drawable.media_notexture),"MEDIA");
        DashboardItem transportation = new DashboardItem(TAG_TRANSPORTATION,getResources().getDrawable(R.drawable.bus_notexture),"Transportation");
        DashboardItem carpool = new DashboardItem(TAG_CARPOOL,getResources().getDrawable(R.drawable.car_notexture),"CARPOOL");

        jobs.setSummery("Bobs and Internships");
        media.setSummery("Explore Rawabi Media");
        transportation.setSummery("Public Buses Transportation");

        ArrayList<DashboardItem> items = new ArrayList<>();
        items.add(jobs);
        items.add(media);
        items.add(transportation);
        items.add(carpool);

        return items;
    }

    private class Holder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView mImageView;
        private TextView mTextView;
        private TextView mSummeryTextView;
        private DashboardItem mDashboardItem;
        private View lineView;
        public Holder(View itemView)
        {
            super(itemView);

            mImageView= (ImageView)itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
            mTextView=(TextView)itemView.findViewById(R.id.textView);
            mSummeryTextView=(TextView)itemView.findViewById(R.id.summeryTextView);
            lineView= itemView.findViewById(R.id.lineView);
        }


        public void bind(DashboardItem item)
        {
            mDashboardItem = item;
            mTextView.setText(item.getTitle());
            mImageView.setImageDrawable(item.getDrawable());
            mSummeryTextView.setText(item.getSummery());
//            lineView.setBackgroundColor(item.getLineColor());
        }

        @Override
        public void onClick(View v)
        {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), mImageView, "trImage");
            switch (mDashboardItem.getTag()){
                case TAG_JOBS:
                    Intent i = new Intent(getContext(),JobsListActivity.class);
                    startActivity(i,options.toBundle());
                    break;
                case TAG_TRANSPORTATION:
                    startActivity(new Intent(getContext(),TransportationActivity.class),options.toBundle());
                    break;
                case TAG_MEDIA:
                    startActivity(new Intent(getContext(),MediaListActivity.class),options.toBundle());
                    break;
                case TAG_CARPOOL:
                    startActivity(new Intent(getContext(),QCenterActivity.class),options.toBundle());
                    break;
            }
        }
    }
    private class Adapter extends RecyclerView.Adapter<Holder>
    {
        private List<DashboardItem> mItems;

        public Adapter(List<DashboardItem> items)
        {
            mItems = items;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.row_dashboard_item_layout, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position)
        {
            DashboardItem item = mItems.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
        public void setList(List<DashboardItem> items){
            mItems=items;
        }

    }

    private class DashboardItem{
        private Drawable mDrawable;
        private String title;
        private String tag;
        private String summery;
        private boolean tint;

        public int getLineColor() {
            return lineColor;
        }

        public void setLineColor(int lineColor) {
            this.lineColor = lineColor;
        }

        private int lineColor;

        public String getSummery() {
            return summery;
        }

        public void setSummery(String summery) {
            this.summery = summery;
        }

        public DashboardItem(String tag, Drawable drawable, String title) {
            this.tag = tag;
            mDrawable = drawable;
            this.title = title;
            summery = "";
//            lineColor = Color.TRANSPARENT;
        }

        public boolean isTint() {
            return tint;
        }

        public void setTint(boolean tint) {
            this.tint = tint;
        }

        public String getTag(){
            return tag;
        }
        public void setTag(String tag){
            this.tag = tag;
        }

        public Drawable getDrawable() {
            return mDrawable;
        }

        public void setDrawable(Drawable drawable) {
            mDrawable = drawable;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}

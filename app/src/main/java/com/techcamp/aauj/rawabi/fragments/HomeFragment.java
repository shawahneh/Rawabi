package com.techcamp.aauj.rawabi.fragments;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.API.BasicApi;
import com.techcamp.aauj.rawabi.API.CarpoolApi;
import com.techcamp.aauj.rawabi.activities.basicActivities.AlbumsListActivity;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.Event;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.basicActivities.EventsListActivity;
import com.techcamp.aauj.rawabi.activities.basicActivities.JobsListActivity;
import com.techcamp.aauj.rawabi.activities.basicActivities.MediaListActivity;
import com.techcamp.aauj.rawabi.activities.basicActivities.TransportationActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    public static final String TAG_JOBS = "tag_jobs";
    public static final String TAG_MEDIA = "tag_media";
    public static final String TAG_TRANSPORTATION = "tag_trans";
    public static final String TAG_CARPOOL = "tag_carpool";
    public static final String TAG_EVENTS = "tag_events";
    public static final String TAG_NEWS = "tag_news";
    public static final String TAG_QCENTER = "TAG_QCENTER";


    private View btnOpenCarpool,btnOpenCalendar;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private ICallBack<Integer> trigerCars;
    private ICallBack<String> trigerWeather;
    private IListCallBack<Event> trigerEvents;
    private TextView tvCarpool,tvWeather,tvEventName;
    private ProgressBar progressBar;
    private ImageView imgWeather;
    private View btnOpenQCenter;

    private IListener mListener;
    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        btnOpenCarpool = view.findViewById(R.id.btnOpenCarpool);
        btnOpenCalendar =view.findViewById(R.id.btnOpenCalendar);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv);
        tvCarpool = view.findViewById(R.id.tvCarpool);
        tvWeather = view.findViewById(R.id.tvWeather);
        tvEventName= view.findViewById(R.id.tvEventName);
        btnOpenQCenter = view.findViewById(R.id.btnOpenQCenter);
        imgWeather = view.findViewById(R.id.imgWeather);

        btnOpenCarpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(mListener != null)
                   mListener.onCardClick(TAG_CARPOOL);
            }
        });
        btnOpenCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null)
                    mListener.onCardClick(TAG_EVENTS);

            }
        });
        btnOpenQCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null)
                    mListener.onCardClick(TAG_QCENTER);

            }
        });


        progressBar = view.findViewById(R.id.progressBar);

        trigerCars = new ICallBack<Integer>() {
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
        trigerWeather = new ICallBack<String>() {
            @Override
            public void onResponse(String item) {
                tvWeather.setText(item);
            }

            @Override
            public void onError(String err) {

            }
        };
        trigerEvents = new IListCallBack<Event>() {
            @Override
            public void onResponse(List<Event> item) {
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


//        getWeather();
//        getEventsToday();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        trigerCars = null;
        trigerEvents = null;
        trigerWeather = null;
    }

    private void getEventsToday() {
//        BasicApi api = WebDummy.getInstance();
//        api.getEventAtDate(new Date(),trigerEvents);
    }

    private void getWeather() {
//        BasicApi api = WebDummy.getInstance();
//        api.getWeather(trigerWeather);
    }

    private void getNumberOfCars() {
        // TODO: 4/20/2018 check this
        if(progressBar.getVisibility() == View.VISIBLE)
            return;
        tvCarpool.setText("Looking for a ride?");
//        CarpoolApi api = WebDummy.getInstance();
//        api.getNumberOfJourneys(trigerCars);
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof IListener){
            mListener = (IListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement IListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // Dashboard Items for HomeFragment (JOBS, Transportation, Media.. etc)
    private List<DashboardItem> getItems() {
        DashboardItem jobs = new DashboardItem(TAG_JOBS,R.drawable.ic_job_50dp,"JOBS");
        DashboardItem media = new DashboardItem(TAG_MEDIA,R.drawable.ic_media_50dp,"MEDIA");
        DashboardItem transportation = new DashboardItem(TAG_TRANSPORTATION,R.drawable.ic_bus_50dp,"Transportation");
        DashboardItem events = new DashboardItem(TAG_EVENTS,R.drawable.ic_events_new_50dp,"Events");


        jobs.setSummery("Jobs and Internships");
        media.setSummery("Explore Rawabi Media");
        transportation.setSummery("Public Transportation");
        events.setSummery("See events");

        ArrayList<DashboardItem> items = new ArrayList<>();
        items.add(transportation);
        items.add(jobs);
        items.add(media);
        items.add(events);

        return items;
    }

    private boolean isMoving = false;
    private class Holder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView mImageView;
        private TextView mTextView;
        private TextView mSummeryTextView;
        private DashboardItem mDashboardItem;
        public Holder(View itemView)
        {
            super(itemView);

            mImageView= (ImageView)itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
            mTextView=(TextView)itemView.findViewById(R.id.textView);
            mSummeryTextView=(TextView)itemView.findViewById(R.id.summeryTextView);
        }


        public void bind(DashboardItem item)
        {
            mDashboardItem = item;
            mTextView.setText(item.getTitle());
            Glide.with(itemView).load(item.getDrawable()).into(mImageView);
//            mImageView.setImageDrawable(item.getDrawable());
            mSummeryTextView.setText(item.getSummery());
        }

        @Override
        public void onClick(View v)
        {
            if(isMoving) /* prevent click when transition is running */
                return;
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
                    startActivity(new Intent(getContext(),AlbumsListActivity.class),options.toBundle());
                    break;
                case TAG_EVENTS:
                    startActivity(new Intent(getContext(),EventsListActivity.class),options.toBundle());
                    break;

                case TAG_NEWS:
                    if(mListener != null)
                        mListener.onCardClick(TAG_NEWS);
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
        private int mDrawable;
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

        public DashboardItem(String tag, int drawable, String title) {
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

        public int getDrawable() {
            return mDrawable;
        }

        public void setDrawable(int drawable) {
            mDrawable = drawable;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public interface IListener{
        void onCardClick(String tag);
    }
}

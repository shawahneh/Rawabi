package com.techcamp.aauj.rawabi.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.LoginRegisterActivity;
import com.techcamp.aauj.rawabi.activities.listActivities.CalendarActivity;
import com.techcamp.aauj.rawabi.activities.listActivities.JobsListActivity;
import com.techcamp.aauj.rawabi.activities.listActivities.TransportationActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG_JOBS = "tag_jobs";
    private static final String TAG_MEDIA = "tag_media";
    private static final String TAG_TRANSPORTATION = "tag_trans";

    private Button btnOpenCarpool,btnOpenCalendar;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
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
                Intent i = new Intent(getContext(), CalendarActivity.class);
                startActivity(i);
            }
        });


        setupRecyclerView();
        return view;
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
        DashboardItem media2 = new DashboardItem(TAG_MEDIA,getResources().getDrawable(R.drawable.car_notexture),"CARPOOL");

        jobs.setSummery("Find and explore new jobs");
        media.setSummery("explore Rawabi Media");
        transportation.setSummery("See timetable");

        ArrayList<DashboardItem> items = new ArrayList<>();
        items.add(jobs);
        items.add(media);
        items.add(transportation);
        items.add(media2);

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
            itemView.setOnClickListener(this);

            mImageView= (ImageView)itemView.findViewById(R.id.imageView);
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
            switch (mDashboardItem.getTag()){
                case TAG_JOBS:
                    Intent i = new Intent(getContext(),JobsListActivity.class);
                    startActivity(i);
                    break;
                case TAG_TRANSPORTATION:
                    startActivity(new Intent(getContext(),TransportationActivity.class));
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

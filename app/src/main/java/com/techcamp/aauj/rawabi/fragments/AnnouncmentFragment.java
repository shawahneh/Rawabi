package com.techcamp.aauj.rawabi.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.API.AnnouncmentWebApi;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.ITriger;
import com.techcamp.aauj.rawabi.R;

import java.util.ArrayList;


public class AnnouncmentFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private AnnouncmentWebApi mAnnouncmentWebApi = WebApi.getInstance(getContext());
    public AnnouncmentFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_announcment, container, false);

        mProgressBar = view.findViewById(R.id.progressBar);
        mRecyclerView = view.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mProgressBar.setVisibility(View.VISIBLE);
        mAnnouncmentWebApi.getAnnouns(new ITriger<ArrayList<Event>>() {
            @Override
            public void onTriger(ArrayList<Event> value) {
                mProgressBar.setVisibility(View.GONE);
                MyAdapter adapter = new MyAdapter(getContext(),value);
                mRecyclerView.setAdapter(adapter);
            }
        });
        return view;
    }

 public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Event> events;
    public MyAdapter(Context context,ArrayList<Event> events){
        mContext = context;
        this.events = events;
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mEventName,mEventDesc,mEventDate;
        private ImageView mEventImage;

        public ViewHolder(View view) {
            super(view);
            mEventDesc = view.findViewById(R.id.eventDescTestView);
            mEventName = view.findViewById(R.id.eventNameTextView);
            mEventDate = view.findViewById(R.id.eventDateTextView);

            mEventImage = view.findViewById(R.id.imageView);
        }

        public TextView getmEventName() {
            return mEventName;
        }

        public void setmEventName(TextView mEventName) {
            this.mEventName = mEventName;
        }

        public TextView getmEventDesc() {
            return mEventDesc;
        }

        public void setmEventDesc(TextView mEventDesc) {
            this.mEventDesc = mEventDesc;
        }

        public ImageView getmEventImage() {
            return mEventImage;
        }

        public void setmEventImage(ImageView mEventImage) {
            this.mEventImage = mEventImage;
        }

        public TextView getmEventDate() {
            return mEventDate;
        }

        public void setmEventDate(TextView mEventDate) {
            this.mEventDate = mEventDate;
        }
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_event, parent, false);

        MyAdapter.ViewHolder vh = new MyAdapter.ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.getmEventDesc().setText(event.getDescription());
        holder.getmEventName().setText(event.getName());
        holder.getmEventDate().setText(event.getDate().toString());

        Glide.with(mContext).load(event.getImageUrl()).into(holder.getmEventImage());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
}

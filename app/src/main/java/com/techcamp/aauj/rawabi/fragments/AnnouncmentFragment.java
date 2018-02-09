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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.API.BasicApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Announcement;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.R;

import java.util.ArrayList;


public class AnnouncmentFragment extends Fragment implements IResponeTriger<ArrayList<Announcement>> {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private BasicApi mAnnouncmentWebApi = WebService.getInstance(getContext());
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
        mAnnouncmentWebApi.getAnnouns(this);
        return view;
    }

    @Override
    public void onResponse(ArrayList<Announcement> value) {
        mProgressBar.setVisibility(View.GONE);
        MyAdapter adapter = new MyAdapter(getContext(),value);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onError(String err) {
        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Announcement> events;
    public MyAdapter(Context context,ArrayList<Announcement> events){
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
                .inflate(R.layout.row_announcment, parent, false);

        MyAdapter.ViewHolder vh = new MyAdapter.ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        Announcement event = events.get(position);
        holder.getmEventDesc().setText(event.getDescription());
        holder.getmEventName().setText(event.getName());
        holder.getmEventDate().setText(event.getRealDate());

        Glide.with(mContext).load(event.getImageUrl()).into(holder.getmEventImage());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
}

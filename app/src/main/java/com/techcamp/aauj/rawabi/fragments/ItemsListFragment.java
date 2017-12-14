package com.techcamp.aauj.rawabi.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.API.AnnouncmentWebApi;
import com.techcamp.aauj.rawabi.API.PoolingJourney;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.ITriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.utils.DateUtil;
import com.techcamp.aauj.rawabi.utils.MapUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsListFragment<T extends Journey> extends Fragment {
    private IFragmentListener mListener;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private LatLng mLatLng;
    private PoolingJourney mPoolingJourney = WebApi.getInstance(getContext());
    public ItemsListFragment() {
    }
    public static ItemsListFragment getInstance(LatLng latLng){
        ItemsListFragment fragment = new ItemsListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("key",latLng);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLatLng = getArguments().getParcelable("key");
        }
    }

    public void refreshView(LatLng latLng){
        mLatLng = latLng;
        mProgressBar.setVisibility(View.VISIBLE);

        mPoolingJourney.filterJourneys(mLatLng,mLatLng,new Date(),0, new IResponeTriger<ArrayList<Journey>>() {
            @Override
            public void onResponse(ArrayList<Journey> value) {
                mProgressBar.setVisibility(View.GONE);
                MyAdapter adapter = new MyAdapter(getContext(),value);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String err) {

            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_announcment, container, false);

        mProgressBar = view.findViewById(R.id.progressBar);
        mRecyclerView = view.findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(layoutManager);
refreshView(mLatLng);

        return view;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private Context mContext;
        private ArrayList<Journey> objs;
        public MyAdapter(Context context,ArrayList<Journey> objs){
            mContext = context;
            this.objs = objs;
        }
        public  class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextViewName,mTextViewAvailable,mTextViewDistance,mTextViewTo,mTextViewRating;
            public ImageView mImage;
            public RatingBar mRatingBar;

            public ViewHolder(View view) {
                super(view);
                mTextViewName = view.findViewById(R.id.tvName);
                mTextViewAvailable = view.findViewById(R.id.tvAvailable);
                mTextViewDistance = view.findViewById(R.id.tvDistance);
                mTextViewTo = view.findViewById(R.id.tvTo);
                mTextViewRating = view.findViewById(R.id.tvRating);
                mImage = view.findViewById(R.id.profile_image);
                mRatingBar = view.findViewById(R.id.rb1);

            }

        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                           int viewType) {
            View view =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_journey, parent, false);

            MyAdapter.ViewHolder vh = new MyAdapter.ViewHolder(view);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            final Journey obj = objs.get(position);
            holder.mTextViewName.setText(obj.getUser().getFullname());
            holder.mTextViewAvailable.setText("Going at" + DateUtil.formatDate(obj.getGoingDate().getTime()));
            holder.mTextViewDistance.setText(MapUtil.getDistance(obj.getStartPoint(),obj.getEndPoint()) + " away");
            holder.mTextViewRating.setText("5/5");
            if(obj.getEndPoint() != null)
            holder.mTextViewTo.setText(MapUtil.getAddress(getContext(),obj.getEndPoint().latitude,obj.getEndPoint().longitude));
            Glide.with(mContext).load(obj.getUser().getImageurl()).into(holder.mImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null)
                        mListener.onJourneyClick(obj);
                }
            });
        }

        @Override
        public int getItemCount() {
            return objs.size();
        }
    }
    public interface IFragmentListener{
        void onJourneyClick(Journey journey);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentListener) {
            mListener = (IFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

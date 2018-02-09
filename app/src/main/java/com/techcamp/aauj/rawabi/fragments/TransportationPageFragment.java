package com.techcamp.aauj.rawabi.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techcamp.aauj.rawabi.API.BasicApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Transportation;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.R;

import java.util.ArrayList;


public class TransportationPageFragment extends Fragment {
    private RecyclerView rvFromRawabi,rvFromRamallah;
    private BasicApi api = WebService.getInstance(getContext());
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    private IResponeTriger<Transportation> triger;
    public TransportationPageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transportation_page, container, false);
        rvFromRamallah = view.findViewById(R.id.rvFromRamallah);
        rvFromRawabi = view.findViewById(R.id.rvFromRawabi);
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        triger = new IResponeTriger<Transportation>() {
            @Override
            public void onResponse(Transportation item) {
                mSwipeRefreshLayout.setRefreshing(false);
                MyAdapter adapterFromRamallah = new MyAdapter(getContext(),item.getFromRamallah());
                MyAdapter adapterFromRawabi = new MyAdapter(getContext(),item.getFromRawabi());

                rvFromRamallah.setAdapter(adapterFromRamallah);
                rvFromRawabi.setAdapter(adapterFromRawabi);
            }

            @Override
            public void onError(String err) {

            }
        };
        setupRecyclersView();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTransportation();
            }
        });
        mSwipeRefreshLayout.setRefreshing(true);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        triger = null;
    }

    private void setupRecyclersView() {
        rvFromRamallah.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFromRawabi.setLayoutManager(new LinearLayoutManager(getContext()));

        rvFromRawabi.setHasFixedSize(true);
        rvFromRamallah.setHasFixedSize(true);

        loadTransportation();
    }

    private void loadTransportation() {
        api.getTransportation(triger);
    }



    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
        private Context mContext;
        private ArrayList<String> times;
        public MyAdapter(Context context,ArrayList<String> times){
            mContext = context;
            this.times = times;
        }
        public  class MyHolder extends RecyclerView.ViewHolder {
            private TextView tvTime;
            public MyHolder(View view) {
                super(view);
                tvTime = view.findViewById(R.id.tvTime);
            }
            public void bind(String txt){
                tvTime.setText(txt);
            }
        }

        @Override
        public MyAdapter.MyHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            View view =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_time, parent, false);
            return new MyHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            holder.bind(times.get(position));
        }

        @Override
        public int getItemCount() {
            return times.size();
        }
    }

}

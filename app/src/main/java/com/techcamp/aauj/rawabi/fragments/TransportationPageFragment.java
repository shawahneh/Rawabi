package com.techcamp.aauj.rawabi.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.techcamp.aauj.rawabi.Beans.TransportationElement;
import com.techcamp.aauj.rawabi.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.database.TransportationDB;

import java.util.ArrayList;
import java.util.List;


public class TransportationPageFragment extends Fragment {
    private RecyclerView rvFromRawabi,rvFromRamallah;
    private BasicApi api = WebService.getInstance(getContext());
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    private ICallBack<Transportation> triger;
    public TransportationPageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transportation_page, container, false);
        rvFromRamallah = view.findViewById(R.id.rvFromRamallah);
        rvFromRawabi = view.findViewById(R.id.rvFromRawabi);
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        triger = new ICallBack<Transportation>() {
            @Override
            public void onResponse(Transportation item) {
                mSwipeRefreshLayout.setRefreshing(false);

                loadListToAdapter(item);

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

    private void loadListToAdapter(Transportation item) {
        MyAdapter adapterFromRamallah = new MyAdapter(getContext(),item.getFromRamallah());
        MyAdapter adapterFromRawabi = new MyAdapter(getContext(),item.getFromRawabi());

        rvFromRamallah.setAdapter(adapterFromRamallah);
        rvFromRawabi.setAdapter(adapterFromRawabi);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFromDatabase();
    }

    private void loadFromDatabase() {
        List<TransportationElement> listFromRawabi = TransportationDB.getInstance(getContext()).getAllByType(TransportationElement.TYPE_FROM_RAWABI);
        List<TransportationElement> listFromRamallah = TransportationDB.getInstance(getContext()).getAllByType(TransportationElement.TYPE_FROM_RAMALLAH);

        Transportation transportation = new Transportation();
        transportation.setFromRamallah( listFromRamallah);
        transportation.setFromRawabi(listFromRawabi);


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
        private List<TransportationElement> times;
        public MyAdapter(Context context,List<TransportationElement> times){
            mContext = context;
            this.times = times;
        }
        public  class MyHolder extends RecyclerView.ViewHolder {
            private TextView tvTime;
            public MyHolder(View view) {
                super(view);
                tvTime = view.findViewById(R.id.tvTime);
            }
            public void bind(TransportationElement transportationElement){
                tvTime.setText(transportationElement.getTime());
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

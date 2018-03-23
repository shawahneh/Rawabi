package com.techcamp.aauj.rawabi.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.techcamp.aauj.rawabi.API.BasicApi;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.model.Transportation;
import com.techcamp.aauj.rawabi.model.TransportationElement;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.database.TransportationDB;

import java.util.List;


public class TransportationPageFragment extends Fragment {
    private RecyclerView rvFromRawabi,rvFromRamallah;
    private BasicApi api = WebApi.getInstance();
//    protected SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBarLoading;
    private ICallBack<Transportation> trigger;
    public TransportationPageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transportation_page, container, false);
        rvFromRamallah = view.findViewById(R.id.rvFromRamallah);
        rvFromRawabi = view.findViewById(R.id.rvFromRawabi);
        progressBarLoading = view.findViewById(R.id.progressBarLoading);
//        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        // transportation available from
        trigger = new ICallBack<Transportation>() {
            @Override
            public void onResponse(Transportation item) {
//                mSwipeRefreshLayout.setRefreshing(false);
                progressBarLoading.setVisibility(View.GONE);

                // clear database
                TransportationDB.getInstance(getContext()).deleteAll();

                // save to database
                for (TransportationElement tr :
                        item.getFromRamallah()) {
                    TransportationDB.getInstance(getContext()).saveBean(tr);
                }
                for (TransportationElement tr :
                        item.getFromRawabi()) {
                    TransportationDB.getInstance(getContext()).saveBean(tr);
                }

                loadListToAdapter(item);

            }

            @Override
            public void onError(String err) {
                progressBarLoading.setVisibility(View.GONE);
                if(getView() != null)
                    Snackbar.make(getView(),err,Snackbar.LENGTH_SHORT) .show();
//                mSwipeRefreshLayout.setRefreshing(false);
            }
        };
        setupRecyclersView();
        return view;
    }

    private void loadListToAdapter(Transportation item) {
        MyAdapter adapterFromRamallah = new MyAdapter(getContext(),item.getFromRamallah());
        MyAdapter adapterFromRawabi = new MyAdapter(getContext(),item.getFromRawabi());

        rvFromRamallah.setAdapter(adapterFromRamallah);
        rvFromRawabi.setAdapter(adapterFromRawabi);
    }


    private void loadFromDatabase() {
        List<TransportationElement> listFromRawabi = TransportationDB.getInstance(getContext()).getAllByType(TransportationElement.TYPE_FROM_RAWABI);
        List<TransportationElement> listFromRamallah = TransportationDB.getInstance(getContext()).getAllByType(TransportationElement.TYPE_FROM_RAMALLAH);

        Transportation transportation = new Transportation();
        transportation.setFromRamallah( listFromRamallah);
        transportation.setFromRawabi(listFromRawabi);

        loadListToAdapter(transportation);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        trigger = null;
    }

    private void setupRecyclersView() {
        rvFromRamallah.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFromRawabi.setLayoutManager(new LinearLayoutManager(getContext()));

        rvFromRawabi.setHasFixedSize(true);
        rvFromRamallah.setHasFixedSize(true);

        loadFromDatabase();
        loadTransportationFromWeb();
    }

    private void loadTransportationFromWeb() {
        progressBarLoading.setVisibility(View.VISIBLE);
        api.getTransportation(trigger);
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

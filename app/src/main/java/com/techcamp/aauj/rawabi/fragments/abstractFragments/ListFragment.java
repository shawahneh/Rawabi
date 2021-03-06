package com.techcamp.aauj.rawabi.fragments.abstractFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.R;


public abstract class ListFragment extends Fragment {
    public static final String ARG_ITEM = "item";
    protected RecyclerView mRecyclerView;
    protected ProgressBar progressBarLoading;
    protected View layoutMessage;
    protected ImageView imgMessage;
    protected TextView tvMessage;
    protected IFragmentListener mListener;
    public ListFragment() {
    }

    private void initRecyclerView(){
        mRecyclerView.setHasFixedSize(true);
        int numberOfCols = getNumberOfCols();
        if(numberOfCols <= 1)
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        else
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),numberOfCols));
    }

    protected int getNumberOfCols(){
        return 1;
    }
    public abstract void setupRecyclerViewAdapter();
    protected abstract void loadDataFromWeb();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.rv);
        layoutMessage = view.findViewById(R.id.layoutMessage);
        imgMessage = view.findViewById(R.id.imgMessage);
        tvMessage = view.findViewById(R.id.tvMessage);
        progressBarLoading = view.findViewById(R.id.progressBarLoading);

        hideMessageLayout();
        initRecyclerView();
        setupRecyclerViewAdapter();
        loadDataFromWeb();

        checkConnection();
    }

    private void checkConnection() {
//        if(!MapUtil.isConnectionAvailable(getContext()))
//            showMessageLayout("No Internet Connection",R.drawable.ic_signal_wifi_off_black_48dp);
    }

    protected void showMessageLayout(String msg, String url){
        if(isAdded()) {
            tvMessage.setText(msg);
            if (url != null)
                Glide.with(getContext()).load(url).into(imgMessage);

            layoutMessage.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }
    protected void showMessageLayout(String msg, int drawable){
        if(isAdded()){

        tvMessage.setText(msg);
            Glide.with(getContext()).load(drawable).into(imgMessage);

        layoutMessage.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        }
    }
    protected void hideMessageLayout(){
        layoutMessage.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof IFragmentListener){
            mListener = (IFragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement IFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    protected void setLoading(boolean loading){
        progressBarLoading.setVisibility(loading?View.VISIBLE:View.GONE);
    }


    public interface IFragmentListener<T>{
        void onItemClicked(T item);
    }




}

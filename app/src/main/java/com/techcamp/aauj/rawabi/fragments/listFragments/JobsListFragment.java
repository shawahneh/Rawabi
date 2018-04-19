package com.techcamp.aauj.rawabi.fragments.listFragments;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.API.BasicApi;
import com.techcamp.aauj.rawabi.API.WebOffline;
import com.techcamp.aauj.rawabi.API.services.OfflineApi;
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.API.services.RequestService;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.Job;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.abstractAdapters.RecyclerAdapter;
import com.techcamp.aauj.rawabi.database.JobsDB;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.utils.DateUtil;

import java.util.List;

/**
 * Created by ALa on 12/31/2017.
 */

public class JobsListFragment extends ListFragment implements IListCallBack<Job> {
    private RequestService requestService;
    private MyAdapter mAdapter;
    public static Fragment newInstance(){
        Fragment fragment = new JobsListFragment();
        return fragment;
    }
    @Override
    public void setupRecyclerViewAdapter() {
        if(mAdapter == null)
            mAdapter = new MyAdapter(null);
        mRecyclerView.setAdapter(mAdapter);

        loadOffline();
    }

    @Override
    protected void loadDataFromWeb() {
        requestService = WebService.getInstance().getJobs(this)
                .saveOffline(WebOffline.CODE_JOBS)
                .start();

        setLoading(true);
    }


    private void loadOffline() {
        List<Job> list = new WebOffline().getJobs(getContext());
        loadListToAdapter(list);
    }

    private void loadListToAdapter(List<Job> list) {
        if(isAdded()){{
                hideMessageLayout();
                if(mAdapter != null)
                mAdapter.setList(list);

        }}
    }

    // data available from WEB
    @Override
    public void onResponse(List<Job> value) {
        setLoading(false);

            if(value.size() <= 0){
//                showMessageLayout("No Jobs available",R.drawable.ic_signal_wifi_off_black_48dp);
            }else {
                hideMessageLayout();
                loadListToAdapter(value);
            }

    }


    @Override
    public void onError(String err) {
        if(getView() != null)
            Snackbar.make(getView(),err,Snackbar.LENGTH_SHORT) .show();
        setLoading(false);
    }


    @Override
    public void onDestroy() {
        if(requestService != null)
            requestService.cancel();
        super.onDestroy();
    }



    private class MyHolder extends com.techcamp.aauj.rawabi.abstractAdapters.Holder<Job> {
        private TextView mEventName,mEventDesc,mEventDate,tvCompanyName;
        public MyHolder(View view) {
            super(view);
            mEventDesc = view.findViewById(R.id.eventDescTestView);
            mEventName = view.findViewById(R.id.eventNameTextView);
            mEventDate = view.findViewById(R.id.eventDateTextView);
            tvCompanyName = view.findViewById(R.id.tvCompanyName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void bind(Job job, int pos) {
            super.bind(job,pos);
            mEventDesc.setText(job.getDescription());
            mEventDate.setText(DateUtil.getRelativeTime(job.getDate()));
            mEventName.setText(job.getName());
            tvCompanyName.setText(job.getCompanyName());
        }
        @Override
        public void onClicked(View v) {
            if(mListener != null)
                mListener.onItemClicked(mItem);
        }

    }
    private class MyAdapter extends RecyclerAdapter<Job> {

        public MyAdapter(List<Job> items) {
            super(items);
        }

        @Override
        public int getLayoutId() {
            return R.layout.row_job;
        }

        @Override
        public com.techcamp.aauj.rawabi.abstractAdapters.Holder getNewHolder(View v) {
            return new MyHolder(v);
        }
    }

}

package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.fragments.ItemsListFragment;
import com.techcamp.aauj.rawabi.utils.DateUtil;
import com.techcamp.aauj.rawabi.utils.MapUtil;

import java.util.ArrayList;

public abstract class ListActivity<T> extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecyclerView = (RecyclerView)findViewById(R.id.rv);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setupRecyclerViewAdapter(mRecyclerView);
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupRecyclerViewAdapter(mRecyclerView);


    }

    protected abstract void setupRecyclerViewAdapter(RecyclerView mRecyclerView);

    public abstract class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> {
        private Context mContext;
        private ArrayList<T> objs;

        public MyAdapter(Context mContext, ArrayList<T> objs) {
            this.mContext = mContext;
            this.objs = objs;
        }

        protected abstract int getLayout();
        protected abstract MyAdapter.ItemViewHolder getHolder(View view);

        public abstract class ItemViewHolder extends RecyclerView.ViewHolder {
            public ItemViewHolder(View view) {
                super(view);
            }
            public abstract void bind(T t);

        }
        protected abstract void onItemClick(T item, int pos);

        @Override
        public MyAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
            View view =  LayoutInflater.from(parent.getContext())
                    .inflate(getLayout(), parent, false);

            MyAdapter.ItemViewHolder vh = getHolder(view);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyAdapter.ItemViewHolder holder, final int position) {
            final T item =  objs.get(position);
            holder.bind(objs.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick(item,position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return objs.size();
        }
    }

}

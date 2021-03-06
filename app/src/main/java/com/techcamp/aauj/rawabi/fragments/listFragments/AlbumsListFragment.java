package com.techcamp.aauj.rawabi.fragments.listFragments;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.API.WebOffline;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.API.services.OfflineApi;
import com.techcamp.aauj.rawabi.API.services.RequestService;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.abstractAdapters.Holder;
import com.techcamp.aauj.rawabi.abstractAdapters.RecyclerAdapter;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.model.AlbumItem;
import com.techcamp.aauj.rawabi.model.Job;

import java.util.List;

public class AlbumsListFragment extends ListFragment {

    RequestService requestService;
    @Override
    protected int getNumberOfCols() {
        return 3;
    }

    @Override
    public void setupRecyclerViewAdapter() {

        /*  load offline data */
            List<AlbumItem> list = WebFactory.getOfflineService().getAlbums(getContext());
            loadListToAdapter(list);

    }

    @Override
    public void onDestroy() {
        if(requestService != null)
            requestService.cancel();
        super.onDestroy();
    }

    @Override
    protected void loadDataFromWeb() {
        progressBarLoading.setVisibility(View.VISIBLE);
        requestService = WebService.getInstance().getAlbums(new IListCallBack<AlbumItem>() {
            @Override
            public void onResponse(List<AlbumItem> item) {
                progressBarLoading.setVisibility(View.GONE);
                loadListToAdapter(item);
            }

            @Override
            public void onError(String err) {
                progressBarLoading.setVisibility(View.GONE);
                if(getView() != null)
                Snackbar.make(getView(),err,Snackbar.LENGTH_SHORT).show();
            }
        })
        .saveOffline(WebOffline.CODE_ALBUMS)
        .start();
    }

    private void loadListToAdapter(List<AlbumItem> list) {
        if(isAdded()){{
            //  available

                hideMessageLayout();
                MyAdapter adapter = new MyAdapter(list);
                mRecyclerView.setAdapter(adapter);

        }}
    }

    private class MyHolder extends Holder<AlbumItem>{
        private TextView tvTitle;
        private ImageView imageView;
        public MyHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        @Override
        public void bind(AlbumItem item, int pos) {
            super.bind(item, pos);
            tvTitle.setText(item.getTitle());
            Glide.with(itemView.getContext()).load(item.getImgUrl()).into(imageView);
        }

        @Override
        public void onClicked(View v) {
            if(mListener != null)
                mListener.onItemClicked(mItem);
        }
    }

    private class MyAdapter extends RecyclerAdapter<AlbumItem>{

        public MyAdapter(List<AlbumItem> items) {
            super(items);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_album;
        }

        @Override
        public Holder getNewHolder(View v) {
            return new MyHolder(v);
        }
    }
}

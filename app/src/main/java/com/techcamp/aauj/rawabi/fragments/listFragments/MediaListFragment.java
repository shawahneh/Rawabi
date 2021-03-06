package com.techcamp.aauj.rawabi.fragments.listFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.stfalcon.frescoimageviewer.ImageViewer;
import com.techcamp.aauj.rawabi.API.BasicApi;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.API.WebOffline;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.API.services.RequestService;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.AlbumItem;
import com.techcamp.aauj.rawabi.model.MediaItem;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.abstractAdapters.Holder;
import com.techcamp.aauj.rawabi.abstractAdapters.RecyclerAdapter;
import com.techcamp.aauj.rawabi.database.MediaItemsDB;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;

import java.util.List;

/**
 * Created by ALa on 2/3/2018.
 */

/**
 * this fragment receives an albumItem object, and it loads all images of that album from web and shows them in a list
 */
public class MediaListFragment extends ListFragment implements IListCallBack<MediaItem> {
    private MyAdapter mAdapter;
    private AlbumItem mAlbum;
    private IListener mListener;
    public static Fragment newInstance(AlbumItem albumItem){
        Fragment fragment = new MediaListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_ITEM, albumItem);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getNumberOfCols() {
        return 3;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlbum = getArguments().getParcelable(ARG_ITEM);
    }

    @Override
    public void setupRecyclerViewAdapter() {
        if(mAdapter == null)
            mAdapter = new MyAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
        // initialize fresco
        Fresco.initialize(getActivity().getApplicationContext());

    }

    @Override
    protected void loadDataFromWeb() {
        BasicApi api = WebFactory.getBasicService();
        api.getGalleryForAlbum(mAlbum.getId(),this)
            .start();

        setLoading(true);
    }



    private void loadListToAdapter(List<MediaItem> list) {
        if(isAdded()){{

                hideMessageLayout();
                if(mAdapter != null)
                    mAdapter.setList(list);

        }}
    }

    @Override
    public void onResponse(List<MediaItem> value) {
        setLoading(false);
        loadListToAdapter(value);

    }

    @Override
    public void onError(String err) {
        if(getView() != null)
        Snackbar.make(getView(),err,Snackbar.LENGTH_SHORT) .show();
        setLoading(false);
    }


    private class MyAdapter extends RecyclerAdapter<MediaItem> {
        private class MyHolder extends Holder<MediaItem> {
            private ImageView mImage;
            public MyHolder(View view) {
                super(view);
                mImage = view.findViewById(R.id.imageView);
                itemView.setOnClickListener(this);
            }

            @Override
            public void bind(MediaItem item, int pos) {
                super.bind(item,pos);
                if(item.getImageUrl() != null)
                    Glide.with(getContext()).load(item.getImageUrl()).into(mImage);
            }
            @Override
            public void onClicked(View v) {
                // TODO: 4/19/2018 check this
                new ImageViewer.Builder<>(getContext(), mItems)
                        .setFormatter(new ImageViewer.Formatter<MediaItem>() {
                            @Override
                            public String format(MediaItem mediaItem) {
                                return mediaItem.getImageUrl();
                            }
                        })
                        .setStartPosition(getAdapterPosition())
                        .show();
            }

        }

        public MyAdapter(List<MediaItem> items) {
            super(items);
        }

        @Override
        public int getLayoutId() {
            return R.layout.row_media;
        }

        @Override
        public Holder getNewHolder(View v) {
            return new MyHolder(v);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mListener != null)
            mListener.onFragmentStopped();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof IListener){
            mListener = (IListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement IListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface IListener{
        void onMediaClicked(MediaItem item);
        void onFragmentStopped();
    }
}

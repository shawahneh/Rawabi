package com.techcamp.aauj.rawabi.fragments.ItemDetailsFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.Beans.Announcement;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.ScrollingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncmentDetailsFragment extends ItemDetailsFragment<Announcement> {
    private TextView tvName, tvDescription, tvDate;
    private ImageView imageView;
    public AnnouncmentDetailsFragment() {
    }




    public static ItemDetailsFragment newInstance(Announcement announcement) {
        ItemDetailsFragment fragment = new AnnouncmentDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ScrollingActivity.ARG_ITEM_ID, announcement);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcment_details, container, false);
        Log.d("tag","onCreateView");
        tvName = view.findViewById(R.id.tvName);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvDate = view.findViewById(R.id.tvDate);
        imageView = view.findViewById(R.id.imageView);

        refreshView();
        return view;
    }

    @Override
    protected void refreshView() {
        tvName.setText(mItem.getName());
        tvDescription.setText(mItem.getDescription());
        tvDate.setText(DateUtils.getRelativeDateTimeString(getContext(), mItem.getDate().getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0));
        if (mItem.getImageUrl() != null) {
            Glide.with(getContext()).load(mItem.getImageUrl()).into(imageView);
        }
    }
}
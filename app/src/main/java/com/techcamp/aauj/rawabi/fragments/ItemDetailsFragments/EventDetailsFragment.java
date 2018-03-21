package com.techcamp.aauj.rawabi.fragments.ItemDetailsFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.model.Event;
import com.techcamp.aauj.rawabi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends ItemDetailsFragment<Event> {

    private TextView tvName, tvDescription, tvDate;
    private ImageView imageView;
    public EventDetailsFragment() {
    }




    public static ItemDetailsFragment newInstance(Event event) {
        ItemDetailsFragment fragment = new EventDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_ITEM, event);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
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

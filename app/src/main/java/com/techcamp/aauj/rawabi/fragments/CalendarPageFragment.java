package com.techcamp.aauj.rawabi.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.API.CalendarWebApi;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.ITriger;
import com.techcamp.aauj.rawabi.R;
import java.util.Date;


public class CalendarPageFragment extends Fragment {

    private TextView mEventName,mEventDesc;
    private ImageView mEventImage;
    private OnFragmentInteractionListener mListener;
    private CalendarView mCalendarView;
    private CalendarWebApi mCalendarWebApi = WebApi.getInstance();

    public CalendarPageFragment() {
    }

    public static CalendarPageFragment newInstance() {
        CalendarPageFragment fragment = new CalendarPageFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_calendar_page, container, false);

        mEventDesc = view.findViewById(R.id.eventDescTestView);
        mEventName = view.findViewById(R.id.eventNameTextView);
        mEventImage = view.findViewById(R.id.imageView);
        mCalendarView = view.findViewById(R.id.calendarView);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                DateChangeClick(new Date(i,i1,i2));
            }
        });
        DateChangeClick(new Date());
        return view;
    }

    private void DateChangeClick(Date date) {
        mCalendarWebApi.getEventAtDate(date, new ITriger<Event>() {
            @Override
            public void onTriger(Event value) {
                mEventName.setText(value.getName());
                mEventDesc.setText(value.getDescription());
                DownloadImage(value.getImageUrl());
            }
        });
    }

    private void DownloadImage(String imageUrl) {
        Glide.with(this).load(imageUrl).into(mEventImage);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

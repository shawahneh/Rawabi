package com.techcamp.aauj.rawabi.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.techcamp.aauj.rawabi.R;


public class CalendarPageFragment extends Fragment {

    private TextView mEventName,mEventDesc;
    private ImageView mEventImage;
    private OnFragmentInteractionListener mListener;

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




        return view;
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

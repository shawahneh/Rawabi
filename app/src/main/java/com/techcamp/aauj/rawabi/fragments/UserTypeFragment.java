package com.techcamp.aauj.rawabi.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.transition.AutoTransition;
import android.support.v4.app.Fragment;
import android.transition.Fade;
import android.support.transition.Transition;
import android.transition.TransitionManager;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techcamp.aauj.rawabi.R;

public class UserTypeFragment extends Fragment {
    public static int MODE = 0;
//    private OnFragmentInteractionListener mListener;
    private View view1,view2,imgRider,layoutNowLater;
    private TextView tvRider;
    private ViewGroup container;
    public UserTypeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_type, container, false);

            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            container = view.findViewById(R.id.container);
            tvRider = view.findViewById(R.id.tvRider);
            imgRider = view.findViewById(R.id.imgRider);
            layoutNowLater = view.findViewById(R.id.layoutNowLater);

        final ViewGroup finalContainer = container;

        view2.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View view) {
//                    Transition transition = new AutoTransition();
//                    transition.setDuration(200);
//                    android.support.transition.TransitionManager.beginDelayedTransition(finalContainer,transition);
//                    view1.setVisibility(View.GONE);
                    MODE = 1;

                }
            });
        view1.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
//                Transition transition = new AutoTransition();
//                transition.setDuration(200);
//                android.support.transition.TransitionManager.beginDelayedTransition(finalContainer,transition);
//                view2.setVisibility(View.GONE);
//                MODE = 0;
//                tvRider.setText("Ride");
//                imgRider.setVisibility(View.GONE);
//                layoutNowLater.setVisibility(View.VISIBLE);
                MODE = 0;

            }
        });

        return view;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}

package com.techcamp.aauj.rawabi.garbage;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.model.Journey;
import com.techcamp.aauj.rawabi.callBacks.ITrigger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.utils.MapUtil;

/**
 * Created by ALa on 11/17/2017.
 */

public class DriverDetailDialogFragment extends DialogFragment {
    private Journey mJourney;
    public static final String J_PARAM = "jparam";
    private TextView mTextViewName,mTextViewPhone,mTextViewAvilable,mTextViewDistance,
        mTextViewFrom,mTextViewTo,mTextViewCarDesc;
    private MapView mMapView;
    private Button mButtonChoose,mButtonCall,mButtonCancel;
    private ImageView mImageView;
    private ITrigger<Journey> mListener;

    public ITrigger<Journey> getmListener() {
        return mListener;
    }

    public void setmListener(ITrigger<Journey> mListener) {
        this.mListener = mListener;
    }

    public static DriverDetailDialogFragment newInstance(Journey mJourney, ITrigger<Journey> clickListener) {
        Bundle args = new Bundle();
        args.putParcelable(J_PARAM,mJourney);
        DriverDetailDialogFragment fragment = new DriverDetailDialogFragment();
        fragment.setmListener(clickListener);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            Window window = dialog.getWindow();
//            WindowManager.LayoutParams wlp = window.getAttributes();
//
//            wlp.gravity = Gravity.BOTTOM;
//            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//            window.setAttributes(wlp);
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mJourney = getArguments().getParcelable(J_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_driver_detail, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextViewName = view.findViewById(R.id.tvName);
        mTextViewPhone =view.findViewById(R.id.tvPhone);
        mTextViewAvilable =view.findViewById(R.id.tvAvailable);
        mTextViewDistance =view.findViewById(R.id.tvDistance);
        mTextViewTo =view.findViewById(R.id.tvTo);
        mTextViewFrom =view.findViewById(R.id.tvFrom);
        mTextViewCarDesc=view.findViewById(R.id.tvCarDesc);
        mButtonCancel = view.findViewById(R.id.btnCancel);
        mButtonChoose = view.findViewById(R.id.btnChoose);
        mImageView = view.findViewById(R.id.profile_image);


    }



    private double getDistance() {
        LatLng start = mJourney.getStartPoint();
        LatLng end = mJourney.getEndPoint();
        return MapUtil.getDistance(start,end);
    }


}

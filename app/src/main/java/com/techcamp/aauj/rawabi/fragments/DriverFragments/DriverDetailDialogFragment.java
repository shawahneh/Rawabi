package com.techcamp.aauj.rawabi.fragments.DriverFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.ITriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.utils.DateUtil;
import com.techcamp.aauj.rawabi.utils.MapUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by alaam on 11/17/2017.
 */

public class DriverDetailDialogFragment extends DialogFragment {
    private Journey mJourney;
    public static final String J_PARAM = "jparam";
    private TextView mTextViewName,mTextViewPhone,mTextViewAvilable,mTextViewDistance,
        mTextViewFrom,mTextViewTo,mTextViewCarDesc;
    private MapView mMapView;
    private Button mButtonChoose,mButtonCall,mButtonCancel;
    private ImageView mImageView;
    private ITriger<Journey> mListener;

    public ITriger<Journey> getmListener() {
        return mListener;
    }

    public void setmListener(ITriger<Journey> mListener) {
        this.mListener = mListener;
    }

    public static DriverDetailDialogFragment newInstance(Journey mJourney, ITriger<Journey> clickListener) {
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

        User user = mJourney.getUser();
        Log.d("tag","username: " + user.getFullname());
        mTextViewName.setText(user.getFullname());
        mTextViewPhone.setText(user.getPhone());

        String date = DateUtil.formatDate(mJourney.getGoingDate().getTime());

        mTextViewAvilable.setText("Available at " + date);
        mTextViewDistance.setText(getDistance()+" Distance");
        if(mJourney.getStartPoint() != null)
        mTextViewTo.setText(getLocName(mJourney.getStartPoint().latitude,mJourney.getStartPoint().longitude));
        if(mJourney.getEndPoint() != null)
        mTextViewFrom.setText(getLocName(mJourney.getEndPoint().latitude,mJourney.getEndPoint().longitude));

        mTextViewCarDesc.setText(mJourney.getCarDescription());
        Glide.with(getActivity()).load(mJourney.getUser().getImageurl()).into(mImageView);

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        mButtonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null)
                    mListener.onTriger(mJourney);
            }
        });
    }

    private String getLocName(double endLocationX, double endLocationY) {
       return MapUtil.getAddress(getActivity(),endLocationX,endLocationY);
    }

    private double getDistance() {
        LatLng start = mJourney.getStartPoint();
        LatLng end = mJourney.getEndPoint();
        return MapUtil.getDistance(start,end);
    }


}

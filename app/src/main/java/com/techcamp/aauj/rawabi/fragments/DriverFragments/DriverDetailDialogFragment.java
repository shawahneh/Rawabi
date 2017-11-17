package com.techcamp.aauj.rawabi.fragments.DriverFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.R;

/**
 * Created by alaam on 11/17/2017.
 */

public class DriverDetailDialogFragment extends DialogFragment {
    private Journey mJourney;
    public static final String J_PARAM = "jparam";
    private TextView mTextViewName,mTextViewPhone,mTextViewAvilable,mTextViewDistance,
        mTextViewFrom,mTextViewTo;
    private MapView mMapView;
    private Button mButtonSendRequest,mButtonCall,mButtonCancel;
    public static DriverDetailDialogFragment newInstance(Journey mJourney) {
        Bundle args = new Bundle();
        args.putParcelable(J_PARAM,mJourney);
        DriverDetailDialogFragment fragment = new DriverDetailDialogFragment();
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

        mMapView = view.findViewById(R.id.mapView);
        mButtonCancel = view.findViewById(R.id.btnCancel);

        User user = mJourney.getUser();
        Log.d("tag","username: " + user.getFullname());
        mTextViewName.setText(user.getFullname());
        mTextViewPhone.setText(user.getPhone());
        mTextViewAvilable.setText("Available at" + mJourney.getGoingDate().toString());
        mTextViewDistance.setText(getDistance()+"");
        mTextViewTo.setText(getLocName(mJourney.getEndLocationX(),mJourney.getEndlocationY()));
        mTextViewFrom.setText(getLocName(mJourney.getStartLocationX(),mJourney.getStartLocationY()));

        MapsInitializer.initialize(this.getActivity());

        mMapView.onCreate(getDialog().onSaveInstanceState());
        mMapView.onResume();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng latLng = new LatLng(mJourney.getEndLocationX(),mJourney.getEndlocationY());
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }

    private String getLocName(double endLocationX, double endLocationY) {
        return null;
    }

    private String getDistance() {
        return "distance";
    }
}

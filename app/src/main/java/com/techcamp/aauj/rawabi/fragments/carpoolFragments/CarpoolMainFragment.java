package com.techcamp.aauj.rawabi.fragments.carpoolFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.techcamp.aauj.rawabi.model.User;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.MapDriverActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.MapRiderActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.ProfileActivity;
import com.techcamp.aauj.rawabi.controllers.SPController;

public class CarpoolMainFragment extends Fragment {
    private TextView tvName;
    private ImageView imgProfile;
    private Button btnRider,btnDriver;

    public CarpoolMainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carpool_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tvName = view.findViewById(R.id.tvName);
        imgProfile = view.findViewById(R.id.imgProfile);
        btnRider = view.findViewById(R.id.btnRider);
        btnDriver = view.findViewById(R.id.btnDriver);

        User user = SPController.getLocalUser(getContext());

        if(user != null){
            Glide.with(getContext()).load(user.getImageurl())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_person_black_24dp))
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgProfile);
            tvName.setText(user.getFullname());
        }
        btnDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),MapDriverActivity.class);
                startActivity(i);
            }
        });
        btnRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),MapRiderActivity.class);
                startActivity(i);
            }
        });
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                startActivity(i);
            }
        });
    }
}

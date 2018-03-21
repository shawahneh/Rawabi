package com.techcamp.aauj.rawabi.fragments.carpoolFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.techcamp.aauj.rawabi.model.User;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.DashboardActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.EditProfileActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.MyJourneysListActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.MyRidesListActivity;
import com.techcamp.aauj.rawabi.controllers.SPController;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private TextView tvName,tvEmail;
    private ImageView imageView;
    private View btnMyRides,btnMyJourneys,btnEditProfile,btnLogout;

    public ProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        imageView = view.findViewById(R.id.imageView);

        btnMyRides = view.findViewById(R.id.btnMyRides);
        btnMyJourneys = view.findViewById(R.id.btnMyJourneys);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);

        btnMyRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),MyRidesListActivity.class);
                startActivity(i);
            }
        });
        btnMyJourneys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),MyJourneysListActivity.class);
                startActivity(i);
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),EditProfileActivity.class);
                startActivity(i);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Logout?")
                        .setConfirmText("Logout")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                logout();
                            }
                        })
                        .show();

            }
        });
        refreshDate();
    }

    private void logout() {
        SPController.saveLocalUser(getContext(),null);
        Intent intent = new Intent(getContext(), DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void refreshDate() {
        User user = SPController.getLocalUser(getContext());

        tvName.setText(user.getFullname());
        tvEmail.setText(user.getUsername());


            Glide.with(this).load(user.getImageurl()).apply(RequestOptions.placeholderOf(R.drawable.person)).apply(RequestOptions.circleCropTransform()).into(imageView);

    }
}

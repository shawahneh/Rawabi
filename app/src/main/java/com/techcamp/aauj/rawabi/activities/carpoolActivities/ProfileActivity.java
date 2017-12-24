package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.controllers.SPController;
import com.techcamp.aauj.rawabi.fragments.SettingsFragment;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {
    private TextView tvName,tvEmail;
    private ImageView imageView;
    private Button btnMyRides,btnMyJourneys,btnEditProfile,btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        imageView = findViewById(R.id.imageView);

        btnMyRides = findViewById(R.id.btnMyRides);
        btnMyJourneys = findViewById(R.id.btnMyJourneys);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);

        btnMyRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this,MyRiderActivity.class);
                startActivity(i);
            }
        });
        btnMyJourneys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this,MyJourneysActivity.class);
                startActivity(i);
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this,EditProfileActivity.class);
                startActivity(i);
            }
        });
        refreshDate();

    }

    private void refreshDate() {
        User user = SPController.getLocalUser(this);

        tvName.setText(user.getFullname());
        tvEmail.setText(user.getUsername());

        if(user.getImageurl() != null){
            Glide.with(this).load(user.getImageurl()).apply(RequestOptions.circleCropTransform()).into(imageView);
        }
    }
}

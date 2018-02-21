package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Intent;
import android.support.v4.app.Fragment;
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
import com.techcamp.aauj.rawabi.activities.abstractActivities.EmptyActivity;
import com.techcamp.aauj.rawabi.activities.abstractActivities.EmptyNoBarActivity;
import com.techcamp.aauj.rawabi.controllers.SPController;
import com.techcamp.aauj.rawabi.fragments.SettingsFragment;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.ProfileFragment;

import org.w3c.dom.Text;

public class ProfileActivity extends EmptyNoBarActivity {
    @Override
    public Fragment getFragment() {
        return new ProfileFragment();
    }
}

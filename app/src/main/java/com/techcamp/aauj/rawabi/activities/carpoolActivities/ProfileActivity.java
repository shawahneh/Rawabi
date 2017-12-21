package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.fragments.SettingsFragment;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}

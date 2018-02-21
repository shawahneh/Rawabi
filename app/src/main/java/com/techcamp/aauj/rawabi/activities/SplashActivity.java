package com.techcamp.aauj.rawabi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.techcamp.aauj.rawabi.activities.DashbordActivity;

/**
 * Created by alaam on 2/21/2018.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this, DashbordActivity.class);
        startActivity(i);
        finish();
    }
}

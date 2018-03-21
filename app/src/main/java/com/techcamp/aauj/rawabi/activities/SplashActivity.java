package com.techcamp.aauj.rawabi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by ALa on 2/21/2018.
 */

/**
 * Splash Screen
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(this, DashboardActivity.class);
        startActivity(i);
        finish();
    }
}


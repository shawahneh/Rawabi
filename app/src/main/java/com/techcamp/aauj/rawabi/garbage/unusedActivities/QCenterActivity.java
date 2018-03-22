package com.techcamp.aauj.rawabi.garbage.unusedActivities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.fragments.listFragments.AnnouncementsListFragment;

public class QCenterActivity extends AppCompatActivity {

    protected Fragment mFragment;
    protected TextView tvTitle;
    protected ImageView imgTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qcenter);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        mFragment =  fm.findFragmentByTag(getFragment().getTag());
        if (mFragment == null){
            mFragment = getFragment();
            fm.beginTransaction().replace(R.id.content,mFragment).commit();
        }
//        tvTitle = findViewById(R.id.tvTitle);
//        imgTitle = findViewById(R.id.imgTitle);
//        tvTitle.setText(getBarTitle());
//        imgTitle.setImageResource(getImage());

    }

    public Fragment getFragment(){
        return AnnouncementsListFragment.newInstance();
    }
}
package com.techcamp.aauj.rawabi.activities.abstractActivities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.techcamp.aauj.rawabi.R;

public abstract class BasicActivity<T> extends AppCompatActivity {
    protected T mBean;
    public static final String ARG_NUMBER_OF_COLS = "numberOfCols";
    public static final String ARG_BEAN = "bean";
    protected Fragment mFragment;
    protected TextView tvTitle;
    protected ImageView imgTitle,imgBackground,imgTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mBean = getIntent().getParcelableExtra(ARG_BEAN);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        mFragment =  fm.findFragmentByTag(getFragment().getTag());
        if (mFragment == null){
            mFragment = getFragment();
            fm.beginTransaction().replace(R.id.content,mFragment).commit();
        }
        tvTitle = findViewById(R.id.tvTitle);
        imgTitle = findViewById(R.id.imgTitle);
        imgBackground = findViewById(R.id.imgBackground);
        imgTop = findViewById(R.id.imgTop);



        tvTitle.setText(getBarTitle());
        imgTitle.setImageResource(getImage());
        imgBackground.setImageResource(getImageBackground());
        imgTop.setImageResource(getImageTop());

    }

    protected int getImageTop() {
        return R.drawable.logo;
    }

    protected int getImageBackground() {
        return R.drawable.city;
    }

    protected abstract int getImage();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    protected abstract String getBarTitle();
    public abstract Fragment getFragment();
}
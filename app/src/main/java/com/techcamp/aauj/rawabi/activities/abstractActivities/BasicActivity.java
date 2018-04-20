package com.techcamp.aauj.rawabi.activities.abstractActivities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.R;

/**
 * template activity
 * @param <T> : optional parcelable model
 */
public abstract class BasicActivity<T> extends AppCompatActivity {
    protected T mBean;
    public static final String ARG_BEAN = "bean";
    protected Fragment mFragment;
    protected TextView tvTitle,tvDescription;
    protected ImageView imgTitle,imgBackground,imgTop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        mBean = getIntent().getParcelableExtra(ARG_BEAN);

        FragmentManager fm = getSupportFragmentManager();
        mFragment =  fm.findFragmentByTag(getFragment().getTag());
        if (mFragment == null){
            mFragment = getFragment();
            fm.beginTransaction().setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
                    .replace(R.id.content,mFragment).commit();
        }
        tvTitle = findViewById(R.id.tvTitle);
        imgTitle = findViewById(R.id.imgTitle);
        imgBackground = findViewById(R.id.imgBackground);
        imgTop = findViewById(R.id.imgTop);
        tvDescription = findViewById(R.id.tvDescription);



        tvTitle.setText(getBarTitle());


        if(getImageBackground() != 0)
            Glide.with(this).load(getImageBackground()).into(imgBackground);

        if(getImage() == 0)
            imgTitle.setVisibility(View.GONE);
        else{
            imgTitle.setVisibility(View.VISIBLE);
            Glide.with(this).load(getImage()).into(imgTitle);
        }
        if(getImageTop() != 0)
            Glide.with(this).load(getImageTop()).into(imgTop);

        if(getDescription() == null){
            tvDescription.setVisibility(View.GONE);
        }else{
            tvDescription.setText(getDescription());
            tvDescription.setVisibility(View.VISIBLE);
        }


    }

    protected String getDescription() {
        return null;
    }

    protected void setDescription(String text){
        if(text == null){
            tvDescription.setVisibility(View.GONE);
        }
        else {
            tvDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(text);
        }
    }

    protected void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fmTransaction = fm.beginTransaction();
        fmTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        fmTransaction.replace(R.id.content,fragment).addToBackStack(null).commit();
    }


    protected int getImageTop() {
        return R.drawable.ic_rawabi_black_50dp;
    }

    protected int getImageBackground() {
        return R.drawable.bg_city;
    }

    protected abstract int getImage();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
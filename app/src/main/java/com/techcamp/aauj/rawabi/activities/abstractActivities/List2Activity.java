package com.techcamp.aauj.rawabi.activities.abstractActivities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.fragments.listFragments.ListFragment;

public abstract class List2Activity<T> extends AppCompatActivity {
    protected T mBean;
    public static final String ARG_NUMBER_OF_COLS = "numberOfCols";
    public static final String ARG_BEAN = "bean";

    Fragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list2);

        mBean = getIntent().getParcelableExtra(ARG_BEAN);


        FragmentManager fm = getSupportFragmentManager();
        mFragment =  fm.findFragmentByTag(getFragment().getTag());
        if (mFragment == null){
            mFragment = getFragment();
            fm.beginTransaction().replace(R.id.content,mFragment).commit();
        }
    }
    protected int getNumberOfCols(){
        int numberOfCols = getIntent().getIntExtra(ARG_NUMBER_OF_COLS,1);
        if (numberOfCols > 0){
            return numberOfCols;
        }else {
            return 1;
        }
    }
    public abstract Fragment getFragment();
}

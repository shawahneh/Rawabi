package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.MyJourneysListFragment;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.MyRidesListFragment;

public class MyRidesActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener
{
    private ViewPager vp_pages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rides);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vp_pages= (ViewPager) findViewById(R.id.vp_pages);
        PagerAdapter pagerAdapter =new FragmentAdapter(getSupportFragmentManager());
        vp_pages.setAdapter(pagerAdapter);

        TabLayout tbl_pages= (TabLayout) findViewById(R.id.tbl_pages);
        tbl_pages.setupWithViewPager(vp_pages);

        tbl_pages.addOnTabSelectedListener(this);
        setTitle("My Rides");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new MyRidesListFragment();
                case 1:
                    return new MyJourneysListFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                //
                //Your tab titles
                //
                case 0:return "My Rides";
                case 1:return "My Journeys";
                default:return null;
            }
        }
    }

}

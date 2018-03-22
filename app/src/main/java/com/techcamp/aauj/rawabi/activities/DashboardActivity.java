package com.techcamp.aauj.rawabi.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.basicActivities.QCenterListActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.CarpoolActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.CarpoolMainActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.LoginRegisterActivity;
import com.techcamp.aauj.rawabi.activities.basicActivities.EventsListActivity;
import com.techcamp.aauj.rawabi.activities.basicActivities.JobsListActivity;
import com.techcamp.aauj.rawabi.activities.basicActivities.MediaListActivity;
import com.techcamp.aauj.rawabi.activities.basicActivities.TransportationActivity;
import com.techcamp.aauj.rawabi.fragments.HomeFragment;
import com.techcamp.aauj.rawabi.fragments.NewsFragment;

/**
 * Main activity for user
 */
public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TabLayout.OnTabSelectedListener,
        HomeFragment.IListener {

    private ViewPager vp_pages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        vp_pages= (ViewPager) findViewById(R.id.vp_pages);
//        PagerAdapter pagerAdapter=new FragmentAdapter(getSupportFragmentManager());
//        vp_pages.setAdapter(pagerAdapter);
//
//        TabLayout tbl_pages= (TabLayout) findViewById(R.id.tbl_pages);
//        tbl_pages.setupWithViewPager(vp_pages);
//        tbl_pages.getTabAt(0).setIcon(R.drawable.ic_home_white_24dp);
////        tbl_pages.getTabAt(1).setIcon(R.drawable.news_outline2);
//        tbl_pages.getTabAt(0).setTag("home");
////        tbl_pages.getTabAt(1).setTag("news");
//
//        tbl_pages.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN);
////        tbl_pages.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
//        tbl_pages.addOnTabSelectedListener(this);
        replaceFragment(new HomeFragment());

    }
    protected void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fmTransaction = fm.beginTransaction();
//        fmTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        fmTransaction.replace(R.id.content,fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
//        } else if(vp_pages.getCurrentItem() != 0) {
//            vp_pages.setCurrentItem(0);
        }else{
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_carpool:
                onCardClick(HomeFragment.TAG_CARPOOL);
                break;
            case R.id.nav_media:
                onCardClick(HomeFragment.TAG_MEDIA);
                break;
            case R.id.nav_events:
                onCardClick(HomeFragment.TAG_EVENTS);
                break;
            case R.id.nav_transportation:
                onCardClick(HomeFragment.TAG_TRANSPORTATION);
                break;
            case R.id.nav_qcenter:
                onCardClick(HomeFragment.TAG_QCENTER);
                break;
        }


        // Close drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tab.getTag().equals("news")){
            tab.setIcon(getResources().getDrawable(R.drawable.news));
        }else
        tab.getIcon().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

        if(tab.getTag().equals("news")){
            tab.setIcon(getResources().getDrawable(R.drawable.news_outline2));
        }else
            tab.getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    // Handle HomeFragment Cards Clicks
    @Override
    public void onCardClick(String tag) {
        switch (tag){
            case HomeFragment.TAG_NEWS:
                vp_pages.setCurrentItem(1);
                break;
            case HomeFragment.TAG_CARPOOL:
                if(WebApi.getInstance().isLogin()){
                    Intent i = new Intent(this, CarpoolActivity.class);
                    startActivity(i);
                }else{

                    Intent i = new Intent(this, LoginRegisterActivity.class);
                    startActivity(i);
                }
                break;
            case HomeFragment.TAG_EVENTS:
                Intent i = new Intent(this, EventsListActivity.class);
                startActivity(i);
                break;
            case HomeFragment.TAG_QCENTER:
                startActivity(new Intent(this,QCenterListActivity.class));
                break;
            case HomeFragment.TAG_JOBS:
                startActivity(new Intent(this,JobsListActivity.class));
                break;
            case HomeFragment.TAG_TRANSPORTATION:
                startActivity(new Intent(this,TransportationActivity.class));
                break;
            case HomeFragment.TAG_MEDIA:
                startActivity(new Intent(this,MediaListActivity.class));
                break;

        }
    }

    // For TABS
    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new HomeFragment();
                case 1:
                    return new NewsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                //
                //Your tab titles
                //
                case 0:return "";
                case 1:return "";
                default:return null;
            }
        }
    }

}

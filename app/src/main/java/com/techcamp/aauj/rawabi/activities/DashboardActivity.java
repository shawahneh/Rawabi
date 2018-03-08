package com.techcamp.aauj.rawabi.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.CarpoolMainActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.LoginRegisterActivity;
import com.techcamp.aauj.rawabi.activities.listActivities.JobsListActivity;
import com.techcamp.aauj.rawabi.database.UsersDB;
import com.techcamp.aauj.rawabi.fragments.AnnouncmentFragment;
import com.techcamp.aauj.rawabi.fragments.CalendarPageFragment;
import com.techcamp.aauj.rawabi.fragments.HomeFragment;
import com.techcamp.aauj.rawabi.fragments.NewsFragment;
import com.techcamp.aauj.rawabi.fragments.TransportationPageFragment;
import com.techcamp.aauj.rawabi.fragments.listFragments.AnnouncementsListFragment;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,TabLayout.OnTabSelectedListener,
        HomeFragment.IListener
        {
private ViewPager vp_pages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setElevation(0);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//        View view = navigationView.getHeaderView(0);
//        ImageView imageView = view.findViewById(R.id.imageView);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ActivityOptionsCompat options = ActivityOptionsCompat.
//                        makeSceneTransitionAnimation(DashboardActivity.this, view, "trImage");
//                Intent i = new Intent(DashboardActivity.this,JobsListActivity.class);
//                startActivity(i,options.toBundle());
//            }
//        });

        vp_pages= (ViewPager) findViewById(R.id.vp_pages);
        PagerAdapter pagerAdapter=new FragmentAdapter(getSupportFragmentManager());
        vp_pages.setAdapter(pagerAdapter);

        TabLayout tbl_pages= (TabLayout) findViewById(R.id.tbl_pages);
        tbl_pages.setupWithViewPager(vp_pages);
        tbl_pages.getTabAt(0).setIcon(R.drawable.ic_home_white_24dp);
        tbl_pages.getTabAt(1).setIcon(R.drawable.news_outline2);
        tbl_pages.getTabAt(0).setTag("home");
        tbl_pages.getTabAt(1).setTag("news");

        tbl_pages.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN);
//        tbl_pages.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        tbl_pages.addOnTabSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

            @Override
            protected void onStop() {
                super.onStop();
                WebService.clear();
            }

            @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashbord, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home:
                setFragment(new HomeFragment(),"home");
                break;
            case R.id.nav_carpool:
                if(WebApi.getInstance(this).isLogin()){
                    Intent i = new Intent(this, CarpoolMainActivity.class);
                    startActivity(i);
                }else{

                    Intent i = new Intent(this, LoginRegisterActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.nav_media:

                break;
            case R.id.nav_calendar:
                setFragment(CalendarPageFragment.newInstance(),"tag");
                break;
            case R.id.nav_transportation:
                setFragment(new TransportationPageFragment(),"tag");
                break;
            case R.id.nav_qcenter:
                setFragment(new AnnouncmentFragment(),"tag");
                break;
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void setFragment(android.support.v4.app.Fragment fragment, String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content,fragment,tag)
                .addToBackStack(tag)
                .commit();
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

    @Override
    public void onCardClick(String tag) {
        switch (tag){
            case HomeFragment.TAG_NEWS:
                vp_pages.setCurrentItem(1);
                break;
        }
    }

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
            return 2;
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
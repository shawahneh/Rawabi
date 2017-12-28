package com.techcamp.aauj.rawabi.activities;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.ITriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.MapActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.MapRiderActivity;
import com.techcamp.aauj.rawabi.database.UsersDB;
import com.techcamp.aauj.rawabi.fragments.AnnouncmentFragment;
import com.techcamp.aauj.rawabi.fragments.CalendarPageFragment;
import com.techcamp.aauj.rawabi.fragments.HomeFragment;
import com.techcamp.aauj.rawabi.fragments.LoginFragment;
import com.techcamp.aauj.rawabi.fragments.TransportationPageFragment;
import com.techcamp.aauj.rawabi.utils.MapUtil;

public class DashbordActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CalendarPageFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content,new HomeFragment(),"tag")
                .commit();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
            new UsersDB(this);
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
                StartCarPoolActivity();
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


    private void StartCarPoolActivity() {

        boolean login = WebApi.getInstance(this).isLogin();
        if(login) {
            Intent i = new Intent(this, CarpoolActivity.class);
            startActivity(i);
        }
        else {
            Intent i = new Intent(this, LoginRegisterActivity.class);
            startActivity(i);
        }
    }
    private void setFragment(android.support.v4.app.Fragment fragment, String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content,fragment,tag)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

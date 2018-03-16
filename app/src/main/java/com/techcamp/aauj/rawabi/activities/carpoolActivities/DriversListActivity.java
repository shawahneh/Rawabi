package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.DriversListFragment;

import java.util.ArrayList;

/**
 * Created by ALa on 2/19/2018.
 */

public class DriversListActivity extends AppCompatActivity implements DriversListFragment.IFragmentListener<Journey> {
    private ArrayList<Journey> mJourneys;
    private Fragment mFragment;
    public static Intent getIntent(Context ctx, ArrayList<Journey> journeys){
        Intent intent = new Intent(ctx,DriversListActivity.class);
        intent.putParcelableArrayListExtra(DriversListFragment.ARG_LIST,journeys);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_list);

        mJourneys = getIntent().getParcelableArrayListExtra(DriversListFragment.ARG_LIST);


        FragmentManager fm = getSupportFragmentManager();
        mFragment =  fm.findFragmentByTag(getFragment().getTag());
        if (mFragment == null){
            mFragment = getFragment();
            fm.beginTransaction().replace(R.id.content,mFragment).commit();
        }

        setTitle(getBarTitle());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
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
    protected String getBarTitle() {
        return "All Drivers";
    }

    public Fragment getFragment() {
        return DriversListFragment.newInstance(mJourneys);
    }

    // on viewButton in DriversListFragment clicked
    @Override
    public void onItemClicked(Journey item) {
        Intent i = new Intent();
        i.putExtra("data",item);
        setResult(Activity.RESULT_OK,i);
        finish();
    }
}

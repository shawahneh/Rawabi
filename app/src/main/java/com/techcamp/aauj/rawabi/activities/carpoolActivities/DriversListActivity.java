package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.techcamp.aauj.rawabi.model.Journey;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.DriversListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALa on 2/19/2018.
 */

/**
 * contains a list of available drivers
 * this activity shown at rider's map activity  when the user clicked to see all available driver
 */
public class DriversListActivity extends AppCompatActivity implements DriversListFragment.IFragmentListener<Journey> {
    private List<Journey> mJourneys;
    private Fragment mFragment;
    public static Intent getIntent(Context ctx, List<Journey> journeys){
        Intent intent = new Intent(ctx,DriversListActivity.class);
        intent.putParcelableArrayListExtra(DriversListFragment.ARG_LIST, (ArrayList<? extends Parcelable>) journeys);
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

        // show back arrow ( up button)
        if(getSupportActionBar() != null)
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

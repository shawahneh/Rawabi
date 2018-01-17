package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.MapDriverActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.MapRiderActivity;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.ProfileActivity;
import com.techcamp.aauj.rawabi.fragments.UserTypeFragment;

public class UserTypeActivity extends AppCompatActivity implements UserTypeFragment.IUserTypeFragmenetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState == null)
            setFragment(new UserTypeFragment(),"tag");
    }
    private void setFragment(android.support.v4.app.Fragment fragment, String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content,fragment,tag)
                .commit();
    }

    @Override
    public void onTypeClick(int type) {
        if(type == 0){

            Intent i = new Intent(this,MapRiderActivity.class);
            i.putExtra("mode",type);
            startActivity(i);
        }else{
            Intent i = new Intent(this,MapDriverActivity.class);
            i.putExtra("mode",type);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.carpool_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_profile) {
            startProfileActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    protected void startProfileActivity() {
        Intent i = new Intent(this,ProfileActivity.class);
        startActivity(i);

    }
}

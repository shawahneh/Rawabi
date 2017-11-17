package com.techcamp.aauj.rawabi.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.fragments.UserTypeFragment;

public class UserTypeActivity extends AppCompatActivity implements UserTypeFragment.IUserTypeFragmenetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
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
            Intent i = new Intent(this,CarpoolActivity.class);
            i.putExtra("mode",type);
            startActivity(i);
    }
}

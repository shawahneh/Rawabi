package com.techcamp.aauj.rawabi.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.fragments.PickerFragment;

public class PickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        if(savedInstanceState == null){
            setFragment(PickerFragment.getInstance(),"picker");
        }

    }

    private void setFragment(Fragment fragment, String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content,fragment,tag)
                .commit();
    }
}

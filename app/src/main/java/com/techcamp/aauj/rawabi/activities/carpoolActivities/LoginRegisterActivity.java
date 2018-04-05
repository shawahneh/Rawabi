package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.controllers.SPController;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.ImageProfileFragment;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.LoginFragment;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.RegisterFragment;
import com.techcamp.aauj.rawabi.model.User;
import com.techcamp.aauj.rawabi.utils.MapUtil;

public class LoginRegisterActivity extends AppCompatActivity implements RegisterFragment.IListener {
    private Button mButtonLogin,mButtonRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        MapUtil.getCurrentLoc(this,null);
        if(savedInstanceState == null){
            setFragment(new LoginFragment());
        }

        mButtonLogin = findViewById(R.id.btnLogin);
        mButtonRegister= findViewById(R.id.btnRegister);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new LoginFragment());
            }
        });
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new RegisterFragment());
            }
        });
    }


    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content,fragment)
                .commit();
    }



    @Override
    public void onFragmentRegisterSuccess(User user) {
        SPController.saveLocalUser(this,user);

        Intent i = new Intent(this, AddImageActivity.class);
        startActivity(i);
        finish();
    }


}

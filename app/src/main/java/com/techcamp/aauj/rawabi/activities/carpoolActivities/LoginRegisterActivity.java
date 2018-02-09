package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.controllers.SPController;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.LoginFragment;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.RegisterFragment;
import com.techcamp.aauj.rawabi.utils.MapUtil;

public class LoginRegisterActivity extends AppCompatActivity {
    private Button mButtonLogin,mButtonRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        MapUtil.getCurrentLoc(this,null);
        if(savedInstanceState == null){
            setFragment(new LoginFragment(),"tag");
        }

        mButtonLogin = findViewById(R.id.btnLogin);
        mButtonRegister= findViewById(R.id.btnRegister);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new LoginFragment(),"tag");
            }
        });
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new RegisterFragment(),"tag");
            }
        });
    }


    private void setFragment(Fragment fragment, String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content,fragment,tag)
                .commit();
    }

}

package com.techcamp.aauj.rawabi.fragments.carpoolFragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.techcamp.aauj.rawabi.API.AuthWebApi;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.UserTypeActivity;
import com.techcamp.aauj.rawabi.controllers.SPController;


public class LoginFragment extends Fragment {

    private EditText mEditTextEmail,mEditTextPassword;
    private AuthWebApi mAuthWebApi = WebService.getInstance(getContext());
    SweetAlertDialog pDialog;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        View btn = view.findViewById(R.id.btnLogin);
        mEditTextEmail = view.findViewById(R.id.txtEmail);
        mEditTextPassword = view.findViewById(R.id.txtPassword);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getContext(), UserTypeActivity.class);
//                getActivity().finish();
//                startActivity(i);
                showProgress();
                mAuthWebApi.login(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString(),
                        new IResponeTriger<User>() {
                            @Override
                            public void onResponse(User item) {
                                pDialog.dismissWithAnimation();
                                SPController.saveLocalUser(getContext(),item);
                                    Intent i = new Intent(getContext(), UserTypeActivity.class);
                                    getActivity().finish();
                                    startActivity(i);
                            }

                            @Override
                            public void onError(String err) {
                                pDialog.dismissWithAnimation();
                                showError();
                            }
                        });

            }
        });
        return view;
    }
    private void showProgress(){
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Checking user info");
        pDialog.setCancelable(false);
        pDialog.show();
    }
    private void showError(){
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("can't validate user info")
                .show();
    }
}

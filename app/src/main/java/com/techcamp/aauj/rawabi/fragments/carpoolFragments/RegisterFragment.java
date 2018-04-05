package com.techcamp.aauj.rawabi.fragments.carpoolFragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.techcamp.aauj.rawabi.API.AuthWebApi;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.activities.carpoolActivities.CarpoolMainActivity;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;
import com.techcamp.aauj.rawabi.model.User;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.controllers.SPController;

import java.util.Date;


public class RegisterFragment extends Fragment {

    private SweetAlertDialog pDialog;
    private EditText txtName,txtPassword,txtPasswordConfirm,txtPhone,txtEmail;
    private RadioButton rbnMale,rbnFemale;
    private Button btnRegister;
    private IListener mListener;

    public RegisterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        txtName = view.findViewById(R.id.txtName);
        txtPassword = view.findViewById(R.id.txtPassword);
        txtPasswordConfirm = view.findViewById(R.id.txtPasswordConfirm);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtEmail = view.findViewById(R.id.txtEmail);
        rbnMale = view.findViewById(R.id.rbnMale);
        rbnFemale = view.findViewById(R.id.rbnFemale);
        btnRegister = view.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerPressed();
            }
        });


    }
    private void showProgress(){
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Register..");
        pDialog.setCancelable(false);
        pDialog.show();
    }
    private void showError(String err){
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(err)
                .show();
    }

    private void registerPressed() {
        if(validate()){
            AuthWebApi api = WebFactory.getAuthService();

            final User user = new User();
            user.setFullname(txtName.getText().toString().trim());
            user.setUsername(txtEmail.getText().toString().trim());
            user.setPhone(txtPhone.getText().toString().trim());
            user.setGender(rbnMale.isChecked()?User.MALE:User.FEMALE);
            user.setPassword(txtPassword.getText().toString());
            user.setBirthdate(new Date());
            user.setAddress("");
            user.setImageurl("");
            showProgress();
            api.userRegister(user, new ICallBack<Boolean>() {
                @Override
                public void onResponse(Boolean success) {
                    pDialog.dismissWithAnimation();
                    if(success){
                        if(mListener != null)
                            mListener.onFragmentRegisterSuccess(user);
                    }else
                        onError("Registration failed, try another username");

                }

                @Override
                public void onError(String err) {
                    pDialog.dismissWithAnimation();
                    showError(err);
                }
            });
        }
    }
    private boolean validate(){
        if(TextUtils.isEmpty(txtName.getText())){
            txtName.setError("Enter your name");
            txtName.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(txtEmail.getText())){
            txtEmail.setError("Enter your email");
            txtEmail.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(txtPassword.getText())){
            txtPassword.setError("Enter the password");
            txtPassword.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(txtPasswordConfirm.getText())){
            txtPasswordConfirm.setError("Enter the password");
            txtPasswordConfirm.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(txtPhone.getText())){
            txtPhone.setError("Enter your phone");
            txtPhone.requestFocus();
            return false;
        }
        if(!txtPassword.getText().toString().equals(txtPasswordConfirm.getText().toString())){
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof RegisterFragment.IListener){
            mListener = (RegisterFragment.IListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement IFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface IListener{
        void onFragmentRegisterSuccess(User user);
    }

}

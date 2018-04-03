package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.techcamp.aauj.rawabi.API.AuthWebApi;
import com.techcamp.aauj.rawabi.API.WebDummy;
import com.techcamp.aauj.rawabi.model.User;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.controllers.SPController;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private EditText txtName,txtOldPassword,txtNewPassword,txtConfirmNewPassword;
    private ImageView imageView;
    private Uri mUriImage;
    private AuthWebApi authWebApi = WebDummy.getInstance();
    private View layoutPassword,btnChangePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        txtName = findViewById(R.id.txtName);
        txtOldPassword = findViewById(R.id.txtOldPassword);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtConfirmNewPassword = findViewById(R.id.txtConfirmNewPassword);
        imageView = findViewById(R.id.imageView);
        layoutPassword = findViewById(R.id.layoutPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutPassword.setVisibility(layoutPassword.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
            }
        });

        User user = SPController.getLocalUser(this);
        if(user != null && user.getImageurl() != null)

        Glide.with(this).load(user.getImageurl()).apply(RequestOptions.placeholderOf(R.drawable.ic_person_black_24dp))
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        setTitle("Edit Profile");
    }

    private void openGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        startActivityForResult(getIntent, PICK_IMAGE);
    }

    public void onClickSave(final View view) {
        if(!validate())
            return;
        User user = SPController.getLocalUser(this);
        if(user == null)
            return;

        if(layoutPassword.getVisibility()==View.VISIBLE)
            user.setPassword(txtNewPassword.getText().toString());
        user.setFullname(txtName.getText().toString());

        ((Button)view).setEnabled(false);
        String oldPassword = layoutPassword.getVisibility()==View.VISIBLE?txtOldPassword.getText().toString():user.getPassword();
        authWebApi.setUserDetails(user, oldPassword , new ICallBack<Boolean>() {
            @Override
            public void onResponse(Boolean saved) {
                if(saved){
                    showError("Changed successfully");
                    finish();
                }else{
                    showError("Error");
                    view.setEnabled(true);
                }
            }

            @Override
            public void onError(String err) {
                showError(err);
                view.setEnabled(true);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            mUriImage = data.getData();
            Glide.with(this).load(mUriImage).apply(RequestOptions.circleCropTransform()).into(imageView);

        }
    }
    private boolean validate() {
        if(TextUtils.isEmpty(txtName.getText())){
            txtName.setError("Field required");
            txtName.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(txtOldPassword.getText())){
            txtOldPassword.setError("Field required");
            txtOldPassword.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(txtNewPassword.getText())){
            txtNewPassword.setError("Field required");
            txtNewPassword.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(txtConfirmNewPassword.getText())){
            txtConfirmNewPassword.setError("Field required");
            txtConfirmNewPassword.requestFocus();
            return false;
        }
        if(!txtNewPassword.getText().toString().equals(txtConfirmNewPassword.getText().toString())){
            showError("Passwords do not match");
            txtNewPassword.requestFocus();
            return false;
        }
        return true;
    }
    private void showError(String error){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}

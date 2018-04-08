package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.controllers.SPController;
import com.techcamp.aauj.rawabi.model.User;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText txtOldPassword;
    private EditText txtNewPassword;
    private EditText txtNewPassword2;

    private SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //binding
        txtOldPassword = findViewById(R.id.txtOldPassword);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtNewPassword2 = findViewById(R.id.txtNewPassword2);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveCommand();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveCommand() {
        if(validateInputs()){
            final User user = SPController.getLocalUser(this);
            user.setPassword(txtNewPassword.getText().toString().trim());

            showProgress("updating password");
            WebFactory.getAuthService().setUserDetails(user, txtOldPassword.getText().toString().trim(), new ICallBack<Boolean>() {
                @Override
                public void onResponse(Boolean success) {
                    pDialog.dismissWithAnimation();
                    if(success){
                        Toast.makeText(ChangePasswordActivity.this, "password changed successfully", Toast.LENGTH_SHORT).show();
                        SPController.saveLocalUser(ChangePasswordActivity.this,user);
                        Intent intent = new Intent();
                        intent.putExtra("data",txtNewPassword.getText().toString());
                        setResult(RESULT_OK,intent);
                        finish();
                    }else
                        onError("onResponse, error changing password");
                }

                @Override
                public void onError(String err) {
                    pDialog.dismissWithAnimation();
                    Log.e("tag",err);
                    Toast.makeText(ChangePasswordActivity.this, "error changing password", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateInputs() {
        EditText txt;

        txt = txtOldPassword;
        if(TextUtils.isEmpty(txt.getText())){
            txt.setError("Please enter old password");
            txt.requestFocus();
            return false;
        }
        txt = txtNewPassword;
        if(TextUtils.isEmpty(txt.getText())){
            txt.setError("Please enter new password");
            txt.requestFocus();
            return false;
        }
        txt = txtNewPassword2;
        if(TextUtils.isEmpty(txt.getText())){
            txt.setError("Please enter new password");
            txt.requestFocus();
            return false;
        }

        return true;
    }

    private void showProgress(String txt){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(txt);
        pDialog.setCancelable(false);
        pDialog.show();
    }

}

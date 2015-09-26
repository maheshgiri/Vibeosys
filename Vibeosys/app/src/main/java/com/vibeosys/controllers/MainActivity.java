package com.vibeosys.controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vibeosys.R;
import com.vibeosys.data.Constants;
import com.vibeosys.framework.AppPreferences;
import com.vibeosys.framework.BaseActivity;
import com.vibeosys.framework.ui.DialogRenderer;

/*
* This is launcher activity it handle first registration flow
* */

public class MainActivity extends BaseActivity {

    private EditText mCustomerID;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkIsRegistered();

        setContentView(R.layout.activity_main);
        View theLoginBtn = findViewById(R.id.loginBtn);
        mCustomerID = (EditText)findViewById(R.id.customer_id);
        mPassword = (EditText)findViewById(R.id.password);

        theLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String theCustId = mCustomerID.getText().toString();
                String thePwd = mPassword.getText().toString();
                // TODO: start service call to validate credentials
                if(theCustId.equals("1234") && thePwd.equals("1234")) {

                    AppPreferences theAppSettings = new AppPreferences(MainActivity.this);
                    theAppSettings.putSetting(Constants.SETTING_IS_REGISTERED, true);

                    DialogInterface.OnDismissListener theListener = new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            Intent theIntent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(theIntent);
                        }
                    };

                    DialogRenderer.render(MainActivity.this, getString(R.string.reg_success), theListener);

                }
                else {
                    mCustomerID.setText("");
                    mPassword.setText("");
                    Toast.makeText(MainActivity.this, "Invalid credentials! Please Try Again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /*
    * Check if user is registered or not
    * */
    private void checkIsRegistered(){
        AppPreferences theAppSettings = new AppPreferences(this);
        if(theAppSettings.getSetting(Constants.SETTING_IS_REGISTERED)) {
            finish();
            Intent theIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(theIntent);
        }
    }
}

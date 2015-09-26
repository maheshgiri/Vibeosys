package com.vibeosys.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.vibeosys.R;
import com.vibeosys.framework.BaseActivity;

/* Login Activity for end user
* */

public class LoginActivity extends BaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View theLoginBtn = findViewById(R.id.loginBtn);
        theLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(theIntent);
            }
        });
    }
}

package com.vibeosys.controllers;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.vibeosys.R;
import com.vibeosys.framework.BaseActivity;

/**
 * Payment activity for selected order
 */
public class PaymentActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setTitle("Payment");
        String total = getIntent().getStringExtra("total_price");
        TextView theAmount = (TextView)findViewById(R.id.amount);
        TextView theTotal = (TextView)findViewById(R.id.total);
        theAmount.setText(total);
        theTotal.setText(total);
    }
}

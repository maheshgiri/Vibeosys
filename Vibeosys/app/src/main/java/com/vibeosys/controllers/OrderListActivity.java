package com.vibeosys.controllers;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.vibeosys.App;
import com.vibeosys.R;
import com.vibeosys.controllers.adapters.OrderListAdapter;
import com.vibeosys.data.Constants;
import com.vibeosys.framework.AppDB;
import com.vibeosys.framework.BaseActivity;

import java.util.HashMap;

/*
* Show Orders list for selected table
* */
public class OrderListActivity extends BaseActivity{

    private long mTableID;
    private ExpandableListView mOrderList;
    private OrderListAdapter mOrderListAdapter;
    private long mCurrentOrderID = -1;
    private View mKOTSendPrint;
    private View mPayBtn;
    HashMap<String, Object> mOrderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        mTableID = getIntent().getIntExtra(Constants.TABLE_ID, -1);
        setTitle(getString(R.string.order_for) + mTableID);
        mOrderList = (ExpandableListView)findViewById(R.id.order_list);
        mOrderListAdapter = new OrderListAdapter(this, mTableID);
        mOrderList.setAdapter(mOrderListAdapter);
        mKOTSendPrint = findViewById(R.id.kot_send_print);
        mKOTSendPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendKOTAndPrint();
            }
        });

        mPayBtn = findViewById(R.id.pay);
        mPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });
    }

    private void pay(){
        if(mOrderData != null) {
            Intent thePayIntent = new Intent(this, PaymentActivity.class);
            thePayIntent.putExtra("total_price", mOrderData.get("total_price").toString());
            startActivity(thePayIntent);
        }
    }

    private void sendKOTAndPrint(){
        AppDB theDb = new AppDB(this);
        theDb.closeOrder(mCurrentOrderID);
    }

    /*
    * Returns current order id
    * */
    private long getCurrentOrderID(){
        AppDB theDb = new AppDB(this);
        mCurrentOrderID = theDb.getServingOrderID(mTableID);
        return mCurrentOrderID;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        // if there is order is process show create order button in action bar
        if(getCurrentOrderID() == -1) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_order, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.add_order){
            // start new order activity
            Intent theIntent = new Intent(this, OrderActivity.class);
            AppDB theDB = new AppDB(this);
            theIntent.putExtra(Constants.ORDER_ID, theDB.createNewOrderId(mTableID));
            startActivity(theIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mOrderListAdapter.notifyDataSetChanged();
        View footer = findViewById(R.id.footer);
        if(getCurrentOrderID() != -1){
            footer.setVisibility(View.VISIBLE);
            TextView theTotalView = (TextView)findViewById(R.id.cur_order_price);
            TextView theReviewView = (TextView)findViewById(R.id.cur_order_itm_cnt);
            AppDB theDb = new AppDB(this);
            mOrderData = theDb.getCurrentOrderInfo(getCurrentOrderID());

            theTotalView.setText(getString(R.string.current_order) + "\n" + mOrderData.get("total_price"));
            theReviewView.setText(mOrderData.get("total_items")+" "+getString(R.string.items_selected_review));
        }
        else{
            footer.setVisibility(View.GONE);
        }

    }
}

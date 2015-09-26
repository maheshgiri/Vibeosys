package com.vibeosys.controllers;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.vibeosys.R;
import com.vibeosys.data.Constants;
import com.vibeosys.data.MenuInfo;
import com.vibeosys.data.OrderInfo;
import com.vibeosys.framework.AppDB;
import com.vibeosys.framework.BaseActivity;
import com.vibeosys.framework.Utility;

import java.util.List;

/*
* Order Preparation screen
* */
public class OrderActivity extends BaseActivity {

    private ListView mMenuList;
    private long mOrderId;
    private OrderInfo mOrderInfo;
    private AppDB mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        mDB = new AppDB(this);
        mOrderId = getIntent().getLongExtra(Constants.ORDER_ID, -1);
        mOrderInfo = mDB.getOrder(mOrderId);
        mMenuList = (ListView)findViewById(R.id.menuList);
        mMenuList.setAdapter(new MenuAdapter());
    }

    class MenuAdapter extends BaseAdapter {

        private List<MenuInfo> mMenus;

        public MenuAdapter(){
            mMenus = mDB.getMenuList();
        }

        @Override
        public int getCount() {
            return mMenus.size();
        }

        @Override
        public MenuInfo getItem(int position) {
            return mMenus.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return Utility.getOrderItem(convertView, getItem(position), OrderActivity.this, mOrderInfo.isOpen(), mOrderId);
        }
    }
}

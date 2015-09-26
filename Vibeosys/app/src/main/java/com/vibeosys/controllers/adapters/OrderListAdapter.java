package com.vibeosys.controllers.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.vibeosys.App;
import com.vibeosys.R;
import com.vibeosys.controllers.OrderActivity;
import com.vibeosys.controllers.OrderExpandCallback;
import com.vibeosys.data.Constants;
import com.vibeosys.data.MenuInfo;
import com.vibeosys.data.OrderInfo;
import com.vibeosys.framework.AppDB;
import com.vibeosys.framework.Utility;
import com.vibeosys.framework.ui.riv.view.RemoteImageView;

import java.util.ArrayList;
import java.util.List;

/**
* This will show main page for table orders
*/
public class OrderListAdapter extends BaseExpandableListAdapter {

    private final AppDB mAppDB;
    private Context mCtx;
    private List<OrderInfo> mOrderList;
    private long mCurrentOrderID = -1;
    private long mTableId;
    private OrderExpandCallback mCallback;


    public OrderListAdapter(Context aCtx, long aTableId) {
        mCtx = aCtx;
        mTableId = aTableId;
        mAppDB = new AppDB(mCtx);
        mOrderList = mAppDB.getOrderList(aTableId);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {

        if(mOrderList != null){
            return mOrderList.size();
        }

        return 0;
    }

    @Override
    public void notifyDataSetChanged() {
        mOrderList = mAppDB.getOrderList(mTableId);
        super.notifyDataSetChanged();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (new AppDB(App.getInstance())).getOrder(getGroup(groupPosition).getOrderId()).getMenuCount();
        //return getGroup(groupPosition).getMenuCount();
    }

    @Override
    public OrderInfo getGroup(int groupPosition) {
        return mOrderList.get(groupPosition);
    }

    @Override
    public MenuInfo getChild(int groupPosition, int childPosition) {

        return (new AppDB(App.getInstance())).getOrder(getGroup(groupPosition).getOrderId()).getMenu(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {

        // If convert view is null create new view
        if(null == convertView ) {
            LayoutInflater theLayoutInflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View theView = theLayoutInflater.inflate(R.layout.item_order, null);
            convertView = theView;
        }

        final OrderInfo theOrder = getGroup(groupPosition);
        TextView theOrderName = (TextView)convertView.findViewById(R.id.order_id);
        AppDB theDb = new AppDB(App.getInstance());
        mCurrentOrderID = theDb.getServingOrderID(mTableId);

        if(mCurrentOrderID == theOrder.getOrderId()) {
            theOrderName.setText("Current");
        }
        else
            theOrderName.setText(mCtx.getString(R.string.title_activity_order)+" : #"+theOrder.getOrderId());
        TextView theItmCnt = (TextView)convertView.findViewById(R.id.item_count);
        theItmCnt.setText((new AppDB(App.getInstance())).getOrder(theOrder.getOrderId()).getMenus().size()+" Items");
        View viewBtn = convertView.findViewById(R.id.viewBtn);
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theorderIntent = new Intent(parent.getContext(), OrderActivity.class);
                theorderIntent.putExtra(Constants.ORDER_ID, theOrder.getOrderId());
                parent.getContext().startActivity(theorderIntent);
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        long theOrderId = getGroup(groupPosition).getOrderId();
        return Utility.getOrderItem(convertView, getChild(groupPosition, childPosition), mCtx, (theOrderId == mCurrentOrderID), theOrderId);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}

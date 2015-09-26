package com.vibeosys.controllers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vibeosys.App;
import com.vibeosys.R;
import com.vibeosys.data.TableInfo;
import com.vibeosys.framework.AppDB;

import java.util.List;

/**
 * This Class helps in binding data with Grid or Lists
 */
public class TablesAdapter extends BaseAdapter {

    private int mType;
    private List<TableInfo> mList;

    public TablesAdapter(int aType){
        mType = aType;
        AppDB theDB = new AppDB(App.getInstance());
        if(mType == 0) {
            mList = theDB.getAllTableList();
        }
        else {
            mList = theDB.getMyTableList();
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TableInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context theContext = parent.getContext();
        LayoutInflater theLayoutInflater = (LayoutInflater)theContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View theView = theLayoutInflater.inflate(R.layout.item_table, null);
        TextView theInfoText = (TextView)theView.findViewById(R.id.info);
        TableInfo theTableInfo = getItem(position);
        theInfoText.setText("Table No. "+theTableInfo.getId()+" | "+theTableInfo.getCapacity());
        if(theTableInfo.isReserved()){
            theView.setBackgroundResource(R.drawable.reserved_tablebg);
        }
        theView.setTag(theTableInfo.getId());

        return theView;
    }
}

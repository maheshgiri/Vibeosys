package com.vibeosys.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.vibeosys.R;
import com.vibeosys.controllers.adapters.TablesAdapter;
import com.vibeosys.data.Constants;

/**
 * This shows single page of application
 */
public class HomePageFragment extends Fragment implements AdapterView.OnItemClickListener {

    private GridView mAllTableGrid;
    private DrawerLayout mDrawerLayout;
    private ListView mFilterList;
    private int mType;

    public void setType(int type){
        mType = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.home_page, container, false);

        mAllTableGrid = (GridView)rootView.findViewById(R.id.tableList);
        mAllTableGrid.setAdapter(new TablesAdapter(mType));
        mAllTableGrid.setOnItemClickListener(this);

        mDrawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        mFilterList = (ListView) rootView.findViewById(R.id.filter_list);

        String[] mFilterTitles = new String[] { "All", "Garden View", "Pool Side View", "Other"};
        // Set the adapter for the list view
        mFilterList.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.filter_list_item, mFilterTitles));
        // Set the list's click listener
        //mFilterList.setOnItemClickListener(new DrawerItemClickListener());

        Button theFiltersBtn = (Button) rootView.findViewById(R.id.filters_btn);
        theFiltersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                } else {
                    mDrawerLayout.closeDrawers();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent theOrderIntent = new Intent(getActivity(), OrderListActivity.class);
        theOrderIntent.putExtra(Constants.TABLE_ID, Integer.valueOf(view.getTag().toString()));
        startActivity(theOrderIntent);
    }

}

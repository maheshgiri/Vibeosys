package com.vibeosys.controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.vibeosys.R;
import com.vibeosys.framework.BaseActivity;

/*
* Home Screen with table list will shown through this activity
* */
public class HomeActivity extends BaseActivity {

    private ViewPager mPager;
    private TablePagerAdapter mPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_content);
        setTitle("Marriott");
        init();
       // fetchData("http://www.w3schools.com/html/", true);
    }

    /*
    * Initialise the UI for Home screen
    * */
    public void init(){

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new TablePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    /**
     * Pager adapter that represents 2 Tabs in Home Screen
     */
    private class TablePagerAdapter extends FragmentStatePagerAdapter {

        public TablePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            String[] tabs = getResources().getStringArray(R.array.home_tabs);
            return tabs[position];

        }

        @Override
        public Fragment getItem(int position) {
            return new HomePageFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if(searchView == null){
            Toast.makeText(this, "Null", Toast.LENGTH_LONG).show();
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_filter){
            if(!mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
            else {
                mDrawerLayout.closeDrawers();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    */

    @Override
    public void onSuccess(String aData) {
        super.onSuccess(aData);

      //  WebView tv = (WebView)findViewById(R.id.data);
        //tv.loadData(aData, "text/html", "utf-8");
    }
}

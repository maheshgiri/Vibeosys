package com.vibeosys.data;

import java.util.ArrayList;

/**
 * Maintains all info related to order
 */
public class OrderInfo {

    private long mOrderId;
    private ArrayList<MenuInfo> mMenus = new ArrayList<MenuInfo>();

    public boolean isOpen() {
        return mIsOpen;
    }

    public void setIsOpen(boolean mIsOpen) {
        this.mIsOpen = mIsOpen;
    }

    private boolean mIsOpen = false;

    public long getOrderId() {
        return mOrderId;
    }

    public void setOrderId(long aOrderId) {
        this.mOrderId = aOrderId;
    }

    public ArrayList<MenuInfo> getMenus() {
        return mMenus;
    }

    /*
    * Set current menus added in order
    * */
    public void setMenus(ArrayList<MenuInfo> aMenus) {
        if(aMenus != null) {
            this.mMenus = aMenus;
        }
    }

    /*
    * Add new menu in order
    * */
    public void addMenu(MenuInfo aMenuInfo){
        mMenus.add(aMenuInfo);
    }

    /*
    * Gives number of menus in this order
    * */
    public int getMenuCount() {
        return mMenus.size();
    }

    /*
    * Get menu at selected position
    * */
    public MenuInfo getMenu(int position) {
        return mMenus.get(position);
    }
}

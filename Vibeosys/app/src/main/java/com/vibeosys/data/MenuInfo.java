package com.vibeosys.data;

import android.view.Menu;

import java.util.ArrayList;

/**
 * Holds all menu related data
 */
public class MenuInfo {

    private long mID;
    private String mName;
    private float mPrice;
    private String mTags;
    private int mQty;

    public MenuInfo(){

    }

    public MenuInfo(long mID, String mName, float mPrice, String mTags, String mImgUrl) {
        this.mID = mID;
        this.mName = mName;
        this.mPrice = mPrice;
        this.mTags = mTags;
        this.mImgUrl = mImgUrl;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String aImgUrl) {
        this.mImgUrl = aImgUrl;
    }

    private String mImgUrl;

    public long getID() {
        return mID;
    }

    public void setID(long aID) {
        this.mID = aID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String aName) {
        this.mName = aName;
    }

    public float getPrice() {
        return mPrice;
    }

    public void setPrice(float aPrice) {
        this.mPrice = aPrice;
    }

    public String getTags() {
        return mTags;
    }

    public void setTags(String aTags) {
        this.mTags = aTags;
    }

    public int getQty() {
        return mQty;
    }

    public void setQty(int aQty) {
        this.mQty = aQty;
    }

}

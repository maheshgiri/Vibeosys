package com.vibeosys.data;

import java.util.ArrayList;
import java.util.List;

/**
 * This holds info related to single table
 */
public class TableInfo {

    private int mID;
    private int mCapacity;
    private List<Integer> mCategories;
    private boolean mReserved;

    public TableInfo(int aID, int aCapacity, List<Integer> aCategories, boolean aReserved){

        mID = aID;
        mCapacity = aCapacity;
        mCategories = aCategories;
        mReserved = aReserved;

    }

    /*
    * return table id
    * */
    public int getId(){
        return mID;
    }

    /*
    * return table status
    * */
    public boolean isReserved(){
        return mReserved;
    }

    /*
    * return table capacity
    * */
    public int getCapacity(){
        return mCapacity;
    }

    /*
    * return table category
    * */
    public List<Integer> getCategories(){
        return mCategories;
    }
}

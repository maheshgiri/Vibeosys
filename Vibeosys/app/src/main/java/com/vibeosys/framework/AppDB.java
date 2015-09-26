package com.vibeosys.framework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vibeosys.data.MenuInfo;
import com.vibeosys.data.OrderInfo;
import com.vibeosys.data.TableInfo;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;

/**
 * Handle sqlite database of application
 */
public class AppDB {

    private static final String DB_NAME = "vibeosys";
    private static final int DB_VERSION = 1;
    private static final String ORDER_TABLE = "order_info";
    private static final String MENU_TABLE = "menu_info";
    private static final String TABLES_TABLE = "table_info";
    //private static final String USER_TABLE = "user";
    private static final String ORDER_MENU_TABLE = "order_menu";

    private static final String KEY_ID = "id";

    // Fileds related to Table
    private static final String KEY_TABLE_ID = "table_id";
    private static final String KEY_TABLE_CAPACITY = "capacity";
    private static final String KEY_TABLE_CATEGORIES = "categories";

    private static final String KEY_ORDER_ID = "order_id";
    private static final String KEY_ORDER_ISOPEN = "is_open";

    // Fields related to menu
    private static final String KEY_MENU_ID = "menu_id";
    private static final String KEY_MENU_QTY = "qty";
    private static final String KEY_MENU_NAME = "name";
    private static final String KEY_MENU_TAGS = "tags";
    private static final String KEY_MENU_ICON = "icon";
    private static final String KEY_MENU_PRICE = "price";

    private DBHelper mDBHelper;

    public AppDB(Context aContext){
        /*File theFile = aContext.getDatabasePath("vibeosys");
        Log.d("TAG", "" + theFile.list());*/
        mDBHelper = new DBHelper(aContext, DB_NAME, null, DB_VERSION);
    }

    /*
    * Get list of all tables available in restaurant
    * */
    public List<TableInfo> getAllTableList(){

        ArrayList<TableInfo> theTableList = new ArrayList<>();
        Cursor theCursor = mDBHelper.getReadableDatabase().rawQuery("select * from "+TABLES_TABLE, null);

        if(theCursor != null && theCursor.getCount()>0){

            theCursor.moveToFirst();

            do {

                List<Integer> cats = getListFromJSON(theCursor.getString(theCursor.getColumnIndex(KEY_TABLE_CATEGORIES)));
                theTableList.add(new TableInfo(theCursor.getInt(theCursor.getColumnIndex(KEY_ID)), theCursor.getInt(theCursor.getColumnIndex(KEY_TABLE_CAPACITY)), cats, false ));

            }while (theCursor.moveToNext());

            theCursor.close();
        }
        return theTableList;
    }

    /*
    * Convert JSON Array into array list
    * */
    public List<Integer> getListFromJSON(String aJSONData){

        JSONArray theJSONArray;
        ArrayList<Integer> list = new ArrayList<>();

        try {
            theJSONArray = new JSONArray(aJSONData);

            for (int i=0; i<theJSONArray.length(); i++){
                list.add(theJSONArray.optInt(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  list;
    }


    /*
    * Get All tables that seleted user is serving
    * */
    public List<TableInfo> getMyTableList(){

        ArrayList<TableInfo> theTableList = new ArrayList<>();
        Cursor theCursor = mDBHelper.getReadableDatabase().rawQuery("select * from "+TABLES_TABLE, null);
        if(theCursor != null && theCursor.getCount()>0){

            theCursor.moveToFirst();
            do {

                List<Integer> cats = getListFromJSON(theCursor.getString(theCursor.getColumnIndex(KEY_TABLE_CATEGORIES)));
                theTableList.add(new TableInfo(theCursor.getInt(theCursor.getColumnIndex(KEY_ID)), theCursor.getInt(theCursor.getColumnIndex(KEY_TABLE_CAPACITY)), cats, false ));

            }while (theCursor.moveToNext());

            theCursor.close();
        }
        return theTableList;

    }

    /*
    * Get order lists with selected table
    * */
    public List<OrderInfo> getOrderList(long aTableId){

        ArrayList<OrderInfo> theOrderList = new ArrayList<>();
        //ArrayList<MenuInfo> mMenus = new ArrayList<MenuInfo>();
        Cursor theCursor = mDBHelper.getReadableDatabase().rawQuery("select * from "+ORDER_TABLE+" where "+KEY_TABLE_ID+" = ? ", new String[]{String.valueOf(aTableId)});

        if(theCursor != null && theCursor.getCount()>0){
            theCursor.moveToFirst();

            do {

                OrderInfo theOrder = new OrderInfo();
                theOrder.setOrderId(theCursor.getLong(theCursor.getColumnIndex(KEY_ID)));
                theOrder.setIsOpen(theCursor.getInt(theCursor.getColumnIndex(KEY_ORDER_ISOPEN))==1);
                //theOrder.setMenus(mMenus);
                theOrderList.add(theOrder);

            }while (theCursor.moveToNext());

            theCursor.close();
        }

        return theOrderList;
    }

    /*
    * Gives list of menus stored in database
    * */
    public List<MenuInfo> getMenuList(){

        ArrayList<MenuInfo> theMenus = new ArrayList<>();
        Cursor theCursor = mDBHelper.getReadableDatabase().rawQuery("select * from "+MENU_TABLE, null);

        if(theCursor != null ) {

            if (theCursor.getCount() > 0) {

                theCursor.moveToFirst();

                do {

                    int id = theCursor.getInt(theCursor.getColumnIndex("id"));
                    String name = theCursor.getString(theCursor.getColumnIndex(KEY_MENU_NAME));
                    float price = theCursor.getFloat(theCursor.getColumnIndex(KEY_MENU_PRICE));
                    String icon = theCursor.getString(theCursor.getColumnIndex(KEY_MENU_ICON));
                    String tags = theCursor.getString(theCursor.getColumnIndex(KEY_MENU_TAGS));
                    theMenus.add(new MenuInfo(id, name, price, tags, icon));

                } while (theCursor.moveToNext());

            }

            theCursor.close();
        }
        return theMenus;

    }

    /*
    * Create new order in database for table
    * */
    public long createNewOrderId(long aTableId) {

        ContentValues theCVs = new ContentValues();
        theCVs.put(KEY_TABLE_ID, aTableId);
        theCVs.put(KEY_ORDER_ISOPEN, 1);
        return mDBHelper.getWritableDatabase().insert(ORDER_TABLE, null, theCVs);

    }

    /*
    * Get details of selected order
    * */
    public OrderInfo getOrder(long aOrderId) {

        Cursor theOrderCursor = mDBHelper.getReadableDatabase().rawQuery("select * from "+ORDER_TABLE+ " where "+KEY_ID+" = "+aOrderId, null);
        OrderInfo theOrder = new OrderInfo();
        theOrder.setOrderId(aOrderId);

        if(theOrderCursor != null) {

            if (theOrderCursor.getCount() > 0) {

                theOrderCursor.moveToFirst();
                theOrder.setIsOpen(theOrderCursor.getInt(theOrderCursor.getColumnIndex(KEY_ORDER_ISOPEN)) == 1);
                Cursor theOMCursor = mDBHelper.getReadableDatabase().rawQuery("select * from " + ORDER_MENU_TABLE + " where " + KEY_ORDER_ID + " = ?", new String[]{String.valueOf(aOrderId)});

                if (theOMCursor != null) {

                    if (theOMCursor.getCount() > 0) {
                        theOMCursor.moveToFirst();

                        do {

                            long menuId = theOMCursor.getLong(theOMCursor.getColumnIndex(KEY_MENU_ID));
                            float price = theOMCursor.getFloat(theOMCursor.getColumnIndex(KEY_MENU_PRICE));
                            MenuInfo theOriginalMenuinfo = getMenuInfo(menuId);
                            MenuInfo theMenuInfo = new MenuInfo(menuId, theOriginalMenuinfo.getName(), price, theOriginalMenuinfo.getTags(), theOriginalMenuinfo.getImgUrl());
                            theOrder.addMenu(theMenuInfo);

                        } while (theOMCursor.moveToNext());

                    }

                    theOMCursor.close();

                }

                theOrderCursor.close();

            }
        }
        return theOrder;
    }

    /*
    * Retrieve menu info from main menu table
    * */
    public MenuInfo getMenuInfo(long aMenuId){

        MenuInfo theMenuInfo = new MenuInfo();
        Cursor theCursor = mDBHelper.getReadableDatabase().rawQuery("select * from "+MENU_TABLE+ " where "+KEY_ID+" = ? ", new String[]{String.valueOf(aMenuId)});
        theMenuInfo.setID(aMenuId);

        if(theCursor != null ) {

            if (theCursor.getCount() > 0) {

                theCursor.moveToFirst();
                theMenuInfo.setName(theCursor.getString(theCursor.getColumnIndex(KEY_MENU_NAME)));
                theMenuInfo.setTags(theCursor.getString(theCursor.getColumnIndex(KEY_MENU_TAGS)));
                theMenuInfo.setImgUrl(theCursor.getString(theCursor.getColumnIndex(KEY_MENU_ICON)));

            }

            theCursor.close();
        }

        return theMenuInfo;

    }

    /*
    * Increase qty
    * */
    public void increaseQty(long menuId, float price, long orderId) {

        String WHERE_CLAUSE = KEY_ORDER_ID+" = ? and "+KEY_MENU_ID+" = ?";
        String[] whr_args = new String[] { String.valueOf(orderId), String.valueOf(menuId) };
        Cursor theOMCursor = mDBHelper.getReadableDatabase().rawQuery("select * from "+ORDER_MENU_TABLE+" where " + WHERE_CLAUSE, whr_args);
        ContentValues theCVs = new ContentValues();

        if(theOMCursor != null && theOMCursor.getCount() > 0){
            theOMCursor.moveToFirst();
            theCVs.put(KEY_MENU_QTY, theOMCursor.getInt(theOMCursor.getColumnIndex(KEY_MENU_QTY))+1);
            mDBHelper.getWritableDatabase().update(ORDER_MENU_TABLE, theCVs, WHERE_CLAUSE, whr_args);
        }
        else {
            theCVs.put(KEY_MENU_QTY, 1);
            theCVs.put(KEY_MENU_PRICE, price);
            theCVs.put(KEY_ORDER_ID , orderId);
            theCVs.put(KEY_MENU_ID, menuId);
            mDBHelper.getWritableDatabase().insert(ORDER_MENU_TABLE, null, theCVs);
        }

        if(theOMCursor != null){
            theOMCursor.close();
        }
    }

    /*
    * Decrease qty
    * */
    public void decreaseQty(long menuId, float price, long orderId) {

        String WHERE_CLAUSE = KEY_ORDER_ID+" = ? and "+KEY_MENU_ID+" = ?";
        String[] whr_args = new String[] { String.valueOf(orderId), String.valueOf(menuId) };
        Cursor theOMCursor = mDBHelper.getReadableDatabase().rawQuery("select * from "+ORDER_MENU_TABLE+" where " + WHERE_CLAUSE, whr_args);
        ContentValues theCVs = new ContentValues();

        if(theOMCursor != null && theOMCursor.getCount() > 0){
            theOMCursor.moveToFirst();
            int theFinalQty = theOMCursor.getInt(theOMCursor.getColumnIndex(KEY_MENU_QTY))-1;
            if(theFinalQty == 0){
                mDBHelper.getReadableDatabase().delete(ORDER_MENU_TABLE, WHERE_CLAUSE, whr_args);
            }
            else {
                theCVs.put(KEY_MENU_QTY, theFinalQty);
                mDBHelper.getWritableDatabase().update(ORDER_MENU_TABLE, theCVs, WHERE_CLAUSE, whr_args);
            }

        }
        else {
            theCVs.put(KEY_MENU_QTY, 1);
            theCVs.put(KEY_MENU_PRICE, price);
            theCVs.put(KEY_ORDER_ID , orderId);
            theCVs.put(KEY_MENU_ID, menuId);
            mDBHelper.getWritableDatabase().insert(ORDER_MENU_TABLE, null, theCVs);
        }

        if(theOMCursor != null){
            theOMCursor.close();
        }
    }

    /*
     * Get selected menu qty
     */
    public int getMenuQty(long orderId, long menuId){

        int theQty = 0;
        String WHERE_CLAUSE = KEY_ORDER_ID+" = ? and "+KEY_MENU_ID+" = ?";
        String[] whr_args = new String[] { String.valueOf(orderId), String.valueOf(menuId) };
        Cursor theCursor = mDBHelper.getReadableDatabase().rawQuery("select * from "+ORDER_MENU_TABLE+" where " + WHERE_CLAUSE, whr_args);

        if(theCursor != null) {

            if (theCursor.getCount() > 0) {
                theCursor.moveToFirst();
                theQty = theCursor.getInt(theCursor.getColumnIndex(KEY_MENU_QTY));
            }

            theCursor.close();
        }

        return theQty;
    }

    /*
    * Get current open order for selected table
    * */
    public long getServingOrderID(long aTableId) {

        long theOrderID = -1;
        Cursor theCursor = mDBHelper.getReadableDatabase().rawQuery("select "+KEY_ID+" from "+ORDER_TABLE+" where "+KEY_TABLE_ID+" = ? and "+KEY_ORDER_ISOPEN+" = ?", new String[]{String.valueOf(aTableId), "1"});
        if(theCursor != null) {
            if (theCursor.getCount() > 0) {
                theCursor.moveToFirst();
                theOrderID = theCursor.getLong(0);
            }
            theCursor.close();
        }
        return theOrderID;
    }

    /*
    * Get total price and total selected items
    * */
    public HashMap<String, Object> getCurrentOrderInfo(long currentOrderID) {

        HashMap<String, Object> result = new HashMap<>();
        Cursor theCursor = mDBHelper.getReadableDatabase().rawQuery("select * from "+ORDER_MENU_TABLE+" where " + KEY_ORDER_ID+" = ? ", new String[] { String.valueOf(currentOrderID) });

        if(theCursor != null) {

            if (theCursor.getCount() > 0) {
                theCursor.moveToFirst();
                result.put("total_items", theCursor.getCount());
                float totalPrice = 0;
                do{
                    totalPrice+= theCursor.getInt(theCursor.getColumnIndex(KEY_MENU_QTY))*theCursor.getFloat(theCursor.getColumnIndex(KEY_MENU_PRICE));

                }while (theCursor.moveToNext());

                result.put("total_price", totalPrice);
            }

            theCursor.close();
        }

        return result;
    }

    /*
    * Finalise and complete order
    * */
    public void closeOrder(long currentOrderID) {

        ContentValues theCVs = new ContentValues();
        theCVs.put(KEY_ORDER_ISOPEN, 0);
        mDBHelper.getWritableDatabase().update(ORDER_TABLE, theCVs, KEY_ID+" = "+currentOrderID, null);

    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
package com.vibeosys.framework;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Handles application preferences in SharedPreferences
 */
public class AppPreferences {

    private static final String NAME = "app_settings";
    private SharedPreferences mPrefs;

    public AppPreferences(Context aContext){
        mPrefs = aContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    /*
    * Save boolean setting
    * */
    public void putSetting(String aName, boolean aValue){
        SharedPreferences.Editor theEditor = mPrefs.edit();
        theEditor.putBoolean(aName, aValue);
        theEditor.commit();
    }

    /*
    * Get setting
    * */
    public boolean getSetting(String aName){
        return mPrefs.getBoolean(aName, false);
    }
}

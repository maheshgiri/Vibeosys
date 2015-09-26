package com.vibeosys.framework.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
*  Utility class for network related operations
* */
public class NetworkUtils {

    /* Method to check network availability
    * */
    public boolean isActiveNetworkAvailable(Context aContext){

        boolean theStatus = false;
        ConnectivityManager theConManager = (ConnectivityManager)aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo theNetInfo = theConManager.getActiveNetworkInfo();
        if(theNetInfo != null) {
            theStatus = theNetInfo.isConnected();
        }
        return theStatus;

    }

}

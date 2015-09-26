package com.vibeosys.framework.ui;

import android.content.Context;
import android.content.DialogInterface;

import com.vibeosys.R;

/**
 * Class to show all type of dialogs
 */
public class DialogRenderer {

    public static void render(final Context aContext, String aMsg, DialogInterface.OnDismissListener aListener) {

        FWDialog dlg = new FWDialog(aContext);
        dlg.setTitle(R.string.app_name);
        dlg.setMessage(aMsg);
        dlg.setOnDismissListener(aListener);
        dlg.show();
    }

}

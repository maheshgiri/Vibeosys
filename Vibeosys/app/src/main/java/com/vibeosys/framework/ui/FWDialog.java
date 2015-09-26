package com.vibeosys.framework.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.vibeosys.R;

/**
 * Basic dialog which will be customised as per need
 */
public class FWDialog extends Dialog {

    private String mMsg;

    public FWDialog(Context context) {
        super(context);
    }

    public FWDialog(Context context, int theme) {
        super(context, theme);
    }

    protected FWDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /*
    * Set message for dialog
    * */
    public void setMessage(String aMsg){
        mMsg = aMsg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View theView  = getLayoutInflater().inflate(R.layout.fw_dialog, null);
        TextView theMsgText = (TextView)theView.findViewById(R.id.msg);
        theMsgText.setText(mMsg);
        View theOkBtn = theView.findViewById(R.id.okBtn);
        theOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(theView);
    }
}

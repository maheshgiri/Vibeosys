package com.vibeosys.framework;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vibeosys.App;
import com.vibeosys.R;
import com.vibeosys.data.MenuInfo;
import com.vibeosys.data.OrderInfo;
import com.vibeosys.framework.ui.riv.view.RemoteImageView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Utility class for common functions
 */
public class Utility {

    /*
    * Load order item ui
    * */
    public static View getOrderItem(View convertView, final MenuInfo aMenuInfo, Context aCtx, boolean isFromCurrentOrder, final long orderId){

        if(null == convertView) {
            LayoutInflater theLayoutInflater = (LayoutInflater) aCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View theView = theLayoutInflater.inflate(R.layout.item_menu, null);
            convertView = theView;
        }

        TextView thePrice = (TextView)convertView.findViewById(R.id.price);
        thePrice.setText("₹ "+aMenuInfo.getPrice());

        TextView theName = (TextView)convertView.findViewById(R.id.menu_name);
        theName.setText(aMenuInfo.getName());

        TextView theTags = (TextView)convertView.findViewById(R.id.tags);
        try {
            JSONArray theJsonTags = new JSONArray(aMenuInfo.getTags());
            String theTagsList = "";
            for(int i=0; i<theJsonTags.length(); i++){
                if(i!= 0) {
                    theTagsList+=" ";
                }
                theTagsList += theJsonTags.optString(i);
            }
            theTags.setText(theTagsList);
        }
        catch (JSONException e){

        }

        final TextView theQtyText = (TextView)convertView.findViewById(R.id.qty);
        AppDB theDB = new AppDB(App.getInstance());
        theQtyText.setText(theDB.getMenuQty(orderId, aMenuInfo.getID())+"");

        RemoteImageView riv = (RemoteImageView)convertView.findViewById(R.id.menu_icon);
        riv.setImageURI(Uri.parse(aMenuInfo.getImgUrl()));

        View rightPan = convertView.findViewById(R.id.rightPan);
        View increaseBtn = convertView.findViewById(R.id.increase);
        View decreaseBtn = convertView.findViewById(R.id.decrease);

        View.OnClickListener theListener = null;
        if (isFromCurrentOrder) {

            theListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppDB theDB = new AppDB(App.getInstance());
                    if (v.getId() == R.id.increase) {
                        theDB.increaseQty(aMenuInfo.getID(), aMenuInfo.getPrice(), orderId);
                        theQtyText.setText(String.valueOf(Integer.valueOf(theQtyText.getText().toString()) + 1));
                    } else if (v.getId() == R.id.decrease) {
                        int theVal = Integer.valueOf(theQtyText.getText().toString());
                        if (theVal > 0) {
                            theDB.decreaseQty(aMenuInfo.getID(), aMenuInfo.getPrice(), orderId);
                            theQtyText.setText(String.valueOf(theVal - 1));
                        }
                    }

                }
            };

            increaseBtn.setVisibility(View.VISIBLE);
            decreaseBtn.setVisibility(View.VISIBLE);
        }
        else {
            increaseBtn.setVisibility(View.GONE);
            decreaseBtn.setVisibility(View.GONE);
        }

        increaseBtn.setOnClickListener(theListener);
        decreaseBtn.setOnClickListener(theListener);
        return convertView;
    }
}

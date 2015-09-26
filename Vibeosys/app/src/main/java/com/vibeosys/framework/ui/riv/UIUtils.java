package com.vibeosys.framework.ui.riv;

import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vibeosys.R;
import com.vibeosys.framework.Logger;

/**
 * Define UI utils here. Some dialog, popup, progress dialog etc...
 * 
 * @author chris
 * 
 */
public class UIUtils
{
    /**
     * Width of UI Element
     */
    private static float      sResWidth            = 0;
    /**
     * Height of UI Element
     */
    private static float      sResHeight           = 0;

    /**
     * For showing images in textview
     */
    public static ImageGetter sImageGetter_150x100 = new Html.ImageGetter()
                                                   {
                                                       @Override
                                                       public Drawable getDrawable( String _source )
                                                       {
                                                           Drawable drawable = null;
                                                           URL url = null;

                                                           try
                                                           {
                                                               url = new URL( _source );
                                                               drawable = Drawable.createFromStream( url.openStream(),
                                                                       "" );
                                                           }
                                                           catch( Exception _e )
                                                           {
                                                               Logger.e(_e.toString());
                                                           }

                                                           // Important
                                                           drawable.setBounds( 0, 0, 150, 100 );

                                                           return drawable;
                                                       }
                                                   };
    /**
     * For showing images in textview
     */
    public static ImageGetter sImageGetter_90x80   = new Html.ImageGetter()
                                                   {
                                                       @Override
                                                       public Drawable getDrawable( String _source )
                                                       {
                                                           Drawable drawable = null;
                                                           URL url = null;

                                                           try
                                                           {
                                                               url = new URL( _source );
                                                               drawable = Drawable.createFromStream( url.openStream(),
                                                                       "" );
                                                           }
                                                           catch( Exception _e )
                                                           {
                                                               Logger.e( _e.toString() );
                                                           }

                                                           // Important
                                                           drawable.setBounds( 0, 0, 90, 80 );

                                                           return drawable;
                                                       }
                                                   };

    /**
     * Show progress dialog with stringId
     * 
     * @param _activity
     * @param _messageId
     * @param _cancelable
     * @return ProgressDialog
     */
    public static ProgressDialog showProgressDialog( Context _activity, int _messageId, boolean _cancelable )
    {

        return showProgressDialog( _activity, _activity.getString( _messageId ), _cancelable );
    }

    /**
     * Show progress dialog with string
     * 
     * @param _activity
     * @param _message
     * @param _cancelable
     * @return ProgressDialog
     */
    public static ProgressDialog showProgressDialog( Context _activity, String _message, boolean _cancelable )
    {
        ProgressDialog dialog = new ProgressDialog( _activity );
        dialog.setMessage( _message );
        dialog.setIndeterminate( false );
        dialog.setCancelable( _cancelable );
        dialog.setOnCancelListener( null );
        dialog.show();

        return dialog;
    }

    /**
     * Show an alert popup
     * 
     * @param _activity
     * @param _messageId
     * @param _titleId
     * @param _buttonTextId
     * @param _onOkClickedListener
     */
    public static void showAlert( Activity _activity, int _messageId, int _titleId, int _buttonTextId,
            DialogInterface.OnClickListener _onOkClickedListener )
    {
        new AlertDialog.Builder( _activity ).setMessage( _messageId )
                .setTitle( _titleId )
                .setCancelable( false )
                .setIcon( R.drawable.ic_launcher )
                .setNeutralButton( _buttonTextId, _onOkClickedListener )
                .show();
    }

    /**
     * Show an alert with 2 buttons
     * 
     * @param _activity
     * @param _messageId
     * @param _titleId
     * @param _button1TextId
     * @param _button2TextID
     * @param _onButton1ClickedListener
     * @param _onButton2ClickedListener
     */
    public static void showAlert( Activity _activity,
            int _messageId,
            int _titleId,
            int _button1TextId,
            int _button2TextID,
            DialogInterface.OnClickListener _onButton1ClickedListener,
            DialogInterface.OnClickListener _onButton2ClickedListener )
    {
        new AlertDialog.Builder( _activity ).setMessage( _messageId )
                .setTitle( _titleId )
                .setCancelable( false )
                .setIcon( R.drawable.ic_launcher )
                .setNeutralButton( _button1TextId, _onButton1ClickedListener )
                .setNegativeButton( _button2TextID, _onButton2ClickedListener )
                .show();
    }

    /**
     * Show an alert with 3 buttons
     * 
     * @param _activity
     * @param _messageId
     * @param _titleId
     * @param _button1TextId
     * @param _button2TextID
     * @param _button3TextID
     * @param _onButton1ClickedListener
     * @param _onButton2ClickedListener
     */
    public static void showAlert( Activity _activity,
            int _messageId,
            int _titleId,
            int _button1TextId,
            int _button2TextID,
            int _button3TextID,
            DialogInterface.OnClickListener _onButton1ClickedListener,
            DialogInterface.OnClickListener _onButton2ClickedListener,
            DialogInterface.OnClickListener _onButton3ClickedListener )
    {
        new AlertDialog.Builder( _activity ).setMessage( _messageId )
                .setTitle( _titleId )
                .setCancelable( false )
                .setIcon( R.drawable.ic_launcher )
                .setNegativeButton( _button1TextId, _onButton1ClickedListener )
                .setNeutralButton( _button2TextID, _onButton2ClickedListener )
                .setPositiveButton( _button3TextID, _onButton3ClickedListener )
                .show();
    }

    /**
     * Show an alert with 2 buttons
     * 
     * @param _activity
     * @param _message
     * @param _titleId
     * @param _button1TextId
     * @param _button2TextID
     * @param _onButton1ClickedListener
     * @param _onButton2ClickedListener
     */
    public static void showAlert( Activity _activity,
            String _message,
            int _titleId,
            int _button1TextId,
            int _button2TextID, DialogInterface.OnClickListener _onButton1ClickedListener,
            DialogInterface.OnClickListener _onButton2ClickedListener )
    {
        new AlertDialog.Builder( _activity ).setMessage( _message )
                .setTitle( _titleId )
                .setCancelable( true )
                .setIcon( R.drawable.ic_launcher )
                .setNeutralButton( _button1TextId, _onButton1ClickedListener )
                .setNegativeButton( _button2TextID, _onButton2ClickedListener )
                .show();
    }

    /**
     * Show an alert popup
     * 
     * @param _activity
     * @param _messageId
     * @param _titleId
     * @param _buttonTextId
     */
    public static void showAlert( Activity _activity, int _messageId, int _titleId, int _buttonTextId )
    {
        new AlertDialog.Builder( _activity ).setMessage( _messageId )
                .setTitle( _titleId )
                .setCancelable( true )
                .setIcon( R.drawable.ic_launcher )
                .setNeutralButton( _buttonTextId, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface _dialog, int _which )
                    {

                    }
                } )
                .show();
    }

    /**
     * Show an alert popup
     * 
     * @param _activity
     * @param _message
     * @param _titleId
     * @param _buttonTextId
     */
    public static void showAlert( Activity _activity, String _message, int _titleId, int _buttonTextId )
    {
        new AlertDialog.Builder( _activity ).setMessage( _message )
                .setTitle( _titleId )
                .setCancelable( true )
                .setIcon( R.drawable.ic_launcher )
                .setNeutralButton( _buttonTextId, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick( DialogInterface _dialog, int _which )
                    {

                    }
                } )
                .show();
    }

    /**
     * Show a long time toast with a text id.
     * 
     * @param _context
     * @param _messageId
     */
    public static void showLongToast( Context _context, int _messageId )
    {
        Toast.makeText( _context, _context.getString( _messageId ), Toast.LENGTH_LONG ).show();
    }

    /**
     * Show a short time toast with a text id.
     * 
     * @param _context
     * @param _messageId
     */
    public static void showShortToast( Context _context, int _messageId )
    {
        Toast.makeText( _context, _context.getString( _messageId ), Toast.LENGTH_SHORT ).show();
    }

    /**
     * Show a long time toast.
     * 
     * @param _context
     * @param _message
     */
    public static void showLongToast( Context _context, String _message )
    {
        Toast.makeText( _context, _message, Toast.LENGTH_LONG ).show();
    }

    /**
     * Show a short time toast.
     * 
     * @param _context
     * @param _message
     */
    public static void showShortToast( Context _context, String _message )
    {
        Toast.makeText( _context, _message, Toast.LENGTH_SHORT ).show();
    }

    /**
     * Calc params for RelativeLayout
     * 
     * @param _context
     * @param _imageView
     * @param _w
     * @param _h
     */
    public static RelativeLayout.LayoutParams calcRelativeLayoutParamsForView( Context _context, ImageView _imageView,
            int _w, int _h,
            int _screenWidth, int _screenHeight )
    {

        RelativeLayout.LayoutParams ret = null;
        calcLayoutParamsForView( _context, _imageView, _w, _h, _screenWidth, _screenHeight );
        _imageView.setLayoutParams( ret = new RelativeLayout.LayoutParams( (int) sResWidth, (int) sResHeight ) );
        return ret;

    }

    /**
     * Calc params for LinearLayout
     * 
     * @param _context
     * @param _imageView
     * @param _w
     * @param _h
     */
    public static void calcLinearLayoutParamsForView( Context _context, ImageView _imageView, int _w, int _h,
            int _screenWidth, int _screenHeight )
    {

        calcLayoutParamsForWideView( _context, _imageView, _w, _h, _screenWidth, _screenHeight );
        _imageView.setLayoutParams( new LinearLayout.LayoutParams( (int) sResWidth, (int) sResHeight ) );

    }

    /**
     * Calc params for Layout
     * 
     * @param _context
     * @param _imageView
     * @param _w
     * @param _h
     */
    private static void calcLayoutParamsForView( Context _context, ImageView _imageView, int _w, int _h,
            int _screenWidth, int _screenHeight )
    {

        float ratio = 0.0f;

        int bmpWidth = _w;
        int bmpHeight = _h;

        if( _context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE )
        {
            ratio = _screenHeight * 1.0f / bmpHeight * 0.5f;
            sResWidth = (int) (ratio * bmpWidth);
            sResHeight = _screenHeight * 0.5f;

            if( (sResWidth > _screenWidth) || (sResHeight > _screenHeight) )
            {
                ratio = _screenWidth * 0.5f / bmpWidth * 0.5f;
                sResWidth = _screenWidth;
                sResHeight = (int) (ratio * bmpHeight);
            }
        }
        else if( _context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT )
        {
            ratio = (_screenWidth * 1.0f / bmpWidth * 0.5f);
            sResWidth = _screenWidth * 0.5f;
            sResHeight = (int) (ratio * bmpHeight);

            if( (sResWidth > _screenWidth) || (sResHeight > _screenHeight) )
            {
                ratio = (_screenHeight * 0.5f / bmpHeight * 0.5f);
                sResWidth = (int) (ratio * bmpWidth);
                sResHeight = _screenHeight;
            }
        }
    }

    /**
     * Calc params for Layout
     * 
     * @param _context
     * @param _imageView
     * @param _w
     * @param _h
     */
    private static void calcLayoutParamsForWideView( Context _context, ImageView _imageView, int _w, int _h,
            int _screenWidth, int _screenHeight )
    {

        float ratio = 0.0f;

        int bmpWidth = _w;
        int bmpHeight = _h;

        if( _context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE )
        {
            ratio = _screenHeight * 1.0f / bmpHeight * 1.0f;
            sResWidth = (int) (ratio * bmpWidth);
            sResHeight = _screenHeight;

            if( (sResWidth > _screenWidth) || (sResHeight > _screenHeight) )
            {
                ratio = _screenWidth * 1.0f / bmpWidth * 1.0f;
                sResWidth = _screenWidth;
                sResHeight = (int) (ratio * bmpHeight);
            }
        }
        else if( _context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT )
        {
            ratio = (_screenWidth * 1.0f / bmpWidth * 1.0f);
            sResWidth = _screenWidth;
            sResHeight = (int) (ratio * bmpHeight);

            if( (sResWidth > _screenWidth) || (sResHeight > _screenHeight) )
            {
                ratio = (_screenHeight * 1.0f / bmpHeight * 1.0f);
                sResWidth = (int) (ratio * bmpWidth);
                sResHeight = _screenHeight;
            }
        }
    }

    /**
     * Create a divider
     * 
     * @return
     */
    public static View createDivider( Context _context )
    {
        View v = new View( _context );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, 1 );
        v.setBackgroundColor( _context.getResources().getColor( android.R.color.darker_gray ) );
        v.setLayoutParams( params );
        return v;
    }

}

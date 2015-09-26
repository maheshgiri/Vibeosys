package com.vibeosys.framework;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Base Activity will give the basic implementation with async task support and other things
 */
public class BaseActivity extends AppCompatActivity implements BackgroundTaskCallback{

    protected void fetchData(final String aServiceUrl, final boolean aShowProgressDlg){
        new BackgroundTask(aShowProgressDlg).execute(aServiceUrl);
    }

    @Override
    public void onSuccess(String aData) {

    }

    @Override
    public void onFailure(String aData) {

    }

    class BackgroundTask extends AsyncTask<String, Void, String> {

        private boolean mShowProgressDlg;
        private ProgressDialog mProgressDialog;

        public BackgroundTask(boolean aShowProgressDlg){
            mShowProgressDlg = aShowProgressDlg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

            if(s != null) {
                onSuccess(s);
            }
            else {
                onFailure(null);
            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String dataLine = null;
                StringBuffer dataBuffer = new StringBuffer();
                while ((dataLine = br.readLine()) != null){
                    dataBuffer.append(dataLine);
                }

                return  dataBuffer.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
                return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(mShowProgressDlg) {
                mProgressDialog = new ProgressDialog(BaseActivity.this);
                mProgressDialog.show();
            }
        }
    }
}

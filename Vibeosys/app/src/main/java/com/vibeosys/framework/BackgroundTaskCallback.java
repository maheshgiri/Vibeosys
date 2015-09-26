package com.vibeosys.framework;

/**
 * Callback for handling async task states
 */
public interface BackgroundTaskCallback {

    public void onSuccess(String aData);
    public void onFailure(String aData);
}

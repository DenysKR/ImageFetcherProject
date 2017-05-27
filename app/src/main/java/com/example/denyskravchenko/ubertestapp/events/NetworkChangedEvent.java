package com.example.denyskravchenko.ubertestapp.events;

/**
 * Created by denyskravchenko on 27.05.17.
 */

public class NetworkChangedEvent {

    private boolean mIsNetworkAvailable;

    public NetworkChangedEvent(boolean mIsNetworkAvailable) {
        this.mIsNetworkAvailable = mIsNetworkAvailable;
    }

    public boolean isIsNetworkAvailable() {
        return mIsNetworkAvailable;
    }
}

package com.example.denyskravchenko.ubertestapp.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.denyskravchenko.ubertestapp.events.NetworkChangedEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by denyskravchenko on 27.05.17.
 */

public class NetworkDetector extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                EventBus.getDefault().post(new NetworkChangedEvent(true));
                Log.d("Network", "Internet YAY");
            } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                Log.d("Network", "No internet :(");
                EventBus.getDefault().post(new NetworkChangedEvent(false));
            }
        }
    }
}

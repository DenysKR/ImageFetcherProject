package com.example.denyskravchenko.ubertestapp;

import android.app.Application;

import com.example.denyskravchenko.ubertestapp.di.DaggerFetchingComponent;
import com.example.denyskravchenko.ubertestapp.di.FetchingComponent;

/**
 * Created by denyskravchenko on 25.05.17.
 */

public class UberApplication extends Application {
    private FetchingComponent mFetchingComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mFetchingComponent = DaggerFetchingComponent.create();
    }

    public FetchingComponent getFetchingComponent() {
        return mFetchingComponent;
    }
}

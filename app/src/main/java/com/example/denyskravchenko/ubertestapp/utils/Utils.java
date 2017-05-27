package com.example.denyskravchenko.ubertestapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by denyskravchenko on 26.05.17.
 */

public class Utils {
    private Utils() {
    }

    public static int dp2px(Context context, int dp) {
        if (context != null && dp >= 0) {
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            int px = (int) (dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
            return px;
        }
        return 0;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                Log.i("NetworkStatus :", "Network connection available.");
                return true;
            }
        }
        return false;
    }
}

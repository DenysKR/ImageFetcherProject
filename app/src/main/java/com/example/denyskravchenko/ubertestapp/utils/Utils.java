package com.example.denyskravchenko.ubertestapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

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
}

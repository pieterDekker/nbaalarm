package com.smartsports.nbaalarm.util;

import android.content.Context;
import android.os.PowerManager;

import com.smartsports.nbaalarm.R;
import com.smartsports.nbaalarm.activities.Start;

/**
 * Created by pieter on 9-10-17.
 */

public abstract class WakeLockWrapper {
    private static PowerManager.WakeLock wakeLock;

    public static void acquire(Context context) {
        if (wakeLock != null) wakeLock.release();

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, context.getString(R.string.alarm_wl_tag));
        wakeLock.acquire();
    }

    public static void release() {
        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }
}

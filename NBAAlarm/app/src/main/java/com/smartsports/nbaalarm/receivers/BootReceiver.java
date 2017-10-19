package com.smartsports.nbaalarm.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.smartsports.nbaalarm.models.Alarm;

/**
 * Created by pieter on 19-10-17.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Alarm.resetAll(context.getApplicationContext());
    }
}

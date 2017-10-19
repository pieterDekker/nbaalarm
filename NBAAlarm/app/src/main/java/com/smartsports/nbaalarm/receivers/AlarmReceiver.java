package com.smartsports.nbaalarm.receivers;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.smartsports.nbaalarm.activities.Alarm;
import com.smartsports.nbaalarm.models.Game;
import com.smartsports.nbaalarm.util.WakeLockWrapper;

/**
 * Created by pieter on 2-10-17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WakeLockWrapper.acquire(context);

        Intent alarm_activity_intent = new Intent(context, Alarm.class);

        alarm_activity_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarm_activity_intent.putExtra("alarm_bundle", intent.getBundleExtra("alarm_bundle"));

        context.startActivity(alarm_activity_intent);

        WakeLockWrapper.release();
    }
}

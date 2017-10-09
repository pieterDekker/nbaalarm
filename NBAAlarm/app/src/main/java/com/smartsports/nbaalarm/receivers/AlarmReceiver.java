package com.smartsports.nbaalarm.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by pieter on 2-10-17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String message = extras.getString("message", "Message not found in intent!");
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

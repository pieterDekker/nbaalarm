package com.smartsports.nbaalarm.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.smartsports.nbaalarm.receivers.AlarmReceiver;

import java.util.Date;

/**
 * Created by pieter on 27-9-17.
 */

public class Alarm {
    private Date triggerDateTime;

    public Alarm(Date triggerDateTime, Context context) {
        this.triggerDateTime = triggerDateTime;

        register(context);
    }

    private boolean register(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        intent.putExtra("message", "You have been woken up!");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        am.set(AlarmManager.RTC_WAKEUP, triggerDateTime.getTime(), pendingIntent);

        return true;
    }

}

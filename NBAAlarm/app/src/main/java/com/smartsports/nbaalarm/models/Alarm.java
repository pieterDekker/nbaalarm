package com.smartsports.nbaalarm.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.smartsports.nbaalarm.receivers.AlarmReceiver;

import java.util.Date;

public class Alarm {
    private Date triggerDateTime;

    private Game game;

    public Alarm(Game game, Date triggerDateTime, Context context) {
        this.triggerDateTime = triggerDateTime;
        this.game = game;
        set(context);
    }

    private void set(Context context) {
        register(context);
    }

    private boolean register(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        Bundle gameBundle = new Bundle();
        gameBundle.putParcelable("game", this.game);

        intent.putExtra("game_bundle", gameBundle);
        intent.setAction("com.smartsports.nbaalarm.ALARM_TRIGGER");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        am.set(AlarmManager.RTC_WAKEUP, triggerDateTime.getTime(), pendingIntent);

        return true;
    }

}

package com.smartsports.nbaalarm.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.smartsports.nbaalarm.receivers.AlarmReceiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public class Alarm implements Serializable {
    private Date triggerDateTime;

    private Game game;

    private String saved_alarm_filename;

    public Alarm(Game game, Date triggerDateTime, Context context) {
        this.triggerDateTime = triggerDateTime;
        this.game = game;
        set(context);
    }

    public void unset(Context context) {

    }

    public static void reset() {

    }

    private void set(Context context) {
        register(context);
        store(context);
    }

    private boolean store(Context context){
        this.saved_alarm_filename = this.toString();

        try {
            new File(this.saved_alarm_filename).createNewFile();

            FileOutputStream outputStream = context.openFileOutput(this.saved_alarm_filename, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(this);

            objectOutputStream.close();
            outputStream.close();
        } catch (FileNotFoundException fnfe) {
            Toast.makeText(context, "File " + this.saved_alarm_filename + " not found!", Toast.LENGTH_LONG).show();
            return false;
        } catch (IOException ioe) {
            Toast.makeText(context, "IOException! " + ioe.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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

    public String toString() {
        return this.triggerDateTime + "_" + this.game.toString();
    }

}

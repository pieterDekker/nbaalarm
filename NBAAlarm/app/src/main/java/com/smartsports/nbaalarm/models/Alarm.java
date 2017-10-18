package com.smartsports.nbaalarm.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.smartsports.nbaalarm.receivers.AlarmReceiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Alarm implements Serializable {
    private Date triggerDateTime;

    private Game game;

    private String saved_alarm;

    public static String getAlarmsFolderPath(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/alarms/";
    }

    public static String getAlarmFilePath(Context context, Alarm alarm) {
        return Uri.parse(Alarm.getAlarmsFolderPath(context) +  alarm.toString()).getPath();
    }

    public File getAlarmFile(Context context) {
        if (this.saved_alarm == null || this.saved_alarm.equals("")) {
            return new File(Alarm.getAlarmFilePath(context, this));
        }
        return new File(this.saved_alarm);
    }

    public Alarm(Game game, Date triggerDateTime, Context context) {
        this.triggerDateTime = triggerDateTime;
        this.game = game;
        set(context);
    }

    private void set(Context context) {
        register(context);
        store(context);
    }

    public void unset(Context context) {
        this.getAlarmFile(context).delete();
    }

    public void reset(Context context) {
        this.register(context);
    }

    private boolean store(Context context){
        this.saved_alarm = Alarm.getAlarmFilePath(context, this);
//        Log.i("Persistent Alarm", this.saved_alarm);

        try {
//            Log.i("Persistent Alarm", "starting...");
            File f = this.getAlarmFile(context);
            f.getParentFile().mkdirs();
//            Log.i("Persistent Alarm", "dirs created");
            boolean result = f.createNewFile();
//            Log.i("Persistent Alarm", "File was " + (result ? "" : "not") + " created");
            FileOutputStream outputStream = new FileOutputStream(f);
//            Log.i("Persistent Alarm", "outputstream opened");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//            Log.i("Persistent Alarm", "objectoutputstream opened");
            objectOutputStream.writeObject(this);
//            Log.i("Persistent Alarm", "object written");
            objectOutputStream.close();
//            Log.i("Persistent Alarm", "oos closed");
            outputStream.close();
//            Log.i("Persistent Alarm", "os closed");
        } catch (FileNotFoundException fnfe) {
            Toast.makeText(context, "File " + this.saved_alarm + " not found!", Toast.LENGTH_LONG).show();
//            Log.i("Persistent Alarm", "fnfe: " + fnfe.getMessage());
            return false;
        } catch (IOException ioe) {
            Toast.makeText(context, "IOException! " + ioe.getMessage(), Toast.LENGTH_LONG).show();
//            Log.i("Persistent Alarm", "ioe: " + ioe.getMessage());
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

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, this.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        am.set(AlarmManager.RTC_WAKEUP, triggerDateTime.getTime(), pendingIntent);

        return true;
    }

    public Game getGame() {
        return this.game;
    }

    @Override
    public String toString() {
        return this.triggerDateTime.getTime() + "_" + this.game.toString();
    }

    @Override
    public int hashCode() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(this.toString().getBytes());
            BigInteger bigInt = new BigInteger(1,digest.digest());
            return bigInt.intValue();
        } catch (NoSuchAlgorithmException nsaException) {
            Log.d("models/Alarm", "Developer made a mistake perhaps? NoSuchAlgorithmException: " + nsaException.getMessage());
        }

        return this.toString().hashCode();
    }
}

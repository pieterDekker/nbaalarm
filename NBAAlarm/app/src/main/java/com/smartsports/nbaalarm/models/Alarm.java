package com.smartsports.nbaalarm.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.smartsports.nbaalarm.receivers.AlarmReceiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Alarm implements Parcelable, Serializable {

    @NonNull
    private Game game;
    private Long time;
    private String saved_alarm;

    public Alarm(@NonNull Game game, Context context, Long time) {
        this.game = game;
        this.time = time;
        set(context);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(game, flags);
        dest.writeString(saved_alarm);
    }

    public static final Parcelable.Creator<Alarm> CREATOR
            = new Parcelable.Creator<Alarm>() {
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    private Alarm(Parcel in) {
        this.game = in.readParcelable(Game.class.getClassLoader());
        this.saved_alarm = in.readString();
    }

    private static String getAlarmsFolderPath(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/alarms/";
    }

    private static String getAlarmFilePath(Context context, Game game) {
        return Uri.parse(Alarm.getAlarmsFolderPath(context) +  Alarm.getAlarmFileName(game)).getPath();
    }

    private static String getAlarmFileName(Game game) {
        return game.getStart().getTime() + "_" + game.toString();
    }

    private File getAlarmFile(Context context) {
        return new File(Alarm.getAlarmFilePath(context, this.game));
    }

    private void set(Context context) {
        register(context);
        store(context);
    }

    public void unset(Context context) {
        this.getAlarmFile(context).delete();
        this.unRegister(context);
    }

    public void reset(Context context) {
        this.register(context);
    }

    public static void resetAll(Context context) {
        String alarmsFolder = context.getFilesDir().getAbsolutePath() + "/alarms/";

        File alarms = new File(alarmsFolder);

        for (File f: alarms.listFiles()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(f);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Alarm a = (Alarm) objectInputStream.readObject();

                a.reset(context);
            } catch (FileNotFoundException | ClassNotFoundException forcnfe) {
                //This should never occur, silently die.
                //TODO add proper logging
            } catch (IOException ioe) {
                Toast.makeText(context, "Unexpected failure: " + ioe.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean store(Context context){
        File f = this.getAlarmFile(context);

        if (f.exists()) {
            return true;
        }

        this.saved_alarm = f.getPath();

        try {
            f.getParentFile().mkdirs();
            boolean result = f.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(f);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            outputStream.close();
        } catch (FileNotFoundException fnfe) {
            Toast.makeText(context, "File " + this.saved_alarm + " not found!", Toast.LENGTH_LONG).show();
            return false;
        } catch (IOException ioe) {
            Toast.makeText(context, "IOException! " + ioe.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean register(Context context) {
        if (Alarm.isSet(this.game, context)) {
            return true;
        }

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        Bundle alarmBundle = new Bundle();
        alarmBundle.putParcelable("alarm", this);

        intent.putExtra("alarm_bundle", alarmBundle);
        intent.setAction("com.smartsports.nbaalarm.ALARM_TRIGGER");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, this.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, this.time, pendingIntent);

        return true;
    }

    private boolean unRegister(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("com.smartsports.nbaalarm.ALARM_TRIGGER");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, this.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pendingIntent);

        return true;
    }

    @NonNull
    public Game getGame() {
        return this.game;
    }

    public static boolean isSet(Game game, Context context) {
        return (new File(Alarm.getAlarmFilePath(context, game))).exists();
    }

    @Override
    public String toString() {
        return Alarm.getAlarmFileName(this.game);
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

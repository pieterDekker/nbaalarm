package com.smartsports.nbaalarm.models;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import com.smartsports.nbaalarm.R;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by pieter on 22-9-17.
 */

public class Game implements Parcelable, Serializable {
    private Team team_1;
    private Team team_2;
    private Date start;
    private boolean alarmSet;

    public Game(Team team_1, Team team_2, Date start) {
        this.team_1 = team_1;
        this.team_2 = team_2;
        this.start = start;
    }

    private Game(Parcel in) {
        this.team_1 = in.readParcelable(Team.class.getClassLoader());
        this.team_2 = in.readParcelable(Team.class.getClassLoader());
        this.start = new Date(in.readLong());
        this.alarmSet = in.readByte() != 0;
    }

    public String toString() {
        return this.team_1.getName() + "_vs_" + this.team_2.getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(team_1, flags);
        out.writeParcelable(team_2, flags);
        out.writeLong(start.getTime());
        out.writeByte((byte) (alarmSet ? 1 : 0));
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public Team getTeam_1() {
        return team_1;
    }

    public Team getTeam_2() {
        return team_2;
    }

    public Date getStart() {
        return start;
    }

    public void setAlarm(Context context) {
        this.alarmSet = true;
        new Alarm(this, context);
    }

    public boolean isAlarmSet(Context context) {
        return Alarm.isSet(this, context);
    }

    public void unsetAlarm(Context context) {
        this.alarmSet = false;

        (new Alarm(this, context)).unset(context);
    }
}

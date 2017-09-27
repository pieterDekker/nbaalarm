package com.smartsports.nbaalarm.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by pieter on 22-9-17.
 */

public class Game implements Parcelable {
    private String team_1;
    private String team_2;
    private Date start;
    private Date end;
    private boolean alarm;

    public Game(String team_1, String team_2, Date start, Date end) {
        this.team_1 = team_1;
        this.team_2 = team_2;
        this.start = start;
        this.end = end;
        this.alarm = false;
    }

    public Game(Parcel in) {
        this.team_1 = in.readString();
        this.team_2 = in.readString();
        this.start = new Date(in.readLong());
        this.end = new Date(in.readLong());
        this.alarm = in.readByte() != 0;
    }

    public String toString() {
        return "The " + this.team_1 + " play against the " + this.team_2 + " at " + this.start.getTime();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(team_1);
        out.writeString(team_2);
        out.writeLong(start.getTime());
        out.writeLong(end.getTime());
        out.writeByte((byte) (alarm ? 1 : 0));
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public String getTeam_1() {
        return team_1;
    }

//    public void setTeam_1(String team_1) {
//        this.team_1 = team_1;
//    }

    public String getTeam_2() {
        return team_2;
    }

//    public void setTeam_2(String team_2) {
//        this.team_2 = team_2;
//    }

    public Date getStart() {
        return start;
    }

//    public void setStart(Date start) {
//        this.start = start;
//    }

    public Date getEnd() {
        return end;
    }

//    public void setEnd(Date end) {
//        this.end = end;
//    }

    public void setAlarm(boolean b) {
        this.alarm = b;
    }

    public boolean getAlarm() {
        return this.alarm;
    }
}

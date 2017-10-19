package com.smartsports.nbaalarm.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dejon on 10/11/2017.
 */

public class Team implements Parcelable {
    private String name;
    private String id;

    public Team(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Team(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(id);
    }

    public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public boolean equals(Team t) {
        return t.name.equals(this.name) && t.id.equals(this.id);
    }

    public boolean equals(String s) {
        return s.equals(this.id) || s.equals(this.name);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}

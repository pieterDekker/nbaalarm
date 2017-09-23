package com.smartsports.nbaalarm.Data;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by pieter on 22-9-17.
 */

public class Game {
    private String team_1;

    private String team_2;

    private Date start;

    public Game(String team_1, String team_2, Date start) {
        this.team_1 = team_1;
        this.team_2 = team_2;
        this.start = start;
    }

    public String toString() {
        return "The " + this.team_1 + " play against the " + this.team_2 + " at " + this.start.getTime();
    }

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
}

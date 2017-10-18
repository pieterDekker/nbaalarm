package com.smartsports.nbaalarm.connections;

import android.util.Log;
import android.widget.Button;

import com.smartsports.nbaalarm.R;
import com.smartsports.nbaalarm.activities.Start;
import com.smartsports.nbaalarm.models.Game;
import com.smartsports.nbaalarm.models.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by dejon on 10/11/2017.
 */

public class NBADatabaseConnector extends DatabaseConnector {
    private ArrayList<Game> games;
    private String teamfilter;

    public NBADatabaseConnector(Start main_activity, String teamfilter) {
        super(main_activity);
        games = new ArrayList<>();
        this.teamfilter = teamfilter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Button refreshButton = (Button) main_activity.findViewById(R.id.asRefreshButton);
        refreshButton.setText("Fetching data");
    }

    @Override
    protected void onPostExecute(Void v) {
        parseNBAGames();
        //TODO remove test statements
        this.games.add(new Game(new Team("testteam", "0"), new Team("Testteam2", "0"), new Date(System.currentTimeMillis() + 30000)));
        main_activity.setGames(games);

        main_activity.showGames(games);
        super.onPostExecute(null);
    }

    private void parseNBAGames() {
        try {
            JSONArray leagueGames = new JSONObject(result).getJSONObject("league").getJSONArray("standard");
            JSONObject game;

            // Look for next match (based on system time)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddhh:mm:ss.SSS");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date currentTime = new Date();
            int low = 0, high = leagueGames.length();
            Log.d("Search", "High: " + high);
            while (low+1 < high) {
                int mid = (low + high) / 2;
                Date temptime;
                try {
                    temptime = sdf.parse(leagueGames.getJSONObject(mid).getString("startTimeUTC").replaceAll("[TZ]", ""));
                } catch (ParseException e) {
                    Log.d("DB init", "ParseException");
                    return;
                }
                if (temptime.before(currentTime)) {
                    low = mid;
                } else if (temptime.after(currentTime)) {
                    high = mid;
                }
            }

            // Insert upcoming ? matches in games array
            int MATCHES_IN_DATABASE = 30, MAX_SEARCHES = 100;
            Date startTime;
            int i = high, j=0;
            String team1, team2;
            while(((game = leagueGames.getJSONObject(i)) != null) && (i<(high+MAX_SEARCHES)) && (j<MATCHES_IN_DATABASE)) {
                try {
                    startTime = sdf.parse(game.getString("startTimeUTC").replaceAll("[TZ]", ""));
                    team1 = game.getJSONObject("hTeam").getString("teamId");
                    team2 = game.getJSONObject("vTeam").getString("teamId");
                    Log.d("NBAConn", teamfilter + " 1: " + team1 + " 2:" + team2);
                    if(teamfilter.equals("none") || teamfilter.equals(team1) || teamfilter.equals(team2)) {
                        games.add(new Game(getTeamById(team1), getTeamById(team2), startTime));
                        j++;
                    }
                } catch (ParseException e) {
                    Log.d("DB init", "ParseException");
                }
                i++;
            }
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.getMessage().toString());
        } finally {
            Button refreshButton = (Button) main_activity.findViewById(R.id.asRefreshButton);
            refreshButton.setText("Refresh Game List");
        }
    }

    private Team getTeamById(String id) {
        for(Team t : main_activity.getTeams()) {
            if(t.getId().equals(id)) return t;
        }
        return new Team(id, id);
    }
}
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

    public NBADatabaseConnector(Start main_activity) {
        super(main_activity);
        games = new ArrayList<>();
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

            // Insert upcoming ? teams in games array
            int TEAMS_IN_DATABASE = 30;
            Date startTime;
            int i = high;
            while(((game = leagueGames.getJSONObject(i)) != null) && (i<(high+TEAMS_IN_DATABASE))) {
                try {
                    startTime = sdf.parse(game.getString("startTimeUTC").replaceAll("[TZ]", ""));
                    games.add(new Game(getTeamById(game.getJSONObject("hTeam").getString("teamId")), getTeamById(game.getJSONObject("vTeam").getString("teamId")), startTime, startTime));
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
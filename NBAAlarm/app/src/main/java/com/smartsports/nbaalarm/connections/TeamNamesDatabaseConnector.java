package com.smartsports.nbaalarm.connections;

import android.util.Log;
import android.widget.Button;

import com.smartsports.nbaalarm.R;
import com.smartsports.nbaalarm.activities.Start;
import com.smartsports.nbaalarm.models.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dejon on 10/11/2017.
 */

public class TeamNamesDatabaseConnector extends DatabaseConnector {
    private ArrayList<Team> teams;

    public TeamNamesDatabaseConnector(Start main_activity) {
        super(main_activity);
        teams = new ArrayList<Team>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Button refreshButton = (Button) main_activity.findViewById(R.id.asRefreshButton);
        refreshButton.setText("Fetching data");
    }

    @Override
    protected void onPostExecute(Void v) {
        parseTeamNames();
        main_activity.setTeams(teams);
        super.onPostExecute(null);
        main_activity.getDataNBA(null);
    }

    private void parseTeamNames() {
        try {
            JSONArray jTeams = new JSONObject(result).getJSONObject("league").getJSONArray("standard");
            JSONObject team;
            int i = 0;
            String name, id;
            while((team = jTeams.getJSONObject(i)) != null) {
                name = team.getString("fullName");
                id = team.getString("teamId");
                teams.add(new Team(name, id));
                i++;
                Log.d("Adding team", "Name: " + name + " - " + id);
            }

        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.getMessage().toString());
        }
    }
}

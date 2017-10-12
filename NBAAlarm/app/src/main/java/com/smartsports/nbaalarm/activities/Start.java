package com.smartsports.nbaalarm.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.smartsports.nbaalarm.R;
import com.smartsports.nbaalarm.adapters.GameAdapter;
import com.smartsports.nbaalarm.connections.NBADatabaseConnector;
import com.smartsports.nbaalarm.connections.TeamNamesDatabaseConnector;
import com.smartsports.nbaalarm.models.Alarm;
import com.smartsports.nbaalarm.models.Game;
import com.smartsports.nbaalarm.models.Team;

import java.util.ArrayList;
import java.util.Date;

public class Start extends AppCompatActivity {
    private ArrayList<Game> games;
    private ArrayList<Team> teams;
    NBADatabaseConnector gamesConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.err.println("Start");
        setContentView(R.layout.activity_start);
	getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        View view =getSupportActionBar().getCustomView();
        getDataTeams();
    }

    private void getDataTeams() {
        TeamNamesDatabaseConnector teamCollector = new TeamNamesDatabaseConnector(this);
        teamCollector.execute(getString(R.string.nba_team_database_url));
    }

    public void getDataNBA(View view) {
        if(gamesConnector != null && gamesConnector.isRunning()) {
            Button refreshButton = (Button) findViewById(R.id.asRefreshButton);
            refreshButton.setText("Fetching data faster");
            return;
        }
        gamesConnector = new NBADatabaseConnector(this);
        gamesConnector.execute(getString(R.string.nba_database_url));
    }

    public void showGames(ArrayList<Game> games) {
        GameAdapter adapter;
        adapter = new GameAdapter(this, games);
        ListView game_list = (ListView) findViewById(R.id.game_list);
        game_list.setAdapter(adapter);
        game_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int item, long position) {
                Log.d("Main Activity","Team " +  Start.this.games.get(item).getTeam_1() + " vs " + Start.this.games.get(item).getTeam_2() );
                Intent detailledGameIntent = new Intent(getApplicationContext(), DetailedGame.class);
                detailledGameIntent.putExtra("game", Start.this.games.get(item));
                startActivity(detailledGameIntent);
            }
        });
    }

    public void setGames(ArrayList<Game> g) {
        this.games = g;
    }

    public void setTeams(ArrayList<Team> t) {
        this.teams = t;
    }

    public ArrayList<Team> getTeams() { return teams; }
}
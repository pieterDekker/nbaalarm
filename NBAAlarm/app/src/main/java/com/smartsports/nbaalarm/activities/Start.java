package com.smartsports.nbaalarm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.smartsports.nbaalarm.R;
import com.smartsports.nbaalarm.adapters.GameAdapter;
import com.smartsports.nbaalarm.models.Alarm;
import com.smartsports.nbaalarm.models.Game;
import com.smartsports.nbaalarm.connections.NBADatabaseConnector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Start extends AppCompatActivity {
    private ArrayList<Game> games;
    NBADatabaseConnector connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.err.println("Start");
        setContentView(R.layout.activity_start);
        getDataNBA(null);
    }

    public void setAlarm() {
        new Alarm(new Date(System.currentTimeMillis() + 2000), getApplicationContext());
    }

    public void getDataNBA(View view) {
        if(connector != null && connector.isRunning()) {
            Button refreshButton = (Button) findViewById(R.id.asRefreshButton);
            refreshButton.setText("Fetching data faster");
            return;
        }
        connector = new NBADatabaseConnector(this);
        connector.execute(getString(R.string.nba_database_url));
        games = connector.games;
    }

    public void showGames(View view) {
        if(games==null) return;
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

    private ArrayList<Game> getDataTest() {
        games = new ArrayList<Game>();
        // Test data
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            games.add(new Game("Tigers", "Bears", sdf.parse("28-09-2017 18:10:00"), sdf.parse("28-09-2017 20:00:01")));
            games.add(new Game("Snakes", "Elephants", sdf.parse("30-09-2017 13:00:00"), sdf.parse("30-09-2017 15:15:00")));
            games.add(new Game("Mice", "Cats", sdf.parse("04-10-2017 18:00:00"), sdf.parse("04-10-2017 20:00:00")));
        } catch (Exception e) {
            Log.d("Main view", "Error converting str to date");
            System.exit(1);
        }

        return games;
    }
}
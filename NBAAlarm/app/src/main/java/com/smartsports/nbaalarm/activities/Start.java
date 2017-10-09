package com.smartsports.nbaalarm.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.smartsports.nbaalarm.R;
import com.smartsports.nbaalarm.adapters.GameAdapter;
import com.smartsports.nbaalarm.models.Game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Start extends AppCompatActivity {
    private ArrayList<Game> games;
    private NBADatabaseConnector connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.err.println("Start");
        setContentView(R.layout.activity_start);

        games = getDataTest();
        getDataNBA("http://data.nba.net/10s/prod/v1/2017/schedule.json");
        showGames();
    }

    public void showGames() {
        GameAdapter adapter;
        try {
            adapter = new GameAdapter(this, games);
        } catch (Exception e) {
            Log.d("showGames", "Getting games from connector failed");
            return;
        }
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

    private ArrayList<Game> getDataNBA(String link) {
        connector = new NBADatabaseConnector();
        connector.execute(link);
        return getDataTest();
    }

    class NBADatabaseConnector extends AsyncTask<String, String, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(Start.this);
        InputStream inputStream = null;
        String result = "";
        ArrayList<Game> games = new ArrayList<>();

        @Override
        protected Void doInBackground(String... params) {
            Log.d("MAIN XD", "Started doInBg()");
            InputStream inputStream;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (Exception e) {
                Log.w("Get NBA connection", e.toString());
                return null;
            }

            try {
                // Set up HTTP post
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                // Convert response to string using String Builder
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();
                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

            } catch (Exception e) {
                Log.w("Get NBA connection", e.toString());
            } finally {
                urlConnection.disconnect();
            }

            return null;
        }

        protected void onPostExecute(Void v) {
            //parse JSON data
            try {
                JSONObject data = new JSONObject(result);
                JSONObject league = data.getJSONObject("league");
                JSONArray leagueGames = league.getJSONArray("standard");
                JSONObject game;

                Date startTime;
                int i = 0;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-ddhh:mm:ss.sss");
                while(((game = leagueGames.getJSONObject(1)) != null) && (i<30)) {
                    try {
                        startTime = sdf.parse(game.getString("startTimeUTC").replaceAll("[TZ]",""));
                        games.add(new Game(game.getJSONObject("hTeam").getString("teamId"), game.getJSONObject("vTeam").getString("teamId"), startTime, startTime));
                    } catch (Exception e) {
                        Log.d("DB init", "Game not added " + e.toString());
                    }
                    i++;
                }
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.getMessage().toString());
            }
        }
    }
}
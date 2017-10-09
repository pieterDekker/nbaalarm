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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class Start extends AppCompatActivity {
    private ArrayList<Game> games;
    private NBADatabaseConnector connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.err.println("Start");
        setContentView(R.layout.activity_start);

        connector = new NBADatabaseConnector();
        games = getDataTest();
        showGames(null);
    }

    public void showGames(View view) {
        GameAdapter adapter;
        try {
            Log.d("Connector", "ready: " + connector.ready + " running: " + connector.running);
            if(connector.ready) {
                games = connector.games;
            } else {
                if(!connector.running) {
                    connector.execute(getString(R.string.nba_database_url));
                }
            }
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

    class NBADatabaseConnector extends AsyncTask<String, String, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(Start.this);
        private InputStream inputStream = null;
        private String result = "";
        public ArrayList<Game> games = new ArrayList<>();
        public boolean ready = false;
        public boolean running = false;

        @Override
        protected Void doInBackground(String... params) {
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
                ready = false;      // New data is available
                running = true;     // Class is currently fetching new data
                JSONObject data = new JSONObject(result);
                JSONObject league = data.getJSONObject("league");
                JSONArray leagueGames = league.getJSONArray("standard");
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
                        running = false;
                        return;
                    }
                    if (temptime.before(currentTime)) {
                        low = mid;
                    } else if (temptime.after(currentTime)) {
                        high = mid;
                    }
                }

                Date startTime;
                int i = high;
                while(((game = leagueGames.getJSONObject(i)) != null) && (i<(high+30))) {
                    try {
                        startTime = sdf.parse(game.getString("startTimeUTC").replaceAll("[TZ]", ""));
                        Log.d("Starttime", sdf.format(startTime));
                        games.add(new Game(game.getJSONObject("hTeam").getString("teamId"), game.getJSONObject("vTeam").getString("teamId"), startTime, startTime));
                    } catch (ParseException e) {
                        Log.d("DB init", "ParseException");
                    }
                    i++;
                }
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.getMessage().toString());
            } finally {
                ready = true;
                running = false;
            }
        }
    }
}
package com.smartsports.nbaalarm.connections;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import com.smartsports.nbaalarm.R;
import com.smartsports.nbaalarm.activities.Start;
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

/**
 * Created by dejon on 10/11/2017.
 */

public class NBADatabaseConnector extends AsyncTask<String, String, Void> {
    private Start main_activity;
    private InputStream inputStream = null;
    private String result = "";
    public ArrayList<Game> games = new ArrayList<>();
    boolean running = false;

    public NBADatabaseConnector(Start main_activity) {
        super();
        this.main_activity = main_activity;
    }

    @Override
    protected void onPreExecute() {
        running = true;
        Button refreshButton = (Button) main_activity.findViewById(R.id.asRefreshButton);
        refreshButton.setText("Fetching data");
    }

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

    @Override
    protected void onPostExecute(Void v) {
        //parse JSON data
        try {
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
                    games.add(new Game(game.getJSONObject("hTeam").getString("teamId"), game.getJSONObject("vTeam").getString("teamId"), startTime, startTime));
                } catch (ParseException e) {
                    Log.d("DB init", "ParseException");
                }
                i++;
            }
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.getMessage().toString());
        } finally {
            main_activity.showGames(null);
            Button refreshButton = (Button) main_activity.findViewById(R.id.asRefreshButton);
            refreshButton.setText("Refresh Game List");
            running = false;
        }
    }

    public boolean isRunning() {
        return running;
    }
}
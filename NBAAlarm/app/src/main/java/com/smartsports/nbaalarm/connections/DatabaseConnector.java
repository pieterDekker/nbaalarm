package com.smartsports.nbaalarm.connections;

import android.os.AsyncTask;
import android.util.Log;

import com.smartsports.nbaalarm.activities.Start;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dejon on 10/11/2017.
 */

public abstract class DatabaseConnector extends AsyncTask<String, String, Void> {

    protected Start main_activity;
    private InputStream inputStream = null;
    protected String result = "";
    boolean running = false;

    public DatabaseConnector(Start main_activity) {
        super();
        this.main_activity = main_activity;
    }

    @Override
    protected void onPreExecute() {
        // Start gathering data, tell others to not do duplicate work
        running = true;
    }

    @Override
    protected Void doInBackground(String... params) {
        Log.d("DatabaseConnector", "Start connection");
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
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}

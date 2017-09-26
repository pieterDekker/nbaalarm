package com.smartsports.nbaalarm.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.smartsports.nbaalarm.R;
import com.smartsports.nbaalarm.adapters.GameAdapter;
import com.smartsports.nbaalarm.models.Game;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void showGames(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        ArrayList<Game> games = new ArrayList<Game>();

        try {
            games.add(new Game("tigers", "bears", sdf.parse("28-09-2017 18:00:00"), sdf.parse("28-09-2017 20:00:00")));
            games.add(new Game("snakes", "elephants", sdf.parse("30-09-2017 12:00:00"), sdf.parse("30-09-2017 14:15:00")));
            games.add(new Game("mice", "cats", sdf.parse("04-10-2017 18:00:00"), sdf.parse("04-10-2017 20:00:00")));
        } catch (Exception e) {
            System.err.println("Error converting str to date");
            System.exit(1);
        }

        GameAdapter adapter = new GameAdapter(this, games);
        ListView game_list = (ListView) findViewById(R.id.game_list);
        game_list.setAdapter(adapter);
    }

}

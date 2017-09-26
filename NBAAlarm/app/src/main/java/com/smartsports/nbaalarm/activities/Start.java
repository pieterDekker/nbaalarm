package com.smartsports.nbaalarm.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.smartsports.nbaalarm.R;
import com.smartsports.nbaalarm.models.Game;
import com.smartsports.nbaalarm.adapters.GameAdapter;

import java.util.ArrayList;
import java.util.Date;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void showGames(View view) {
        ArrayList<Game> games = new ArrayList<Game>();

        games.add(new Game("tigers", "bears", new Date()));
        games.add(new Game("snakes", "elephants", new Date()));
        games.add(new Game("mice", "cats", new Date()));

        GameAdapter adapter = new GameAdapter(this, games);

        ListView game_list = (ListView) findViewById(R.id.game_list);

        game_list.setAdapter(adapter);
    }

}

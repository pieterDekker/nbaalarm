package com.smartsports.nbaalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.smartsports.nbaalarm.Data.Game;
import com.smartsports.nbaalarm.Data.GameAdapter;

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

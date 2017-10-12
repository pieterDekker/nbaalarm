package com.smartsports.nbaalarm.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.smartsports.nbaalarm.R;
import com.smartsports.nbaalarm.adapters.TeamAdapter;
import com.smartsports.nbaalarm.models.Team;

import java.util.ArrayList;

public class TeamList extends AppCompatActivity {
    private ArrayList<Team> teams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        View view = getSupportActionBar().getCustomView();

        if(getIntent().hasExtra("teams")) {
            this.teams = (ArrayList<Team>) getIntent().getExtras().get("teams");
        }
        showTeams(teams);
    }

    public void showTeams(ArrayList<Team> teams) {
        TeamAdapter adapter;
        adapter = new TeamAdapter(this, teams);
        ListView team_list = (ListView) findViewById(team_list);
        team_list.setAdapter(adapter);
        team_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int item, long position) {
                Intent detailledGameIntent = new Intent(getApplicationContext(), DetailedGame.class);
                detailledGameIntent.putExtra("team", TeamList.this.teams.get(item));
                startActivity(detailledGameIntent);
            }
        });
    }

}

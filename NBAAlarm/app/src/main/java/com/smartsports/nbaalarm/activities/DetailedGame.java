package com.smartsports.nbaalarm.activities;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.smartsports.nbaalarm.R;
import com.smartsports.nbaalarm.models.Game;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by dejon on 9/26/2017.
 */

public class DetailedGame extends AppCompatActivity {
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_game_view);
	    getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar2);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        View view = getSupportActionBar().getCustomView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().hasExtra("game")) {
            this.game = (Game) getIntent().getExtras().get("game");
            this.setTeamNames();
            this.setDate();
            this.setTimes();
            this.setAlarmText();
        }
    }

    public void setAlarm(View view) {
        if(!game.isAlarmSet(this.getApplicationContext())) {
            // Remove an alarm for the current game
            String timeOffset = ((Spinner) findViewById(R.id.dgvAlarmOffset)).getSelectedItem().toString();
            Log.d("DetailedGame", timeOffset);
            Long time;
            if(timeOffset.equals(getString(R.string.alarm_dropdown_0))) {
                game.setAlarm(this.getApplicationContext(), game.getStart().getTime());
            } else if (timeOffset.equals(getString(R.string.alarm_dropdown_0_10))) {
                game.setAlarm(this.getApplicationContext(), System.currentTimeMillis()+(10*1000));
            } else if (timeOffset.equals(getString(R.string.alarm_dropdown_15))) {
                game.setAlarm(this.getApplicationContext(), game.getStart().getTime()-(15*60*1000));
            } else if (timeOffset.equals(getString(R.string.alarm_dropdown_10))) {
                game.setAlarm(this.getApplicationContext(), game.getStart().getTime()-(10*60*1000));
            } else if (timeOffset.equals(getString(R.string.alarm_dropdown_5))) {
                game.setAlarm(this.getApplicationContext(), game.getStart().getTime()-(5*60*1000));
            } else {
                game.setAlarm(this.getApplicationContext());
            }
        } else {
            // Set an alarm for the current game
            game.unsetAlarm(this.getApplicationContext());
        }
        // Confirm to the user that the alarm is set
        this.setAlarmText();
    }

    private void setTeamNames() {
        TextView dgvTeam1 = (TextView) findViewById(R.id.dgvTeam1);
        dgvTeam1.setText(game.getTeam_1().getName());
        TextView dgvTeam2 = (TextView) findViewById(R.id.dgvTeam2);
        dgvTeam2.setText(game.getTeam_2().getName());
    }

    private void setDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_format));
        sdf.setTimeZone(TimeZone.getTimeZone(getString(R.string.current_timezone)));
        TextView dgvDate = (TextView)findViewById(R.id.dgvDate);
        dgvDate.setText(sdf.format(game.getStart()));
    }

    private void setTimes() {
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.time_format));
        sdf.setTimeZone(TimeZone.getTimeZone(getString(R.string.current_timezone)));
        TextView dgvStartingTime = (TextView)findViewById(R.id.dgvStartingTime);
        dgvStartingTime.setText(getString(R.string.starting_time) + " " + sdf.format(game.getStart()));
    }

    private void setAlarmText() {
        // Set alarm text
        TextView dgvAlarmIsSet = (TextView) findViewById(R.id.dgvAlarmIsSet);
        Button dgvSetAlarmButton = (Button) findViewById(R.id.dgvSetAlarmButton);
        Spinner dgvAlarmOffset = (Spinner) findViewById(R.id.dgvAlarmOffset);
        if(game.isAlarmSet(this.getApplicationContext())) {
            dgvAlarmOffset.setVisibility(View.INVISIBLE);
            dgvAlarmIsSet.setText(getString(R.string.alarm_set));
            dgvSetAlarmButton.setText(getString(R.string.remove_alarm));
        } else {
            dgvAlarmOffset.setVisibility(View.VISIBLE);
            dgvAlarmIsSet.setText(getString(R.string.alarm_not_set));
            dgvSetAlarmButton.setText(getString(R.string.set_alarm));
        }
    }
}

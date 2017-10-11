package com.smartsports.nbaalarm.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smartsports.nbaalarm.R;
import com.smartsports.nbaalarm.models.*;
import com.smartsports.nbaalarm.models.Alarm;

import java.text.SimpleDateFormat;
import java.util.Date;
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

        if(getIntent().hasExtra("game")) {
            this.game = (Game) getIntent().getExtras().get("game");
            this.setTeamNames();
            this.setDate();
            this.setTimes();
            this.setAlarmText();
        }
    }

    public void setAlarm(View view) {
        if(game.getAlarm()) {
            // Remove an alarm for the current game
            game.setAlarm(false);
        } else {
            // Set an alarm for the current game
            com.smartsports.nbaalarm.models.Alarm alarm = new Alarm(
                    new Date(System.currentTimeMillis()),
                    this.getApplicationContext()
            );
            game.setAlarm(true);
        }
        // Confirm to the user that the alarm is set
        this.setAlarmText();
    }

    private void setTeamNames() {
        TextView dgvTeam1 = (TextView) findViewById(R.id.dgvTeam1);
        dgvTeam1.setText(game.getTeam_1());
        TextView dgvTeam2 = (TextView) findViewById(R.id.dgvTeam2);
        dgvTeam2.setText(game.getTeam_2());
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
        TextView dgvEndTime = (TextView)findViewById(R.id.dgvEndTime);
        dgvEndTime.setText(getString(R.string.end_time) + " " + sdf.format(game.getEnd()));
    }

    private void setAlarmText() {
        // Set alarm text
        TextView dgvAlarmIsSet = (TextView) findViewById(R.id.dgvAlarmIsSet);
        Button dgvSetAlarmButton = (Button) findViewById(R.id.dgvSetAlarmButton);
        if(game.getAlarm()) {
            dgvAlarmIsSet.setText(getString(R.string.alarm_set));
            dgvSetAlarmButton.setText(getString(R.string.remove_alarm));
        } else {
            dgvAlarmIsSet.setText(getString(R.string.alarm_not_set));
            dgvSetAlarmButton.setText(getString(R.string.set_alarm));
        }
    }
}

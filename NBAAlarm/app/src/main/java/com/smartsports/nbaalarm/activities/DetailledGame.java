package com.smartsports.nbaalarm.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.smartsports.nbaalarm.R;
import com.smartsports.nbaalarm.models.Game;

import java.text.SimpleDateFormat;

/**
 * Created by dejon on 9/26/2017.
 */

public class DetailledGame extends AppCompatActivity {
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailled_game_view);

        if(getIntent().hasExtra("game")) {
            this.game = (Game) getIntent().getExtras().get("game");
            this.setTeamNames();
            this.setDate();
            this.setTimes();
            this.setAlarmText();
        }
    }

    public void setAlarm(Game game) {
        // Set an alarm for the current game being viewed
        game.setAlarm(true);
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
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.dateformat));
        TextView dgvDate = (TextView)findViewById(R.id.dgvDate);
        dgvDate.setText(sdf.format(game.getStart()));
    }

    private void setTimes() {
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.timeformat));
        TextView dgvStartingTime = (TextView)findViewById(R.id.dgvStartingTime);
        dgvStartingTime.setText(sdf.format(game.getStart()));
        TextView dgvEndTime = (TextView)findViewById(R.id.dgvEndTime);
        dgvStartingTime.setText(sdf.format(game.getEnd()));
    }

    private void setAlarmText() {
        // Set alarm text
        TextView dgvAlarmIsSet = (TextView) findViewById(R.id.dgvAlarmIsSet);
        if(game.getAlarm()) {
            dgvAlarmIsSet.setText("Alarm set for the beginning of the match");
        } else {
            dgvAlarmIsSet.setText("Alarm not set");
        }
    }
}

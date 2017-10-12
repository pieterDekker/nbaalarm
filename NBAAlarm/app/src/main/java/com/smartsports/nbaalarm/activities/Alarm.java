package com.smartsports.nbaalarm.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.smartsports.nbaalarm.R;
import com.smartsports.nbaalarm.models.Game;

public class Alarm extends AppCompatActivity {
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Intent intent = getIntent();

        Bundle gameBundle = intent.getBundleExtra("game_bundle");
        Game game = gameBundle.getParcelable("game");

        TextView tvTeam_1 = (TextView) findViewById(R.id.alarm_tv_team_1);
        TextView tvTeam_2 = (TextView) findViewById(R.id.alarm_tv_team_2);
        TextView tvTime = (TextView) findViewById(R.id.alarm_tv_time);

        tvTeam_1.setText(game != null ? game.getTeam_1().getName() : "No game");
        tvTeam_2.setText(game != null ? game.getTeam_2().getName() : "No game");
        tvTime.setText(game != null ? game.getStart().toString() : "No game");

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        player = MediaPlayer.create(this, notification);
        player.setLooping(true);
        player.start();
    }

    @Override
    public void onAttachedToWindow() {
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    public void dismiss(View view) {
        player.stop();
        this.finish();
    }
}

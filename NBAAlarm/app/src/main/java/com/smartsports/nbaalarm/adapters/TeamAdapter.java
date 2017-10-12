package com.smartsports.nbaalarm.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smartsports.nbaalarm.R;
import com.smartsports.nbaalarm.models.Team;

import java.util.ArrayList;

/**
 * Created by dejon on 10/12/2017.
 */

public class TeamAdapter extends ArrayAdapter {
    public TeamAdapter(Context context, ArrayList<Team> teams) {
        super(context, 0, teams);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Team team = (Team) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.team_view, parent, false);
        }
        // Lookup view for data population
        TextView tvTeam_1 = (TextView) convertView.findViewById(R.id.tvTeamName);
        // Populate the data into the template view using the data object
        tvTeam_1.setText(team != null ? team.getName() : "Null");
        // Return the completed view to render on screen
        return convertView;
    }
}


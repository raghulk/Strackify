package com.sportstracking.strackify.adapter;

/**
 * strackify: favorite teams adapter
 * populates the favorites data in either past events, upcoming events or about team
 * uses the favorite_team_view or about_team_view layout file
 * depending on the fragment open
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.model.Team;
import com.sportstracking.strackify.ui.aboutteam.AboutTeamFragment;
import com.sportstracking.strackify.ui.pastevents.PastEventsFragment;
import com.sportstracking.strackify.ui.upcomingevents.UpcomingEventsFragment;
import com.sportstracking.strackify.utility.Values;
import com.sportstracking.strackify.utility.VolleyService;


import java.util.ArrayList;

public class FavoriteTeamsAdapter extends RecyclerView.Adapter<FavoriteTeamsAdapter.MyViewHolder> {
    private ArrayList<Team> teamsData;
    private Activity activity;
    private VolleyService volleyService;
    private Object reference;
    private String callFrom;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView teamNameView;
        public ImageView teamThumbView;

        public MyViewHolder(View v) {
            super(v);
            teamNameView = v.findViewById(R.id.teamName);
            teamThumbView = v.findViewById(R.id.teamThumb);
        }
    }

    /**
     * parametrized constructor to setup the adapter
     * @param activity activity set from
     * @param teamsData teams data array list
     * @param volleyService volley service reference
     * @param reference class reference
     * @param callFrom called from (eg. About Team)
     */
    public FavoriteTeamsAdapter(Activity activity, ArrayList<Team> teamsData, VolleyService volleyService, Object reference, String callFrom) {
        this.activity = activity;
        this.teamsData = teamsData;
        this.volleyService = volleyService;
        this.reference = reference;
        this.callFrom = callFrom;
    }

    /**
     * inflates custom favorite_about_team_view or favorite_team_view
     *
     * @param parent   parent view group
     * @param viewType view type
     * @return adapter view holder
     */
    @Override
    public FavoriteTeamsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v;
        switch (callFrom) {
            case Values.ABOUT_TEAM: {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.favorite_about_team_view, parent, false);
                break;
            }
            default: {
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.favorite_team_view, parent, false);
            }

        }
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    /**
     * displays the team name and the logo for the team
     * on clicking requests data for particular fragment called from
     *
     * @param holder   reference to view
     * @param position position in the recycler view
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String teamNameText = teamsData.get(position).getTeamName();
        if (!callFrom.equals(Values.ABOUT_TEAM)) {
            if (teamNameText.length() > 10) {
                teamNameText = teamNameText.substring(0, 7) + "..";
            }
        }

        holder.teamNameView.setText(teamNameText);

        if (!teamsData.get(position).getTeamBadge().equals("null") && !teamsData.get(position).getTeamBadge().equals(null) && !teamsData.get(position).getTeamBadge().isEmpty()) {
            volleyService.makeImageRequest(teamsData.get(position).getTeamBadge(), holder.teamThumbView);
        } else {
            volleyService.makeImageRequest("https://images.unsplash.com/photo-1563882757905-21bd5e0875fe?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2089&q=80", holder.teamThumbView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String teamName = teamsData.get(position).getTeamName();
                String teamId = teamsData.get(position).getTeamId();

                switch (callFrom) {
                    case Values
                            .PAST_EVENTS: {
                        PastEventsFragment pastEventsFragment = (PastEventsFragment) reference;
                        pastEventsFragment.makeNewRequest(teamId, teamName);
                        break;
                    }
                    case Values
                            .UPCOMING_EVENTS: {
                        UpcomingEventsFragment upcomingEventsFragment = (UpcomingEventsFragment) reference;
                        upcomingEventsFragment.makeNewRequest(teamId, teamName);
                        break;
                    }
                    case Values.ABOUT_TEAM: {
                        AboutTeamFragment aboutTeamFragment = (AboutTeamFragment) reference;
                        aboutTeamFragment.showTeamDetails(teamsData.get(position));
                        break;
                    }
                }

            }
        });
    }

    /**
     * number of items in the array list
     *
     * @return number of items in the countries list
     */
    @Override
    public int getItemCount() {
        return teamsData.size();
    }


}
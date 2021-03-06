package com.sportstracking.strackify.adapter;

/**
 * strackify: past events adapter
 * populates the events data in for past events
 * uses the event_view layout file
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.sportstracking.strackify.model.PastEvent;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.utility.VolleyService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PastEventsAdapter extends RecyclerView.Adapter<PastEventsAdapter.MyViewHolder> implements Filterable {
    private ArrayList<PastEvent> pastEventsData;
    private ArrayList<PastEvent> pastEventsDataComplete;
    private Activity activity;
    private VolleyService volleyService;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView eventNameView;
        public ImageView eventThumbView;
        public TextView eventLeagueView;
        public TextView eventDateView;
        public TextView scoreDetailsView;

        public MyViewHolder(View v) {
            super(v);
            eventNameView = v.findViewById(R.id.eventName);
            eventThumbView = v.findViewById(R.id.eventThumb);
            eventLeagueView = v.findViewById(R.id.eventLeague);
            eventDateView = v.findViewById(R.id.eventDate);
            scoreDetailsView = v.findViewById(R.id.scoreDetails);
        }
    }

    /**
     * parametrized constructor to setup the adapter
     *
     * @param activity       activity set from
     * @param pastEventsData past events data array list
     * @param volleyService  volley service reference
     */
    public PastEventsAdapter(Activity activity, ArrayList<PastEvent> pastEventsData, VolleyService volleyService) {
        this.activity = activity;
        this.pastEventsData = pastEventsData;
        this.pastEventsDataComplete = new ArrayList<>(pastEventsData);
        this.volleyService = volleyService;
    }

    /**
     * inflates custom event_view
     *
     * @param parent   parent view group
     * @param viewType view type
     * @return adapter view holder
     */
    @Override
    public PastEventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    /**
     * displays the event details for an event
     *
     * @param holder   reference to view
     * @param position position in the recycler view
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.eventNameView.setText(pastEventsData.get(position).getEventName());

        if (!pastEventsData.get(position).getEventThumbnail().isEmpty() && !pastEventsData.get(position).getEventThumbnail().equals("null")) {
            volleyService.makeImageRequest(pastEventsData.get(position).getEventThumbnail(), holder.eventThumbView);
        }

        holder.eventLeagueView.setText(pastEventsData.get(position).getEventLeague());

        if (pastEventsData.get(position).getAwayScore() != null && !pastEventsData.get(position).getAwayScore().isEmpty() && !pastEventsData.get(position).getAwayScore().equals("null")) {
            holder.scoreDetailsView.setText(pastEventsData.get(position).getHomeTeam() + " [" + pastEventsData.get(position).getHomeScore() + " - " + pastEventsData.get(position).getAwayScore() + "] " + pastEventsData.get(position).getAwayTeam());
        } else {
            holder.scoreDetailsView.setVisibility(View.GONE);
        }

        String dateTime = "";
        String time = pastEventsData.get(position).getEventTime();
        String format = "", pattern = "";
        if (!time.equals("null") && !time.equals(null) && !time.isEmpty()) {
            dateTime = pastEventsData.get(position).getEventDate() + " " + pastEventsData.get(position).getEventTime();
            format = "yyyy-MM-dd HH:mm:ss";
            pattern = "dd MMMM, yyyy @ hh:mm a";
        } else {
            dateTime = pastEventsData.get(position).getEventDate();
            format = "yyyy-MM-dd";
            pattern = "dd MMMM, yyyy";
        }
        try {
            String dateTimeStr = dateTime;
            Date date = new SimpleDateFormat(format).parse(dateTimeStr);
            String formatedDate = new SimpleDateFormat(pattern).format(date);
            holder.eventDateView.setText(formatedDate);
        } catch (Exception e) {
            holder.eventDateView.setVisibility(View.GONE);
        }
    }

    /**
     * number of items in the array list
     *
     * @return number of items in the events list
     */
    @Override
    public int getItemCount() {
        return pastEventsData.size();
    }

    /**
     * search filter for events
     *
     * @return reference to filter for events
     */
    @Override
    public Filter getFilter() {
        return filter;
    }

    /**
     * filters event data on the basis of search query
     */
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<PastEvent> filteredPastEventsData = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredPastEventsData.addAll(pastEventsDataComplete);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PastEvent event : pastEventsDataComplete) {
                    if (event.getEventName().toLowerCase().contains(filterPattern)) {
                        filteredPastEventsData.add(event);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredPastEventsData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            pastEventsData.clear();
            pastEventsData.addAll((ArrayList<PastEvent>) results.values);
            notifyDataSetChanged();
        }
    };
}
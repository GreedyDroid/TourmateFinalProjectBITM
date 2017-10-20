package com.example.sayed.tourmate.events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sayed.tourmate.R;

import java.util.ArrayList;

/**
 * Created by nurud on 10/19/2017.
 */

public class EventViewAdapter extends RecyclerView.Adapter<EventViewAdapter.EventViewHolder>{

    private Context context;
    private ArrayList<UserEvent>events;

    //constructor for this adapter>>>>>>>>>>
    public EventViewAdapter(Context context, ArrayList<UserEvent> events) {
        this.context = context;
        this.events = events;
    }


    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_event, parent, false);

        EventViewHolder holder = new EventViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.areaTV.setText(events.get(position).getLocation());
        holder.dateTV.setText(events.get(position).getStartDate()
                    +" - "+events.get(position).getReturnDate());
        holder.budgetTV.setText("Budget: "+events.get(position).getBudget());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    //View Holder for Recycler View>>>>>>>>>
    public class EventViewHolder extends RecyclerView.ViewHolder{
        private TextView areaTV, dateTV, budgetTV;


        public EventViewHolder(View itemView) {
            super(itemView);

            areaTV = itemView.findViewById(R.id.tour_location_TV_single);
            dateTV = itemView.findViewById(R.id.tour_date_TV_single);
            budgetTV = itemView.findViewById(R.id.tour_budget_TV_single_view);
        }
    }
}

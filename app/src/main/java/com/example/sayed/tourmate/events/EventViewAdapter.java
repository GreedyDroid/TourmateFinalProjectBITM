package com.example.sayed.tourmate.events;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sayed.tourmate.R;
import com.example.sayed.tourmate.TransitionHelper;
import com.example.sayed.tourmate.events.event_detail.EventDetail;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nurud on 10/19/2017.
 */

public class EventViewAdapter extends RecyclerView.Adapter<EventViewAdapter.EventViewHolder> {

    private final Activity activity;
    private Context context;
    private ArrayList<UserEvent> events;

    //constructor for this adapter>>>>>>>>>>
    public EventViewAdapter(Context context, ArrayList<UserEvent> events) {
        this.context = context;
        this.events = events;
        activity = (Activity) context;
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
                + " - " + events.get(position).getReturnDate());
        holder.budgetTV.setText("Budget: " + events.get(position).getBudget());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    //View Holder for Recycler View>>>>>>>>>
    public class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView areaTV, dateTV, budgetTV;


        public EventViewHolder(View itemView) {
            super(itemView);

            areaTV = itemView.findViewById(R.id.tour_location_TV_single);
            dateTV = itemView.findViewById(R.id.tour_date_TV_single);
            budgetTV = itemView.findViewById(R.id.tour_budget_TV_single_view);

// I did This for testing purpse // TODO: start new activity for detail event info>>>

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    UserEvent selectedIvent = events.get(position);
                    Intent intent = new Intent(context, EventDetail.class).putExtra("allEvents", selectedIvent);
                    try {
                        /*context.startActivity(intent);*/
                        transitionTo(intent);
                    } catch (Exception e) {
                        Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                    }
                    /*Toast.makeText(context, "Spent Amount: "+
                            events.get(position).getAllExpenses()
                            .get(0).getSpentSector()+"\nAmount: "+events.get(position)
                            .getAllExpenses().get(0).getSpentMoney(), Toast.LENGTH_SHORT).show();*/
                }
            });
        }

        @SuppressWarnings("unchecked")
        void transitionTo(Intent i) {

            final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(activity, true);
            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs);
            context.startActivity(i, transitionActivityOptions.toBundle());
        }
    }
}

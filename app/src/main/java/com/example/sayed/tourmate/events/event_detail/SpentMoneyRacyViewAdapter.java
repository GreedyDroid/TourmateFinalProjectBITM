package com.example.sayed.tourmate.events.event_detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sayed.tourmate.R;
import com.example.sayed.tourmate.events.SpentMoneyFor;

import java.util.ArrayList;

/**
 * Created by nurud on 10/21/2017.
 */

public class SpentMoneyRacyViewAdapter extends RecyclerView.Adapter<SpentMoneyRacyViewAdapter.CostItemViewHolder>{
    private Context context;
    private ArrayList<SpentMoneyFor> spentMoneySectors;

    public SpentMoneyRacyViewAdapter(Context context, ArrayList<SpentMoneyFor> spentMoneySectors) {
        this.context = context;
        this.spentMoneySectors = spentMoneySectors;
    }

    @Override
    public CostItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_cost_view_recyclar, parent, false);

        CostItemViewHolder costViewHolder = new CostItemViewHolder(v);
        return costViewHolder;
    }

    @Override
    public void onBindViewHolder(CostItemViewHolder holder, int position) {
        holder.spent_reason_TV.setText(spentMoneySectors.get(position)
                    .getSpentSector());
        holder.spent_money_TV.setText(spentMoneySectors.get(position)
                    .getSpentMoney());
        holder.spent_time_TV.setText(spentMoneySectors.get(position)
                    .getSpentTime());
    }

    @Override
    public int getItemCount() {
        return spentMoneySectors.size();
    }

    //View Holder For RecyclerVIew
    public class CostItemViewHolder extends RecyclerView.ViewHolder{
        TextView spent_reason_TV, spent_time_TV, spent_money_TV;

        public CostItemViewHolder(View itemView) {
            super(itemView);

            spent_reason_TV = itemView.findViewById(R.id.spent_reason_TV);
            spent_money_TV = itemView.findViewById(R.id.spent_money_TV);
            spent_time_TV = itemView.findViewById(R.id.spent_time_TV);
        }
    }
}

package com.example.sayed.tourmate.events.event_detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.sayed.tourmate.R;
import com.example.sayed.tourmate.events.SpentMoneyFor;
import com.example.sayed.tourmate.events.UserEvent;

import java.util.ArrayList;

public class EventDetail extends AppCompatActivity {

    UserEvent selectedEvent;
    //For RecyclarVie List:
    private RecyclerView costListRV;
    private ArrayList<SpentMoneyFor> spentMoneySectors;
    private SpentMoneyRacyViewAdapter mSpentMoneyRacyViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //For recycler ViewList>>
        costListRV =(RecyclerView) findViewById(R.id.spentMoneySectorsListRecycler);
        //getting selected Event Object from EventViewAdapter
        Intent intent = getIntent();
        selectedEvent = (UserEvent) intent.getSerializableExtra("allEvents");
//TODO: Do database Work here>>>>>>>>>>>>>>>>>

        //If there is any data of events cost>>>>>>>
        try{
            if (selectedEvent.getAllExpenses().size()>1){
                spentMoneySectors = selectedEvent.getAllExpenses();
                createCostList(spentMoneySectors);
            }
        }catch (Exception e){ //If there is no cost data in getAllExpenses>>>>>>>>>>>>>>
            Toast.makeText(this, "Hit Plus Button And Add Your Tour Cost..", Toast.LENGTH_SHORT).show();
            ArrayList<SpentMoneyFor> emptyList = new ArrayList<SpentMoneyFor>();
            selectedEvent.setAllExpenses(emptyList);
            createCostList(emptyList);
        }



        //For Floating ActionBar
        FloatingActionButton addExpensesFab = (FloatingActionButton) findViewById(R.id.addExpensesFab);
        addExpensesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


    //Create Cost List >>>>>>>>>>>>>>
    private void createCostList(ArrayList<SpentMoneyFor> spentMoneySectors){
        mSpentMoneyRacyViewAdapter = new SpentMoneyRacyViewAdapter(this, spentMoneySectors);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        costListRV.setLayoutManager(llm);
        costListRV.setAdapter(mSpentMoneyRacyViewAdapter);
    }
}

package com.example.sayed.tourmate.events.event_detail;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.sayed.tourmate.R;
import com.example.sayed.tourmate.events.Events;
import com.example.sayed.tourmate.events.SpentMoneyFor;
import com.example.sayed.tourmate.events.UserEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EventDetail extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private String databaseEventChild;

    UserEvent selectedEvent;
    //For RecyclarVie List:
    private RecyclerView costListRV;
    private ArrayList<SpentMoneyFor> spentMoneySectors;
    private SpentMoneyRacyViewAdapter mSpentMoneyRacyViewAdapter;
    private static final String ACTION = "cost_data";

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
        spentMoneySectors = new ArrayList<>();
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
                AddCostDialogFragment addCost = new AddCostDialogFragment();
                FragmentManager fm = getFragmentManager();
                addCost.show(fm, getString(R.string.dialog_tag));
            }
        });

        //Firebase getInstance
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = database.getReference().child("User").child(userId).child("Event").child(selectedEvent.getEventKey());
        databaseEventChild = "allExpenses";
        Toast.makeText(this, ""+selectedEvent.getEventKey(), Toast.LENGTH_SHORT).show();
    }


    //Create Cost List >>>>>>>>>>>>>>
    private void createCostList(ArrayList<SpentMoneyFor> spentMoneySectors){
        mSpentMoneyRacyViewAdapter = new SpentMoneyRacyViewAdapter(this, spentMoneySectors);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        costListRV.setLayoutManager(llm);
        costListRV.setAdapter(mSpentMoneyRacyViewAdapter);
    }

    //For Receiving BroadCast
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SpentMoneyFor newCost = (SpentMoneyFor) intent.getSerializableExtra("newCost");
            spentMoneySectors.add(newCost);
            createCostList(spentMoneySectors);
        }
    };

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, new IntentFilter(ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
    }

    public void saveAllCost(View view) {
        databaseReference.child(databaseEventChild).setValue(spentMoneySectors).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EventDetail.this, "Successed", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EventDetail.this, "Fai", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Adding Data In background.................................................................................................................
    //Progress>>>>>>>>>>>>>>>>Adding Event in Fierebase Database

   /* class MyAsyncTaskAddEvent extends AsyncTask<Void, Void, Void> {


        //Update Datebase>>>>>>>>>>>>
        private boolean updateDatabase(){

            //ForUpdating Database.... using key and pursh to seperate this event from other in firebase database..
            databaseReference.child(databaseEventChild).setValue(spentMoneySectors).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
            return true;
        }
    


        @Override
        protected Void doInBackground(Void... params) {
            updateDatabase();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

    }*/
    //End of Adding Event on FireBase............
}

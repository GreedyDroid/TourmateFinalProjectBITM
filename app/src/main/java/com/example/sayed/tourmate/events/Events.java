package com.example.sayed.tourmate.events;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sayed.tourmate.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Events extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private MyAsyncTaskAddEvent asyncTaskAddEvent;
    private MyAsyncTaskLoadEvent asyncTastLoasdEvent;
    private ArrayList<UserEvent>events;

    //For Recycler View
    private RecyclerView mRecyclerView;
    private EventViewAdapter eventViewAdapter;

    //exsperement
    private ArrayList<SpentMoneyFor>expenses = new ArrayList<>();

    private String location="", budget="", startDate="", returnDate="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.event_recycler_view);
        setSupportActionBar(toolbar);

        //Firebase getInstance
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = database.getReference().child("User").child(userId).child("Event");

        //If Event Is added
        Intent intent = getIntent();
        if(intent.hasExtra("tourLocation")){
            Toast.makeText(this, ""+getIntent().getStringExtra("tourLocation"), Toast.LENGTH_SHORT).show();
            location = getIntent().getStringExtra("tourLocation");
            budget = intent.getStringExtra("tourBudget");
            startDate = intent.getStringExtra("tourStartDate");
            returnDate = intent.getStringExtra("tourReturnDate");

            //For Database

            asyncTaskAddEvent = new MyAsyncTaskAddEvent();
            asyncTaskAddEvent.execute(location, budget, startDate, returnDate);
        }


        //For Floting Action Bar
        FloatingActionButton addEventFab = (FloatingActionButton) findViewById(R.id.addEvent);
        addEventFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivity(new Intent(Events.this, AddEvent.class));
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Load All Event from Database
        asyncTastLoasdEvent = new MyAsyncTaskLoadEvent();
        asyncTastLoasdEvent.execute();
    }


    //Progress>>>>>>>>>>>>>>>>Adding Event in Fierebase Database

    class MyAsyncTaskAddEvent extends AsyncTask<String, Integer, Void> {

        boolean runningRegister;
        ProgressDialog progressDialog;
        boolean isSignUpProcessed, logInSuccess;


        //Update Datebase>>>>>>>>>>>>
        private boolean updateDatabase(String location, String budget, String startDate, String returnDate){

            //ForUpdating Database.... using key and pursh to seperate this event from other in firebase database..
            String eventKey = databaseReference.push().getKey();
            UserEvent singleEvent = new UserEvent(eventKey, location, budget, startDate, returnDate, expenses);
            databaseReference.child(eventKey).setValue(singleEvent).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    logInSuccess = true;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    logInSuccess = true;
                }
            });
            return true;
        }


        @Override
        protected Void doInBackground(String... params) {
            isSignUpProcessed = updateDatabase(params[0], params[1], params[2], params[3]);
            int i = 0;
            if (logInSuccess){
                runningRegister = false;
            }
            while(runningRegister){
                try {
                    Thread.sleep(1000);
                    if (logInSuccess){
                        break;
                    }
                    if(isSignUpProcessed){
                        runningRegister = false;
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(i++ == 100){
                    runningRegister = false;
                }

                publishProgress(i);
            }
            //don't touch dialog here it'll break the application
            //do some lengthy stuff like calling login webservice
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage("Signing In\n  Time: "+String.valueOf(values[0]));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runningRegister = true;
            isSignUpProcessed= false;
            logInSuccess= false;

            progressDialog = new ProgressDialog(Events.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Updating Data...");
            progressDialog.show();

            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    runningRegister = false;
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

    }
    //End of Adding Event on FireBase............



    //For Loading data from database>>>>>

    class MyAsyncTaskLoadEvent extends AsyncTask<Void, Integer, Void> {

        boolean runningRegister;
        ProgressDialog progressDialog;
        boolean isSignUpProcessed, logInSuccess;

        //Load AllEvent From Firebase
        private boolean loadEvents() {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    events = new ArrayList<UserEvent>();
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        UserEvent event = snapshot.getValue(UserEvent.class);
                        events.add(event);
                    }
                    try{
                        //For Recycler View
                        eventViewAdapter = new EventViewAdapter(Events.this, events);
                        LinearLayoutManager llm = new LinearLayoutManager(Events.this);
                        mRecyclerView.setLayoutManager(llm);
                        mRecyclerView.setAdapter(eventViewAdapter);
                    }catch (Exception e){

                    }
                    logInSuccess = true;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    logInSuccess = true;
                }
            });
            return true;
        }

        @Override
        protected Void doInBackground(Void... params) {
            isSignUpProcessed = loadEvents();
            int i = 0;
            if (logInSuccess){
                runningRegister = false;
            }
            while(runningRegister){
                try {
                    Thread.sleep(1000);
                    if (logInSuccess){
                        break;
                    }
                    if(isSignUpProcessed){
                        runningRegister = false;
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(i++ == 100){
                    runningRegister = false;
                }

                publishProgress(i);
            }
            //don't touch dialog here it'll break the application
            //do some lengthy stuff like calling login webservice
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage("Loading Events..\n  Time: "+String.valueOf(values[0]));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runningRegister = true;
            isSignUpProcessed= false;
            logInSuccess= false;

            progressDialog = new ProgressDialog(Events.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Updating Data...");
            progressDialog.show();

            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    runningRegister = false;
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();
        }

    }


}

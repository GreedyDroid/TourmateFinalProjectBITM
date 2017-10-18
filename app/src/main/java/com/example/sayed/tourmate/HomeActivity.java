package com.example.sayed.tourmate;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sayed.tourmate.databinding.ActivityHomeBinding;
import com.example.sayed.tourmate.events.Events;
import com.example.sayed.tourmate.login_signup.LoginActivity;
import com.example.sayed.tourmate.login_signup.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    //For FireBase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   // mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_events:
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_nearby:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_weather:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_settings:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        //FireBase Authentication Check User Is logged IN>>>
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("User");

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    // TODO:  GO to Profile Activity
                    binding.layoutForSignUp.setVisibility(View.GONE);
                    binding.layoutForProfile.setVisibility(View.VISIBLE);
                    Toast.makeText(HomeActivity.this, "User Loged in: "
                            +user.getEmail(), Toast.LENGTH_SHORT).show();
                }else {
                    binding.layoutForSignUp.setVisibility(View.VISIBLE);
                    binding.layoutForProfile.setVisibility(View.GONE);
                }
            }
        };

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void gotoLogInActivity(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void goToSignUpActivity(View view) {
        startActivity(new Intent(this, SignupActivity.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    public void goToProfile(View view) {
        startActivity(new Intent(this, Profile.class));
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void logOUt(View view) {
        firebaseAuth.signOut();
        Toast.makeText(this, "signOUt", Toast.LENGTH_SHORT).show();
    }

    public void goToEvents(View view) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            startActivity(new Intent(this, Events.class));
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please Log in To View Your Events")
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}

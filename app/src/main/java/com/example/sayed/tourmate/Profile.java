package com.example.sayed.tourmate;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.sayed.tourmate.databinding.ActivityProfileBinding;

public class Profile extends AppCompatActivity {

 /*   private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
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

    };*/

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

   /*     BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigationP);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);*/
    }

    public void signOut(View view) {
        startActivity(new Intent(this, HomeActivity.class));
    }
}

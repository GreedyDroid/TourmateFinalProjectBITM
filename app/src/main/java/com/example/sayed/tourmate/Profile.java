package com.example.sayed.tourmate;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.sayed.tourmate.databinding.ActivityProfileBinding;
import com.example.sayed.tourmate.login_signup.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

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
    //For Binding
    private ActivityProfileBinding binding;
    //For Database
    private DatabaseReference databaseReference;
    //For Database auth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        //FireBase Authentication Check User Is logged IN>>>
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        MyAsyncTask downloadUserInfo = new MyAsyncTask();
        downloadUserInfo.execute();


        /*if (user != null){
            binding.emailAddressP.setText(
                    TextUtils.isEmpty(user.getEmail()) ? "No email" : user.getEmail());
            binding.userProfileName.setText(
                    TextUtils.isEmpty(user.getDisplayName()) ? "No Name Found" : user.getDisplayName());
            if (user.getPhotoUrl() != null){
                    // Download photo and set to image
                    Picasso.with(this).load(user.getPhotoUrl()).into(
                            binding.userProfilePhoto
                    );
            }
            binding.userProfilePhoto.setImageBitmap(BitmapFactory.decodeFile(user.getPhotoUrl().toString()));
        }else {
            finish();
        }
        //For Database>>
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("User");*/

   /*     BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigationP);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);*/
    }

    // All Backgroudn Task Here>>>>>>>>>>>>>
    class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

        private boolean running;
        private boolean isDownloadProcessDone;
        private ProgressDialog progressDialog;
        private FirebaseUser user = firebaseAuth.getCurrentUser();
        private String userUnicID = user.getUid();
        private User userData = new User();


        private boolean downloadProfileData(){
            if (user != null){
                //Logged in using Social Media
                if (user.getDisplayName() != null){
                    //set image
                    //TODO download img into BITMAP>>>>>
                    //set name
                    userData.setUserName(
                            TextUtils.isEmpty(user.getDisplayName()) ? "No Name Found" : user.getDisplayName());
                    //set Email
                    userData.setUserEmail(
                            TextUtils.isEmpty(user.getEmail()) ? "No email" : user.getEmail());
                    //FlagUp
                    setData(userData);
                    isDownloadProcessDone = true;
                }else {
                    //Logged in using Email And Password
                    DatabaseReference curUserDataRef = databaseReference.child(userUnicID);
                    curUserDataRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userData = dataSnapshot.getValue(User.class);
                            setData(userData);
                            isDownloadProcessDone = true;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //TODO database error Load Old Data>>>>>
                            isDownloadProcessDone = true;
                            Toast.makeText(Profile.this, "No User Data Found!! Try Again", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }else {
                Toast.makeText(Profile.this, "User Data Not Found", Toast.LENGTH_SHORT).show();
                finish();
            }
            return true;
        }

        private void setData(User userData){
            binding.userProfileName.setText(
                    TextUtils.isEmpty(userData.getUserName()) ? "No Name" : userData.getUserName());
            binding.mobileNoP.setText(
                    TextUtils.isEmpty(userData.getUserPhoneNo()) ? "No Mobile No Found" : userData.getUserPhoneNo());
            binding.emailAddressP.setText(
                    TextUtils.isEmpty(userData.getUserEmail()) ? "No Mobile No Found" : userData.getUserEmail());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            isDownloadProcessDone = downloadProfileData();
            int i = 0;
            while(running){
                if (isDownloadProcessDone){
                    running = false;
                    break;
                }
                /*try {
                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                if(i++ == 100){
                    running = false;
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
            running = true;
            isDownloadProcessDone = false;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(user.getPhotoUrl() != null){
                Picasso.with(Profile.this).load(user.getPhotoUrl())
                        .transform(new CropCircleTransformation())
                        .into(binding.userProfilePhoto);
            }
        }

    }
}

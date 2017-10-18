package com.example.sayed.tourmate.login_signup;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sayed.tourmate.Profile;
import com.example.sayed.tourmate.R;
import com.example.sayed.tourmate.databinding.ActivitySignupBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private static final String TAG = "SignupActivity";
    private static final int FB_SIGN_IN = 100;
    private static final int Gl_SIGN_IN = 100;
    //For FireBase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private MyAsyncTaskSignUP myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

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
                   // startActivity(new Intent(SignupActivity.this, Profile.class));
                    finish();
                }
            }
        };

        // If no user is logged in>>>>>>>>>>
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
        binding.linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

    }

    private void signUp() {

        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        binding.btnSignup.setEnabled(false);


        final String name = binding.inputName.getText().toString();
        final String email = binding.inputEmail.getText().toString();
        final String mobile = binding.inputMobile.getText().toString();
        String password = binding.inputPassword.getText().toString();

        // TODO: Implement  signup logic here.
        myAsyncTask = new MyAsyncTaskSignUP();
        myAsyncTask.execute(email, password, name, mobile);
        /*try{

            registerWithEmailAndPass(email, password, name, mobile);
        }catch (Exception e){
            Toast.makeText(this, "Failed Try Again", Toast.LENGTH_LONG).show();
        }*/
    }
    


    //User ID>>>>>>>>>>
    private String getUserID(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user.getUid();
    }

    private void updateDatabase(String name, String email, String mobile){
        //ForUpdating Database
        if (getUserID()!= null){

                String userid = getUserID();
                User user = new User(name, email,mobile, userid);
                databaseReference.child(getUserID()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, "Failed to Update information", Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }

    // this code is written inside asynTask
 /*   private void registerWithEmailAndPass(final String email, String password, final String name, final String mobile){
        //Firebase Auth
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            updateDatabase(name, email, mobile);
                        }else {
                            Snackbar.make(binding.coordinatorLayout, "Sign Up Failed, Please Try Again", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
//                            Toast.makeText(SignupActivity.this, "Sign Up Failed, Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, "logIn Failed Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void onSignupSuccess() {
        binding.btnSignup.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    private void onSignupFailed() {
        binding.btnSignup.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String name = binding.inputName.getText().toString();
        String email = binding.inputEmail.getText().toString();
        String mobile = binding.inputMobile.getText().toString();
        String password = binding.inputPassword.getText().toString();
        String reEnterPassword = binding.inputReEnterPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            binding.inputName.setError("at least 3 characters");
            valid = false;
        } else {
            binding.inputName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            binding.inputEmail.setError(null);
        }

        if (mobile.isEmpty() || (mobile.length()<6&& mobile.length()>15)) {
            binding.inputMobile.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            binding.inputMobile.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            binding.inputPassword.setError("between 6 and 20 alphanumeric characters");
            valid = false;
        } else {
            binding.inputPassword.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || reEnterPassword.length() > 20 || !(reEnterPassword.equals(password))) {
            binding.inputReEnterPassword.setError("Password Do not match");
            valid = false;
        } else {
            binding.inputReEnterPassword.setError(null);
        }

        return valid;
    }

    public void signUpWithSocialMedia(View view) {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme_Dark)
                        .setLogo(R.drawable.logo)
                        .setProviders(AuthUI.GOOGLE_PROVIDER
                        ,AuthUI.FACEBOOK_PROVIDER)
                        .build(),Gl_SIGN_IN);
    }

    //Progress>>>>>>>>>>>>>>>>signUp

    class MyAsyncTaskSignUP extends AsyncTask<String, Integer, Void> {

        boolean runningRegister;
        ProgressDialog progressDialog;
        boolean isSignUpProcessed, logInSuccess;

        //Register With Email And Pass
        private boolean signUpUsingFireBase(final String email, String password, final String name, final String mobile){
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                updateDatabase(name, email, mobile);
                            }else {
                                Snackbar.make(binding.coordinatorLayout, "Sign Up Failed, Please Try Again", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    });

            return true;
        }
        //Update Datebase>>>>>>>>>>>>
        private boolean updateDatabase(String name, String email, String mobile){
            //ForUpdating Database
            if (getUserID()!= null){

                String userid = getUserID();
                User user = new User(name, email,mobile, userid);
                databaseReference.child(getUserID()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                        logInSuccess = true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, "Failed to Update information", Toast.LENGTH_SHORT).show();
                        logInSuccess = true;
                    }
                });
            }
            return true;
        }


        @Override
        protected Void doInBackground(String... params) {
            isSignUpProcessed = signUpUsingFireBase(params[0], params[1], params[2], params[3]);
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

            progressDialog = new ProgressDialog(SignupActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();

            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    runningRegister = false;
                    binding.btnSignup.setEnabled(true);
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            binding.btnSignup.setEnabled(true);
        }

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
}

package com.example.sayed.tourmate.login_signup;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sayed.tourmate.HomeActivity;
import com.example.sayed.tourmate.Profile;
import com.example.sayed.tourmate.R;
import com.example.sayed.tourmate.databinding.ActivityLogInBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private ActivityLogInBinding binding;
    private MyAsyncTask myAsyncTask;
    public boolean isLoggedIn = false;
    private static final int FB_SIGN_IN = 100;
    private static final int Gl_SIGN_IN = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in);

        firebaseAuth = FirebaseAuth.getInstance();
        
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        binding.linkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        //UserLogged In Listener
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    // TODO:  GO to Profile Activity
                    finish();
//                    startActivity(new Intent(LoginActivity.this, Profile.class));
                }
            }
        };
    }

    private void login() {

        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        binding.btnLogin.setEnabled(false);

       /* final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging In...");
        progressDialog.show();*/
    //TODO: set progressbar for newer version>>>>>>>>>>>>>>
        //final ProgressBar progressBar = new ProgressBar(LoginActivity.this,R.style.AppTheme_Dark_Dialog);
        final String email = binding.inputEmail.getText().toString();
        final String password = binding.inputPassword.getText().toString();

        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(email, password);
//        logInUsingFIreBase(email, password);

//        new ProgressBarInBackground(this).execute(email, password);
        // TODO: Implement your own authentication logic here.

       /* new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 2000);*/
    }
    
    /*private void logInUsingFIreBase(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }else {
                            showSnackbar(R.string.log_in_failed);
                            Toast.makeText(LoginActivity.this, "ShowSnackBar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        binding.btnLogin.setEnabled(true);
        finish();
    }

    private void onLoginFailed() {
        binding.btnLogin.setEnabled(true);
    }

    private boolean validate()  {
        boolean valid = true;

        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            binding.inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            binding.inputPassword.setError("between 6 and 20 alphanumeric characters");
            valid = false;
        } else {
            binding.inputPassword.setError(null);
        }

        return valid;
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

    public void signOut(View view) {
        firebaseAuth.signOut();
        Toast.makeText(this, "Signd Out", Toast.LENGTH_SHORT).show();
    }

    public void singInWithFacebook(View view) {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                .setTheme(R.style.AppTheme_Dark)
                .setLogo(R.drawable.logo)
                .setProviders(AuthUI.FACEBOOK_PROVIDER)
                .build(),FB_SIGN_IN);
    }

    public void signInWithGoogle(View view) {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme_Dark)
                        .setLogo(R.drawable.logo)
                        .setProviders(AuthUI.GOOGLE_PROVIDER)
                        .build(),Gl_SIGN_IN);
    }


    //Progress

class MyAsyncTask extends AsyncTask<String, Integer, Void> {

    boolean running;
    ProgressDialog progressDialog;
    boolean logInSuccess, logInProcessed;


    private boolean logInUsingFIreBase(String email, String password){
        try{
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Success Called from On click Listener", Toast.LENGTH_SHORT).show();
                                finish();
                                logInSuccess = true;
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "         LogIn Failed \nWrond Email Or Password", Toast.LENGTH_SHORT).show();
                    binding.inputPassword.setText("");
                    logInSuccess = true;
                }
            });
        }catch (Exception e){
            Toast.makeText(LoginActivity.this, ""+e, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

        @Override
        protected Void doInBackground(String... params) {
            logInProcessed = logInUsingFIreBase(params[0], params[1]);
            int i = 0;
            if(logInSuccess){
                running = false;
            }
            while(running){
                try {
                    Thread.sleep(1000);
                    // Beacuse Listener takes time to make logInSuccess True..
                    if (logInSuccess){
                        break;
                    }
                    if (logInProcessed){
                        running = false;
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
            logInSuccess= false;

            progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Logging In...");
            progressDialog.show();

            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    running = false;
                    binding.btnLogin.setEnabled(true);
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            binding.btnLogin.setEnabled(true);
        }

    }




/*
    private class ProgressBarInBackground extends AsyncTask<String, Void, Void> {
        private ProgressDialog asyncDialog;

        public ProgressBarInBackground(Context context) {
            asyncDialog = new ProgressDialog(context, R.style.AppTheme_Dark);
        }

        private void logInUsingFIreBase(String email, String password){
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                onLoginSuccess();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }else {
                                showSnackbar(R.string.log_in_failed);
                                Toast.makeText(LoginActivity.this, "ShowSnackBar", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(LoginActivity.this, "alukolalat", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... params) {
            //don't touch dialog here it'll break the application
            //do some lengthy stuff like calling login webservice

            logInUsingFIreBase(params[0], params[1]);
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            //hide the dialog
            asyncDialog.dismiss();
            Toast.makeText(LoginActivity.this, "Loggin Successful", Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }
    }*/
}

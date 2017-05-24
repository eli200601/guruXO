package com.app.elisoft.guru;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.app.elisoft.guru.Activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            Log.d(TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());
            //To Delete!!!
            Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
            this.startActivity(myIntent);
            this.finish();
            } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
            Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
            this.startActivity(myIntent);
            this.finish();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}

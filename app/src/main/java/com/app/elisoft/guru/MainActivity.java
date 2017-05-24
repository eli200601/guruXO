package com.app.elisoft.guru;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.app.elisoft.guru.Activity.LoginActivity;
import com.app.elisoft.guru.BroadcastReceiver.AlarmReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private Intent alarmIntent;
    private PendingIntent pendingIntent;
    private AlarmManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1001, alarmIntent, 0);
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            Log.d(TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());
            startUpdateUserStatus();

            } else {
            // User is signed out from FireBase
            Log.d(TAG, "onAuthStateChanged:signed_out");
            launchLoginActivity();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            launchLoginActivity();
        }
        else {
            startUpdateUserStatus();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopUpdateUserStatus();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void launchLoginActivity(){
        stopUpdateUserStatus();
        Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
        this.startActivity(myIntent);
        this.finish();
    }

    public void startUpdateUserStatus() {
        int interval = 1000*60;
        Log.d(TAG, "startUpdateUserStatus() - Starting To update user status");
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval , pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void stopUpdateUserStatus() {
        boolean isWorking = (PendingIntent.getBroadcast(this, 1001, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null);
        if (isWorking) {
            Log.d(TAG, "Stopping User Status");
            manager.cancel(pendingIntent);
        }
    }

    private void updateUserLastLogin(FirebaseUser user) {
        mDatabase.child("users").child(user.getUid()).child("lastLogin").setValue(System.currentTimeMillis());
//        mDatabase.child("users").child(uid).child("lastLogin").setValue(1);

    }


}

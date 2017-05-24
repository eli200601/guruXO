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

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        start();


    }

    public void start() {
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 5000;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000*60 , pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            Log.d(TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());

            //ToDO: Delete!!!
//            Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
//            this.startActivity(myIntent);
//            this.finish();
            } else {
            // User is signed out from FireBase
            Log.d(TAG, "onAuthStateChanged:signed_out");
            launchLoginActivity();
        }

    }

    private void launchLoginActivity(){
        Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
        this.startActivity(myIntent);
        this.finish();
    }
    private void updateUserLastLogin(FirebaseUser user) {
        mDatabase.child("users").child(user.getUid()).child("lastLogin").setValue(System.currentTimeMillis());
//        mDatabase.child("users").child(uid).child("lastLogin").setValue(1);

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
            this.startActivity(myIntent);
            this.finish();
        }
        else {
            Log.d(TAG, "Updating the user last seen time");
            updateUserLastLogin(currentUser);
        }
    }



    @Override
    public void onStop() {
        super.onStop();
    }
}

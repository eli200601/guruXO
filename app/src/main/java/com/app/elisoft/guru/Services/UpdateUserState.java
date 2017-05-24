package com.app.elisoft.guru.Services;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateUserState extends IntentService {

    private static final String TAG = "UpdateUserState";

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;

    public UpdateUserState(){
        super("");
    }

    public UpdateUserState(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Starting To Update last login time");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Log.d(TAG, "User is not null");
            mDatabase.child("users").child(currentUser.getUid()).child("lastLogin").setValue(System.currentTimeMillis());
        } else {
            Log.d(TAG,"User is null");
        }

        Log.d(TAG, "Stopping UpdateUserState");
        this.stopSelf();
    }
}

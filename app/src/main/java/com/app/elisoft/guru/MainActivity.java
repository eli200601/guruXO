package com.app.elisoft.guru;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.elisoft.guru.Activity.BaseActivity;
import com.app.elisoft.guru.Activity.LoginActivity;
import com.app.elisoft.guru.BroadcastReceiver.AlarmReceiver;
import com.app.elisoft.guru.Recycler.RecyclerAdapter;
import com.app.elisoft.guru.Table.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;


    private Intent alarmIntent;
    private PendingIntent pendingIntent;
    private AlarmManager manager;

    private RecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private ArrayList<User> usersList;

    private TextView userNameTitle;
    private ImageView logoutButton;
    private ImageView refresh_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Init The Update user login Intent
        alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1001, alarmIntent, 0);
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        usersList = new ArrayList<>();
        showProgressDialog();


    }

//    private void collectUserList(ArrayList<User> users) {
//
//        ArrayList<Long> phoneNumbers = new ArrayList<>();
//
//        //iterate through each user, ignoring their UID
//        for (Map.Entry<String, Object> entry : users.entrySet()){
//
//            //Get user map
//            Map singleUser = (Map) entry.getValue();
//            //Get phone field and append to list
//            phoneNumbers.add((Long) singleUser.get("phone"));
//        }
//
//        System.out.println(phoneNumbers.toString());
//    }

    private View.OnClickListener listenerLogout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            launchLoginActivity();
        }
    };

    private View.OnClickListener listenerRefresh = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getApplicationContext(), "Refreshing", Toast.LENGTH_SHORT).show();
            mDatabase.child("users").addListenerForSingleValueEvent(valueEventListener);
        }
    };

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

//                if (usersList.size() < snapshot.getChildrenCount()) {
            hideProgressDialog();
            usersList.clear();

            Log.d(TAG ,""+dataSnapshot.getChildrenCount());
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                User post = postSnapshot.getValue(User.class);

                Log.d(TAG, post.getEmail());
                if (!currentUser.getUid().equals(post.getUid())) {
                    usersList.add(post);
                }

            }
            sortApplicationList();
            Log.d(TAG, "User list size is: "+ String.valueOf(usersList.size()));
            mAdapter.setItems(usersList);
            mAdapter.notifyDataSetChanged();
//                }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            Log.d(TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());
            startUpdateUserStatus();

            } else {
            // User is signed out from FireBase
            Log.d(TAG, "onAuthStateChanged:signed_out");
            launchLoginActivity();
        }
        //Setting up the user name title
        userNameTitle = (TextView) findViewById(R.id.user_name_title);
        String name = "hi, " + currentUser.getEmail().split("@")[0];
        userNameTitle.setText(name);

        //Setting up the logout button
        logoutButton = (ImageView) findViewById(R.id.logout_button);
        refresh_button = (ImageView) findViewById(R.id.refresh_button);

        refresh_button.setOnClickListener(listenerRefresh);
        logoutButton.setOnClickListener(listenerLogout);

        //Init the Recycler ToDo: hare!!!k
        mAdapter = new RecyclerAdapter(getApplicationContext(), usersList);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mDatabase.child("users").addValueEventListener(valueEventListener);
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
            mDatabase.child("users").addListenerForSingleValueEvent(valueEventListener);
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
//        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void stopUpdateUserStatus() {
        boolean isWorking = (PendingIntent.getBroadcast(this, 1001, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null);
        if (isWorking) {
            Log.d(TAG, "Stopping User Status");
            manager.cancel(pendingIntent);
        }
    }

    public void sortApplicationList() {
        Collections.sort(usersList, lastLoginDesComparator);
    }

    private Comparator<User> lastLoginDesComparator = new Comparator<User>() {
        @Override
        public int compare(User user1, User user2) {
            long long1 = user1.getLastLogin();
            long long2 = user2.getLastLogin();
            if (long1 < long2) {
                return 1;
            } else {
                return -1;
            }
        }
    };

    private void updateUserLastLogin(FirebaseUser user) {
        mDatabase.child("users").child(user.getUid()).child("lastLogin").setValue(System.currentTimeMillis());
//        mDatabase.child("users").child(uid).child("lastLogin").setValue(1);

    }


}

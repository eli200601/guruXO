package com.app.elisoft.guru;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.elisoft.guru.Activity.BaseActivity;
import com.app.elisoft.guru.Activity.LeaderBoardActivity;
import com.app.elisoft.guru.Activity.LoginActivity;
import com.app.elisoft.guru.BroadcastReceiver.AlarmReceiver;
import com.app.elisoft.guru.Dialogs.GotInviteDialog;
import com.app.elisoft.guru.Dialogs.InviteDialog;
import com.app.elisoft.guru.EventBus.MessageEvent;
import com.app.elisoft.guru.Recycler.RecyclerAdapter;
import com.app.elisoft.guru.Table.User;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private User currentUserLocal;

    private EventBus eventBus = EventBus.getDefault();

    private Intent alarmIntent;
    private PendingIntent pendingIntent;
    private AlarmManager manager;

    private RecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private ArrayList<User> usersList;

    private TextView userNameTitle;
    private ImageView logoutButton;
    private ImageView refresh_button;
    private ImageView cup_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fabric.with(this, new Crashlytics());

        mAuth = FirebaseAuth.getInstance();



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("users").addValueEventListener(valueEventListener);
                }
            }
        };


        // Init The Update user login Intent
        alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1001, alarmIntent, 0);
        loadPreference();
        usersList = new ArrayList<>();
        showProgressDialog();

        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is signed in
            Log.d(TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());
            startUpdateUserStatus();
        } else {
            // User is signed out from FireBase
            Log.d(TAG, "onAuthStateChanged:signed_out");
            launchLoginActivity();
            return;
        }



    }


    @Override
    protected void onStart() {
        super.onStart();


        //Init the Recycler ToDo: hare!!!k
        mAdapter = new RecyclerAdapter(getApplicationContext(), usersList, getUserFromDB(currentUser));
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);


        //Setting up the user name title
        userNameTitle = (TextView) findViewById(R.id.user_name_title);
        String name = "hi, " + currentUser.getEmail().split("@")[0];
        userNameTitle.setText(name);

        //Setting up the logout button
        logoutButton = (ImageView) findViewById(R.id.logout_button);
        refresh_button = (ImageView) findViewById(R.id.refresh_button);
        cup_button = (ImageView) findViewById(R.id.cup_button);

        refresh_button.setOnClickListener(listenerRefresh);
        logoutButton.setOnClickListener(listenerLogout);
        cup_button.setOnClickListener(listenerCupButton);

        mAuth.addAuthStateListener(mAuthListener);
    }

    private void loadPreference() {
        String language = "en";
        Locale myLocale = new Locale(language);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.setLocale(myLocale);
        //manually set layout direction to a LTR location
        config.setLayoutDirection(new Locale("en"));
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        String locale = getResources().getConfiguration().locale.getDisplayName();

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

    public static ArrayList<User> cloneList(ArrayList<User> list) {
        ArrayList<User> clone = new ArrayList<User>(list.size());
        for (User item : list) clone.add(new User(item));
        return clone;
    }

    private View.OnClickListener listenerCupButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, currentUserLocal.getMyWins() + " " +  currentUserLocal.getMyLoses());

            ArrayList<User> allList = new ArrayList<>();
            allList.clear();
            allList = cloneList(usersList);

            Log.d(TAG, "allList - " + allList.size());
            Log.d(TAG, "usersList - " + usersList.size());

            allList.add(currentUserLocal);
            Log.d(TAG, "allList + userList - " + allList.size());
            ArrayList<User> topThree = getTopThree(allList);



            Intent myIntent = new Intent(getApplicationContext(), LeaderBoardActivity.class);
            myIntent.putExtra("top_three_list", topThree);

            startActivity(myIntent);
        }
    };

    public ArrayList<User> getTopThree(ArrayList<User> allList){
        int count = 0;
        User one,two,three;
        one = new User();
        two = new User();
        three = new User();

        ArrayList<User> orederList = new ArrayList<>();
        for(User user: allList) {
            Log.d(TAG, "user " + user.getEmail() + " Wins " + user.getMyWins());
            int winsUser = Integer.valueOf(user.getMyWins());
            int winsOne = Integer.valueOf(one.getMyWins());
            int winsTwo = Integer.valueOf(two.getMyWins());
            int winsThree = Integer.valueOf(three.getMyWins());

            if (winsUser > winsOne) {
                User temp2, temp3;

                temp2 = one;
                temp3 = two;

                one = user;
                two = temp2;
                three = temp3;

                Log.d(TAG, "user " + user.getEmail() + " in One");
            } else {
                if (winsUser > winsTwo) {
                    three = two;
                    two = user;
                    Log.d(TAG, "user " + user.getEmail() + " in two");
                } else {
                    if (winsUser > winsThree) {
                        three = user;
                        Log.d(TAG, "user " + user.getEmail() + " in three");
                    }
                }
            }

        }
        orederList.add(one);
        orederList.add(two);
        orederList.add(three);
        orederList.add(currentUserLocal);

        for (User user: orederList){
            Log.d(TAG, "Top 3: " + user.getEmail());
        }

        return orederList;


    }

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
            hideProgressDialog();
            usersList.clear();
            Log.d(TAG ,"" + dataSnapshot.getChildrenCount());
            if (currentUser!= null ) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    User post = postSnapshot.getValue(User.class);

                    Log.d(TAG, post.getEmail());
                    if (!currentUser.getUid().equals(post.getUid())) {
                        usersList.add(post);
                    } else {
                        currentUserLocal = post;
                    }
                }
                sortApplicationList();
                Log.d(TAG, "User list size is: "+ String.valueOf(usersList.size()));
                mAdapter.setItems(usersList);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    private User getUserFromDB(FirebaseUser firebaseUser) {
        User user = new User(currentUser.getUid(),currentUser.getEmail(), "", 0);
        return user;
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
//            mDatabase.child("users").addListenerForSingleValueEvent(valueEventListener);
        }
        String username = currentUser.getUid();
        FirebaseMessaging.getInstance().subscribeToTopic("user_" + username);
        eventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            stopUpdateUserStatus();
        }

        String username = currentUser.getUid();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + username);
        Log.d(TAG, "Unregister ");
        eventBus.unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void launchLoginActivity(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            stopUpdateUserStatus();
        }
        Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
        this.startActivity(myIntent);
        this.finish();
    }

    public void startUpdateUserStatus() {
        int interval = 1000*60;
        Log.d(TAG, "startUpdateUserStatus() - Starting To update user status");
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
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

    public User getUserFromUID(String uid) {
        for (User user: usersList) {
            if (uid.equals(user.getUid())) {
                return user;
            }
        }
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent instanceof MessageEvent.OnInviteToPlay) {
            String hostName = ((MessageEvent.OnInviteToPlay) messageEvent).getHostName();
            String host_id = ((MessageEvent.OnInviteToPlay) messageEvent).getHostId();
            String game_room = ((MessageEvent.OnInviteToPlay) messageEvent).getGameRoom();

            Log.d(TAG, "I am invited by :) " + hostName);

            User hostUser = getUserFromUID(host_id);
            Log.d(TAG, "From Users class, host is: " + hostUser.getEmail());
            Log.d(TAG, "Game room = " + game_room);

            Bundle bundle = new Bundle();

            bundle.putSerializable("UserClient", currentUserLocal);
            bundle.putSerializable("UserHost", hostUser);
            bundle.putString("game_room", game_room);

            Intent gotInviteActivity = new Intent(this, GotInviteDialog.class);

            gotInviteActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            gotInviteActivity.putExtra("bundleGotInvite", bundle);
            startActivity(gotInviteActivity);

//            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//            alertDialog.setTitle("Invited!");
//            alertDialog.setMessage("You been invited by " + hostName);
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            alertDialog.show();
        }
        else {
            if (messageEvent instanceof MessageEvent.OnUserClickInLobby) {
                int position = ((MessageEvent.OnUserClickInLobby) messageEvent).getPosition();
                //                When Clicking on item in list
                Log.d(TAG, "User clicked on " + position);

                Bundle bundle = new Bundle();

                bundle.putSerializable("UserClient", usersList.get(position));
                bundle.putSerializable("UserHost", currentUserLocal);

                Intent dialogActivity = new Intent(this, InviteDialog.class);
                dialogActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                View view_item = mRecyclerView.getLayoutManager().findViewByPosition(position);


                Pair<View, String> iconPair = Pair.create(view_item.findViewById(R.id.user_profile_icon), "user_icon");

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, iconPair);

                dialogActivity.putExtra("bundleUser", bundle);
                startActivity(dialogActivity, options.toBundle());


            }
        }
    }
}

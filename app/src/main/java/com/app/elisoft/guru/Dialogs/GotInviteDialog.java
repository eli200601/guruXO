package com.app.elisoft.guru.Dialogs;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.app.elisoft.guru.R;
import com.app.elisoft.guru.Table.GameRoom;
import com.app.elisoft.guru.Table.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class GotInviteDialog extends AppCompatActivity {

    static final String TAG = "GotInviteDialog";
    User host_user, client_user;
    String game_room;

    private DatabaseReference mDatabase;

    Button accept, decline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_got_invite);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundleGotInvite");
        client_user = (User) bundle.getSerializable("UserClient");
        host_user = (User) bundle.getSerializable("UserHost");
        game_room = bundle.getString("game_room");

        Log.d(TAG, "Host!!! " + host_user.getEmail());
        Log.d(TAG, "client_user!!! " + client_user.getEmail());
        Log.d(TAG, "Game room = " + game_room);
        //need to add listeners to buttons
        // need to open a game room if ok

        mDatabase = FirebaseDatabase.getInstance().getReference();

        accept = (Button) findViewById(R.id.accept_invite_dialog);
        decline = (Button) findViewById(R.id.cancel_invite_dialog);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGameRoom();


            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDialog();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseMessaging.getInstance().subscribeToTopic("room_" + game_room);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("room_" + game_room);
    }

    //    private ValueEventListener valueEventListener = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//
////                if (usersList.size() < snapshot.getChildrenCount()) {
//            hideProgressDialog();
//            usersList.clear();
//
//            Log.d(TAG ,"" + dataSnapshot.getChildrenCount());
//            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//
//                User post = postSnapshot.getValue(User.class);
//
//                Log.d(TAG, post.getEmail());
//                if (!currentUser.getUid().equals(post.getUid())) {
//                    usersList.add(post);
//                }
//
//            }
//            sortApplicationList();
//            Log.d(TAG, "User list size is: "+ String.valueOf(usersList.size()));
//            mAdapter.setItems(usersList);
//            mAdapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    };

    private void openGameRoom() {
        String room_name = game_room;
        GameRoom game = new GameRoom(room_name, host_user, client_user);

        mDatabase.child("game_rooms").child(game_room).setValue(game);
    }

    private void closeDialog(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

}

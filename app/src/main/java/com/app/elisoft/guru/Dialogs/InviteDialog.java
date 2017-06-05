package com.app.elisoft.guru.Dialogs;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.elisoft.guru.Activity.GameActivity;
import com.app.elisoft.guru.EventBus.MessageEvent;
import com.app.elisoft.guru.R;
import com.app.elisoft.guru.Services.SendMessageToDevice;
import com.app.elisoft.guru.Table.GameRoom;
import com.app.elisoft.guru.Table.User;
import com.app.elisoft.guru.Utils.Keys;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.UUID;

public class InviteDialog extends AppCompatActivity {

    static final String TAG = "InviteDialog";
    private FirebaseAuth auth;
    private String FCM_API_KEY = "AAAAK_9HT5E:APA91bHdVCtHBj8p6ovE8kGQmTWZdrLSE6b9WGH58Cio-GXI7umvkfGe8B4nG4v3At2a486iV_vUh65pOMMIr87K876QyTWKZZ2ZodZCpHBqqAvDTyO2Ux-oDJtvjA2lxNipfP87_6Mr";
    User host_user, client_user, current_user;
    String game_room;

    private DatabaseReference mDatabase;

    private EventBus eventBus = EventBus.getDefault();


    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_invite);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundleUser");
        client_user = (User) bundle.getSerializable("UserClient");
        host_user = (User) bundle.getSerializable("UserHost");
        current_user = host_user;

        Log.d(TAG, "Host!!! " + host_user.getEmail());
        Log.d(TAG, "client_user!!! " + client_user.getEmail());

        // Generating optional game room
        game_room = UUID.randomUUID().toString();
        Log.d(TAG, "uuid: " + game_room);

        title = (TextView) findViewById(R.id.title_invite);

        Intent intentNew = new Intent(InviteDialog.this, SendMessageToDevice.class);
        intentNew.putExtra("host_name", host_user.getEmail().split("@")[0]);
        intentNew.putExtra("host_uid", host_user.getUid());
        intentNew.putExtra("address_prefix", "user_");
        intentNew.putExtra("client_uid", client_user.getUid());
        intentNew.putExtra("game_room", game_room);
        intentNew.putExtra("request_type", Keys.REQUEST_TYPE_INVITE);



        intentNew.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startService(intentNew);

//        sendNotification("hey man", host_user.getUid(), client_user.getUid());

        TextView clientName = (TextView) findViewById(R.id.other_player_name);
        Button cancel = (Button) findViewById(R.id.cancel_invite_dialog);

        clientName.setText(client_user.getEmail());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDialog();
            }
        });

    }

    private void closeDialog(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseMessaging.getInstance().subscribeToTopic("room_" + game_room);
        eventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("room_" + game_room);
        eventBus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent instanceof MessageEvent.OnAcceptInvite) {
            String response = ((MessageEvent.OnAcceptInvite) messageEvent).getMessage();
            Log.d(TAG, "GotInviteDialog catch OnAcceptInvite - " + response);
            if (response.equals(Keys.RESPONSE_AGREE)) {
                // need to open new screen
                title.setText("Challenge accepted :)");
                startGame();
            }else {
                //            removeGameRoom();
                title.setText("He is chicken - decline");
                //Need to delete game room
                //Need to prompt the user about decline!
            }

        }
    }

    public void startGame(){
        Bundle bundle = new Bundle();

        bundle.putSerializable("UserClient", client_user);
        bundle.putSerializable("UserHost", host_user);
        bundle.putString("game_room", game_room);
        bundle.putSerializable("profile", current_user);

        Intent startGame = new Intent(this, GameActivity.class);

        startGame.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startGame.putExtra("bundleStartGame", bundle);
        startActivity(startGame);
        closeDialog();
    }

    private void removeGameRoom() {
        String room_name = game_room;
        GameRoom game = new GameRoom(room_name, host_user, client_user);

        mDatabase.child("game_rooms").child(game_room).removeValue();
    }
}

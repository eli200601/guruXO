package com.app.elisoft.guru.Dialogs;


import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.elisoft.guru.Activity.GameActivity;
import com.app.elisoft.guru.EventBus.MessageEvent;
import com.app.elisoft.guru.R;
import com.app.elisoft.guru.Services.SendMessageToDevice;
import com.app.elisoft.guru.Table.GameRoom;
import com.app.elisoft.guru.Table.User;
import com.app.elisoft.guru.Utils.Keys;
import com.app.elisoft.guru.Views.CircleTransform;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class GotInviteDialog extends AppCompatActivity {

    static final String TAG = "GotInviteDialog";
    User host_user, client_user, current_user;
    String game_room;

    private DatabaseReference mDatabase;

    private EventBus eventBus = EventBus.getDefault();

    TextView host_name, title;
    ImageView host_icon;
    Button accept, decline;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_got_invite);

        supportPostponeEnterTransition();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundleGotInvite");
        client_user = (User) bundle.getSerializable("UserClient");
        host_user = (User) bundle.getSerializable("UserHost");
        game_room = bundle.getString("game_room");
        current_user = client_user ;

        Log.d(TAG, "Host!!! " + host_user.getEmail());
        Log.d(TAG, "client_user!!! " + client_user.getEmail());
        Log.d(TAG, "Game room = " + game_room);
        //need to add listeners to buttons
        // need to open a game room if ok

        mDatabase = FirebaseDatabase.getInstance().getReference();

        accept = (Button) findViewById(R.id.accept_invite_dialog);
        decline = (Button) findViewById(R.id.cancel_invite_dialog);

        title = (TextView)  findViewById(R.id.got_invite_title);
        host_name = (TextView) findViewById(R.id.got_invite_client_name);
        host_icon = (ImageView) findViewById(R.id.got_invite_host_icon);

        String host_user_icon_url = host_user.getIconURL();
        if (host_user_icon_url == null) host_user_icon_url = "a";
        Picasso.with(this)
                .load(host_user_icon_url)
                .placeholder(R.mipmap.profile_icon)
                .error(R.mipmap.profile_icon)
                .transform(new CircleTransform())
                .into(host_icon);

        host_name.setText(host_user.getEmail().split("@")[0]);

        host_name.setVisibility(View.INVISIBLE);
        host_icon.setVisibility(View.INVISIBLE);
        title.setVisibility(View.INVISIBLE);


        host_icon.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.ZoomInDown)
                .duration(1000)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        host_name.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.Tada)
                                .duration(1000)
                                .withListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        title.setVisibility(View.VISIBLE);
                                        YoYo.with(Techniques.FlipInX)
                                                .duration(600)
                                                .playOn(title);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                })
                                .playOn(host_name);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).playOn(host_icon);


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Accept the challenge
                openGameRoom();

                Intent intentNew = new Intent(GotInviteDialog.this, SendMessageToDevice.class);
                intentNew.putExtra("host_name", host_user.getEmail().split("@")[0]);
                intentNew.putExtra("host_uid", host_user.getUid());
                intentNew.putExtra("address_prefix", "room_");
                intentNew.putExtra("client_uid", game_room);
                intentNew.putExtra("game_room", game_room);
                intentNew.putExtra("request_type", Keys.REQUEST_TYPE_RESPONSE_TO_INVITE);
                intentNew.putExtra("message", Keys.RESPONSE_AGREE);

                intentNew.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startService(intentNew);

                startGame();
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Decline the challenge
                Intent intentNew = new Intent(GotInviteDialog.this, SendMessageToDevice.class);
                intentNew.putExtra("host_name", host_user.getEmail().split("@")[0]);
                intentNew.putExtra("host_uid", host_user.getUid());
                intentNew.putExtra("address_prefix", "room_");
                intentNew.putExtra("client_uid", game_room);
                intentNew.putExtra("game_room", game_room);
                intentNew.putExtra("request_type", Keys.REQUEST_TYPE_RESPONSE_TO_INVITE);
                intentNew.putExtra("message", Keys.RESPONSE_DECLINE);

                intentNew.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startService(intentNew);
                closeDialog();
            }
        });

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
            Log.d(TAG, "GotInviteDialog catch OnAcceptInvite");
        }
    }


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

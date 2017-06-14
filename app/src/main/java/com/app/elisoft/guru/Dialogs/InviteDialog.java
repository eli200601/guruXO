package com.app.elisoft.guru.Dialogs;


import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lusfold.spinnerloading.SpinnerLoading;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

    private ImageView client_user_icon, host_user_icon, vs_icon;
    private TextView client_user_text, host_user_text;
    private Button cancel_button;
    private RelativeLayout spinner_container;
    private SpinnerLoading spinner;



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

        supportPostponeEnterTransition();


        client_user_icon = (ImageView) findViewById(R.id.invite_client_icon);
        host_user_icon = (ImageView) findViewById(R.id.invite_host_icon);
        vs_icon = (ImageView) findViewById(R.id.invite_vs_icon);

        client_user_text = (TextView) findViewById(R.id.invite_client_name);
        host_user_text = (TextView) findViewById(R.id.invite_host_name);

        cancel_button = (Button) findViewById(R.id.cancel_invite_dialog);

        spinner_container = (RelativeLayout) findViewById(R.id.spinner_container);

        spinner = (SpinnerLoading) findViewById(R.id.spinner);
        spinner.setPaintMode(1);
        spinner.setCircleRadius(20);
//        spinner.setItemCount(0);

//        client_user_text.setVisibility(View.INVISIBLE);
        host_user_icon.setVisibility(View.INVISIBLE);
        host_user_text.setVisibility(View.INVISIBLE);
        vs_icon.setVisibility(View.INVISIBLE);
        spinner_container.setVisibility(View.INVISIBLE);

        String client_user_icon_url = client_user.getIconURL();
        if (client_user_icon_url == null) client_user_icon_url = "a";
        // Load Image of client user
        Picasso.with(this)
                .load(client_user_icon_url)
                .placeholder(R.mipmap.profile_icon)
                .error(R.mipmap.profile_icon)
                .transform(new CircleTransform())
                .into(client_user_icon, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                    supportStartPostponedEnterTransition();
                    }

                });
        //Load image of host
        String host_user_icon_url = host_user.getIconURL();
        if (host_user_icon_url == null) host_user_icon_url = "a";
        Picasso.with(this)
                .load(host_user_icon_url)
                .placeholder(R.mipmap.profile_icon)
                .error(R.mipmap.profile_icon)
                .transform(new CircleTransform())
                .into(host_user_icon);

        client_user_text.setText(client_user.getEmail().split("@")[0]);
        host_user_text.setText(host_user.getEmail().split("@")[0]);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
//                        client_user_text.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        Log.d(TAG," client user name onAnimationEnd");
//                        client_user_text.setVisibility(View.VISIBLE);
                        vs_icon.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.BounceIn)
                                .duration(2000)
                                .withListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        Log.d(TAG," VS anim finished onAnimationEnd");
                                        host_user_icon.setVisibility(View.VISIBLE);
                                        YoYo.with(Techniques.BounceInRight)
                                                .duration(1000)
                                                .playOn(host_user_icon);

                                        host_user_text.setVisibility(View.VISIBLE);
                                        YoYo.with(Techniques.BounceInUp)
                                                .duration(1300)
                                                .withListener(new Animator.AnimatorListener() {
                                                    @Override
                                                    public void onAnimationStart(Animator animator) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animator animator) {
                                                        Log.d(TAG," Host anim finished onAnimationEnd");
                                                        spinner_container.setVisibility(View.VISIBLE);
                                                        YoYo.with(Techniques.FadeIn)
                                                                .duration(700)
                                                                .playOn(spinner_container);

                                                    }

                                                    @Override
                                                    public void onAnimationCancel(Animator animator) {

                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animator animator) {

                                                    }
                                                })
                                                .playOn(host_user_text);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                })
                                .playOn(vs_icon);

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }

                })
                .playOn(client_user_text);


        cancel_button.setOnClickListener(cancelClick);

        sendRequestMessage();

//        setEnterSharedElementCallback(new SharedElementCallback() {
//            @Override
//            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
//                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
//                Log.d(TAG, "onSharedElementEnd");
//                YoYo.with(Techniques.BounceInRight)
//                        .duration(1000)
//                        .repeat(1)
//                        .withListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animator) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animator) {
//                        Log.d(TAG, "Item onAnimationEnd");
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animator) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animator) {
//
//                    }
//                })
//                        .playOn(client_user_icon);
//            }
//        });
    }

    public void loadClientIcon(){

    }


    private View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            closeDialog();
        }
    };

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        return super.onCreateView(parent, name, context, attrs);
    }

    public void sendRequestMessage(){
        Log.d(TAG, "Host!!! " + host_user.getEmail());
        Log.d(TAG, "client_user!!! " + client_user.getEmail());

        // Generating optional game room
        game_room = UUID.randomUUID().toString();
        Log.d(TAG, "uuid: " + game_room);

        Intent intentNew = new Intent(InviteDialog.this, SendMessageToDevice.class);
        intentNew.putExtra("host_name", host_user.getEmail().split("@")[0]);
        intentNew.putExtra("host_uid", host_user.getUid());
        intentNew.putExtra("address_prefix", "user_");
        intentNew.putExtra("client_uid", client_user.getUid());
        intentNew.putExtra("game_room", game_room);
        intentNew.putExtra("request_type", Keys.REQUEST_TYPE_INVITE);



        intentNew.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startService(intentNew);
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
                startGame();
            }else {

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

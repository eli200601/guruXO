package com.app.elisoft.guru.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.elisoft.guru.R;
import com.app.elisoft.guru.Table.User;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by EliranA on 5/31/2017.
 */

public class GameActivity extends AppCompatActivity {

    private static final String TAG = GameActivity.class.getSimpleName();
    //    Player1      Player2
    User host_user, client_user;
    String game_room;
    ImageView[][] matrix;



    TextView host,client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_room);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundleStartGame");
        client_user = (User) bundle.getSerializable("UserClient");
        host_user = (User) bundle.getSerializable("UserHost");
        game_room = bundle.getString("game_room");


        initGameBoard();

//        host = (TextView) findViewById(R.id.host_name);
//        client = (TextView) findViewById(R.id.client_name);
//
//        host.setText(host_user.getEmail());
//        client.setText(client_user.getEmail());
    }

    public void initGameBoard(){
        matrix = new ImageView[3][3];

        matrix[0][0] = (ImageView) findViewById(R.id.one);
        matrix[0][1] = (ImageView) findViewById(R.id.two);
        matrix[0][2] = (ImageView) findViewById(R.id.three);
        matrix[1][0] = (ImageView) findViewById(R.id.four);
        matrix[1][1] = (ImageView) findViewById(R.id.five);
        matrix[1][2] = (ImageView) findViewById(R.id.six);
        matrix[2][0] = (ImageView) findViewById(R.id.seven);
        matrix[2][1] = (ImageView) findViewById(R.id.eight);
        matrix[2][2] = (ImageView) findViewById(R.id.nine);

        for(int i =0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                matrix[i][j].setVisibility(View.INVISIBLE);
            }
        }

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


}

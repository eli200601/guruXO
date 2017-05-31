package com.app.elisoft.guru.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.app.elisoft.guru.MainActivity;
import com.app.elisoft.guru.R;
import com.app.elisoft.guru.Table.User;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by EliranA on 5/31/2017.
 */

public class GameActivity extends AppCompatActivity {

    private static final String TAG = GameActivity.class.getSimpleName();

    User host_user, client_user;
    String game_room;

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

        host = (TextView) findViewById(R.id.host_name);
        client = (TextView) findViewById(R.id.client_name);

        host.setText(host_user.getEmail());
        client.setText(client_user.getEmail());

    }


}

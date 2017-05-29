package com.app.elisoft.guru.Dialogs;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.app.elisoft.guru.R;
import com.app.elisoft.guru.Table.User;

public class GotInviteDialog extends AppCompatActivity {

    static final String TAG = "GotInviteDialog";
    User host_user, client_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_got_invite);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundleGotInvite");
        client_user = (User) bundle.getSerializable("UserClient");
        host_user = (User) bundle.getSerializable("UserHost");

        Log.d(TAG, "Host!!! " + host_user.getEmail());
        Log.d(TAG, "client_user!!! " + client_user.getEmail());

        //need to add listeners to buttons
        // need to open a game room if ok
    }



}

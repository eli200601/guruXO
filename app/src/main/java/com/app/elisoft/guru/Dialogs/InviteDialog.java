package com.app.elisoft.guru.Dialogs;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.elisoft.guru.R;
import com.app.elisoft.guru.Services.SendMessageToDevice;
import com.app.elisoft.guru.Table.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

public class InviteDialog extends AppCompatActivity {

    static final String TAG = "InviteDialog";
    private FirebaseAuth auth;
    private String FCM_API_KEY = "AAAAK_9HT5E:APA91bHdVCtHBj8p6ovE8kGQmTWZdrLSE6b9WGH58Cio-GXI7umvkfGe8B4nG4v3At2a486iV_vUh65pOMMIr87K876QyTWKZZ2ZodZCpHBqqAvDTyO2Ux-oDJtvjA2lxNipfP87_6Mr";
    User host_user, client_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_invite);


        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundleUser");
        client_user = (User) bundle.getSerializable("UserClient");
        host_user = (User) bundle.getSerializable("UserHost");

        Log.d(TAG, "Host!!! " + host_user.getEmail());
        Log.d(TAG, "client_user!!! " + client_user.getEmail());

        // Generating optional game room
        String uniqueId = UUID.randomUUID().toString();
        Log.d(TAG, "uuid: " + uniqueId);


        Intent intentNew = new Intent(InviteDialog.this, SendMessageToDevice.class);
        intentNew.putExtra("host_name", host_user.getEmail().split("@")[0]);
        intentNew.putExtra("host_uid", host_user.getUid());
        intentNew.putExtra("client_uid", client_user.getUid());
        intentNew.putExtra("game_room", uniqueId);
        intentNew.putExtra("request_type", "invite");


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
    protected void onPause() {
        super.onPause();

    }

}

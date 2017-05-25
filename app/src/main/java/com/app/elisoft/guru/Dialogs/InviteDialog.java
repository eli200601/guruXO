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
import com.app.elisoft.guru.Table.User;

public class InviteDialog extends AppCompatActivity {

    static final String TAG = "InviteDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_invite);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundleUser");
        User client_user = (User) bundle.getSerializable("User");

        Log.d(TAG, client_user.getEmail());

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


}

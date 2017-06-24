package com.app.elisoft.guru.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.app.elisoft.guru.EventBus.MessageEvent;
import com.app.elisoft.guru.Table.User;
import com.app.elisoft.guru.TicTacToe.GameManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by EliranA on 6/25/2017.
 */

public class GetBestMove extends IntentService {

    private static final String TAG = "GetBestMove";

    private EventBus bus = EventBus.getDefault();

    GameManager gameManager = GameManager.getInstance();
    User com_user;

    public GetBestMove(String name) {
        super(name);
    }

    public GetBestMove() {
        super(null);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Starting GetBestMove");
//        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundleStartGame");
        com_user = (User) bundle.getSerializable("Player");
        Log.d(TAG, "User is: " + com_user.getEmail());

        int move = gameManager.calMove(com_user);

        MessageEvent.OnFindingBestMove event;
        event = new MessageEvent.OnFindingBestMove(move);

        bus.post(event);

    }
}

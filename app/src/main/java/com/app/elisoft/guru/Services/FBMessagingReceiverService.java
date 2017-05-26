package com.app.elisoft.guru.Services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class FBMessagingReceiverService extends FirebaseMessagingService {

    private final static String TAG = "FBMessagingReceiver";

    //Message receiver in the main lobby
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map date = remoteMessage.getData();

            String host_id = date.get("host_id").toString();
            String host_name = date.get("host_name").toString();

        }
    }
}
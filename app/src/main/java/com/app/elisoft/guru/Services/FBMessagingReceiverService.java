package com.app.elisoft.guru.Services;

import android.util.Log;

import com.app.elisoft.guru.EventBus.MessageEvent;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;


public class FBMessagingReceiverService extends FirebaseMessagingService {

    private final static String TAG = "FBMessagingReceiver";
    private EventBus bus = EventBus.getDefault();
    //Message receiver in the main lobby
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map date = remoteMessage.getData();

            MessageEvent.OnInviteToPlay event = null;


            String host_id = date.get("host_id").toString();
            String host_name = date.get("host_name").toString();

            event = new MessageEvent.OnInviteToPlay(host_name,host_id);
            bus.post(event);
        }
    }
}
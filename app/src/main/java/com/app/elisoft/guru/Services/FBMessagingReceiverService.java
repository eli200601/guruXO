package com.app.elisoft.guru.Services;

import android.util.Log;

import com.app.elisoft.guru.EventBus.MessageEvent;
import com.app.elisoft.guru.Utils.Keys;
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

            if (date.get(Keys.REQUEST_TYPE).equals(Keys.REQUEST_TYPE_INVITE)) {
                // response for Invite
                Log.d(TAG, "This is invite received message");
                MessageEvent.OnInviteToPlay event;

                String host_id = date.get("host_id").toString();
                String host_name = date.get("host_name").toString();
                String game_room = date.get("game_room").toString();

                event = new MessageEvent.OnInviteToPlay(host_name, host_id, game_room);
                bus.post(event);
                //Catch on Main Activity

            } else if (date.get(Keys.REQUEST_TYPE).equals(Keys.REQUEST_TYPE_RESPONSE_TO_INVITE)) {
                // response for Invitation
                Log.d(TAG, "This is response invite received message");
                if (date.get("message") != null) {
                    Log.d(TAG, "The client accepted your offer!!!");
                    MessageEvent.OnAcceptInvite event;
                    event = new MessageEvent.OnAcceptInvite(date.get("message").toString());
                    bus.post(event);
                }
            } else {
                if (date.get(Keys.REQUEST_TYPE).equals(Keys.MESSAGE_ARRIVE)) {
                    Log.d(TAG, "MESSAGE_ARRIVE");
                    MessageEvent.UserArrive event;
                    event = new MessageEvent.UserArrive(date.get("message").toString());
                    bus.post(event);
                } else {
                    if (date.get(Keys.REQUEST_TYPE).equals(Keys.MESSAGE_MOVE)) {
                        Log.d(TAG, "MESSAGE_MOVE");
                        MessageEvent.MoveRequest event;
                        event = new MessageEvent.MoveRequest(date.get("message").toString());
                        bus.post(event);

                    }
                }
            }
        }
    }
}
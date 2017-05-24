package com.app.elisoft.guru.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.style.UpdateAppearance;
import android.util.Log;

import com.app.elisoft.guru.Services.UpdateUserState;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        // For our recurring task, we'll just display a message
        Log.d(TAG, "BroadcastReceiver - AlarmReceiver: Started");
        // Starting To update the user status
        intent = new Intent(context, UpdateUserState.class);
        context.startService(intent);
    }
}

package com.techbytecare.kk.onlinequiz.Service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techbytecare.kk.onlinequiz.Common.Common;

/**
 * Created by kunda on 12/25/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService  {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        handleNotification(remoteMessage.getNotification().getBody());
    }

    private void handleNotification(String body) {

        Intent pushNotification = new Intent(Common.STR_PUSH);
        pushNotification.putExtra("message",body);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    }
}

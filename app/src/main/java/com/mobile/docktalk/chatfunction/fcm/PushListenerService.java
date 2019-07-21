package com.mobile.docktalk.chatfunction.fcm;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.mobile.docktalk.R;
import com.mobile.docktalk.chatfunction.App;
import com.mobile.docktalk.chatfunction.ui.activity.SplashActivity;
import com.mobile.docktalk.chatfunction.utils.ActivityLifecycle;
import com.mobile.docktalk.chatfunction.utils.NotificationUtils;
import com.quickblox.messages.services.fcm.QBFcmPushListenerService;

import java.util.Map;

public class PushListenerService extends QBFcmPushListenerService {
    private static final String TAG = PushListenerService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 1;

    protected void showNotification(String message) {
        NotificationUtils.showNotification(App.getInstance(), SplashActivity.class,
                App.getInstance().getString(R.string.notification_title), message,
                R.mipmap.ic_notification, NOTIFICATION_ID);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    protected void sendPushMessage(Map data, String from, String message) {
        super.sendPushMessage(data, from, message);
        Log.v(TAG, "From: " + from);
        Log.v(TAG, "Message: " + message);
        if (ActivityLifecycle.getInstance().isBackground()) {
            showNotification(message);
        }
    }
}
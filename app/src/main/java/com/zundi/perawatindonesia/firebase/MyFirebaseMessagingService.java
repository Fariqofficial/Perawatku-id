package com.zundi.perawatindonesia.firebase;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.zundi.perawatindonesia.App;
import com.zundi.perawatindonesia.ChatActivity;
import com.zundi.perawatindonesia.NotificationUtils;
import com.zundi.perawatindonesia.Util;
import com.zundi.perawatindonesia.model.Res;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by zindx on 07/09/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private NotificationUtils nu;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        nu = new NotificationUtils(getApplicationContext());

        if(remoteMessage == null)
            return;

        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handlePush(json.getJSONObject("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Kosongkan dlu
        if (remoteMessage.getNotification() != null) {}
    }

    private void handlePush(JSONObject json) throws JSONException {
        Res.getNotification item = new Gson().fromJson(json.toString(), Res.getNotification.class);
        if(Util.isAppIsInBackground(getApplicationContext())) {
            nu.showNotificationMessage(item.title, item.message, item.timeStamp,
                    new Intent(getApplicationContext(), ChatActivity.class));
        } else {
            Intent push = new Intent(App.PUSH_NOTIFICATION);
            push.putExtra("data", json.toString());
            LocalBroadcastManager.getInstance(this).sendBroadcast(push);
        }
    }

}

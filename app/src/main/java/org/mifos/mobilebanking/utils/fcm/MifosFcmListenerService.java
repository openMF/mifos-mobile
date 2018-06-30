/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mifos.mobilebanking.utils.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.notification.MifosNotification;
import org.mifos.mobilebanking.ui.activities.HomeActivity;
import org.mifos.mobilebanking.utils.Constants;

import java.util.Map;

public class MifosFcmListenerService extends FirebaseMessagingService {

    private static final String TAG = MifosFcmListenerService.class.getSimpleName();

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String from = remoteMessage.getFrom();
        Map<String, String> data = remoteMessage.getData();

        String message = data.get(getString(R.string.message));
        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        sendNotification(message);
        Intent registrationComplete = new Intent(Constants.NOTIFY_HOME_FRAGMENT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param message FCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(getString(R.string.notification), true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.mifos_icon)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        MifosNotification notification = new MifosNotification();
        notification.setMsg(message);
        notification.setTimeStamp(System.currentTimeMillis());
        notification.setRead(false);
        notification.save();

        notificationManager.notify(0, notificationBuilder.build());
    }
}

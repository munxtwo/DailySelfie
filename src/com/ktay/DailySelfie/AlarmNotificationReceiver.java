package com.ktay.DailySelfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    private static final String NOTIFICATION_TEXT = "Time for another selfie!";
    private static final int NOTIFICATION_ID = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent restartMainActivityIntent = new Intent(context, MainActivity.class);
        restartMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                restartMainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setTicker(NOTIFICATION_TEXT)
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setContentIntent(contentIntent)
                .setContentText(NOTIFICATION_TEXT)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}

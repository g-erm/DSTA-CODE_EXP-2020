package com.example.powerpuffgirls;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class ProximityIntentReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        Boolean entering = intent.getBooleanExtra(key, false);
        if (entering) {
            Log.d(getClass().getSimpleName(), "entering");
        }else {
            Log.d(getClass().getSimpleName(), "exiting");
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.example.powerpuffgirls";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder
                        .setSmallIcon(R.drawable.alert)
                        .setContentTitle("Proximity Alert!")
                        .setContentText("You are near a current cluster!" )
                        .build();
        Intent notificationIntent = new Intent();
        // set intent so it does not start a new activity

        PendingIntent pIntent = PendingIntent.getActivity(context, 0,notificationIntent, 0);
        notificationBuilder.setContentIntent(pIntent);
        notificationManager.notify(NOTIFICATION_ID, notification);


    }
}

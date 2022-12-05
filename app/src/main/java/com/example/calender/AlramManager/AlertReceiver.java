package com.example.calender.AlramManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        Log.v("alarmTest"   , "Ring!");
        String dbTitle;
        dbTitle = intent.getStringExtra("101");
        Log.v("showNoti", "getTitle : " + dbTitle);
        NotificationCompat.Builder nb = notificationHelper.getChannel2Notification(dbTitle, dbTitle);
        notificationHelper.getManager().notify(1,nb.build());
    }
}

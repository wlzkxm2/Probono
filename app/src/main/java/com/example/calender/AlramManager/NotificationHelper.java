package com.example.calender.AlramManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.calender.R;

public class NotificationHelper extends ContextWrapper {
    public static final String channel1ID = "channel1ID";
    public static final String channel1Name = "channel 1 ";
    public static final String channel2ID = "channel2ID";
    public static final String channel2Name = "channel 2 ";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    public void createChannels(){
        NotificationChannel channel1 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel1 = new NotificationChannel(channel1ID,channel1Name, NotificationManager.IMPORTANCE_DEFAULT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel1.enableLights(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel1.enableVibration(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel1.setLightColor(R.color.white);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getManager().createNotificationChannel(channel1);
        }

        NotificationChannel channel2 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel2 = new NotificationChannel(channel2ID,channel2Name, NotificationManager.IMPORTANCE_DEFAULT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel2.enableLights(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel2.enableVibration(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel2.setLightColor(R.color.white);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getManager().createNotificationChannel(channel2);
        }

    }

    public NotificationManager getManager() {
        if (mManager == null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message){
        return new NotificationCompat.Builder(getApplicationContext(), channel1ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.notiicon);
    }

    public NotificationCompat.Builder getChannel2Notification(String title, String message){
        return new NotificationCompat.Builder(getApplicationContext(), channel2ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.the_easy_logo);
    }

    public NotificationCompat.Builder getChannelNotification(){
        return new NotificationCompat.Builder(getApplicationContext(), channel1ID)
                .setSmallIcon(R.drawable.ic_launcher_background);
    }
}

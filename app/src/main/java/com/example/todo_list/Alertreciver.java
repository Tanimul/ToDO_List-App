package com.example.todo_list;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Alertreciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "bal", Toast.LENGTH_SHORT).show();
        Log.d("dddd","bal");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "fuad")
                .setSmallIcon(R.drawable.ic_delete)
                .setContentTitle("Test Notification")
                .setContentText("Test Notification made by fuad ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, notificationBuilder.build());
    }
}

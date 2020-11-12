package com.example.todo_list.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.todo_list.R;

public class Alertreciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "fuad")
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.githubicon))
                .setSmallIcon(R.drawable.ic_check)
                .setContentTitle("Complete your Task ")
                .setContentText("Are you Complete your Task ? ")

                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, notificationBuilder.build());
    }
}

package com.example.fresher.utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.fresher.R;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        createNotificationChannel();

    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String id = getString(R.string.default_notification_channel_id);

            CharSequence name = getString(R.string.default_notification_channel_name);

            String description = getString(R.string.default_notification_channel_description);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(id, name, importance);

            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);

        }

    }

}

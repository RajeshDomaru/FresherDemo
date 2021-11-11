package com.example.fresher.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.example.fresher.R;

public class NotificationUtils {

    public static String getChannelId(Context context) {

        if (context != null)

            return context.getResources().getString(R.string.default_notification_channel_id);

        return "Default Channel Id";

    }

    public static String getChannelName(Context context) {

        if (context != null)

            return context.getResources().getString(R.string.default_notification_channel_name);

        return "Default Channel Name";

    }

    public static String getChannelDescription(Context context) {

        if (context != null)

            return context.getResources().getString(R.string.default_notification_channel_description);

        return "Default Channel Description";

    }

    public static void createNotificationChannel(Context context) {

        if (context != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String id = getChannelId(context);

            CharSequence name = getChannelName(context);

            String description = getChannelDescription(context);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(id, name, importance);

            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);

        }

    }

}

package com.example.fresher.utils;

import android.app.Application;

import com.google.firebase.messaging.FirebaseMessaging;

public class MyApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        NotificationUtils.createNotificationChannel(getApplicationContext());

    }

}
